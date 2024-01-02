package xyz.ttyz.toubasemvvm.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.CallSuper;
import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ViewDataBinding;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.XXPermissions;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.toubasemvvm.R;
import xyz.ttyz.toubasemvvm.event.InputHideEvent;
import xyz.ttyz.toubasemvvm.event.NetEvent;
import xyz.ttyz.toubasemvvm.utils.Constants;
import xyz.ttyz.toubasemvvm.utils.DensityUtil;
import xyz.ttyz.toubasemvvm.utils.DialogUtils;
import xyz.ttyz.toubasemvvm.utils.NetworkUtil;
import xyz.ttyz.toubasemvvm.utils.StatusBarUtil;
import xyz.ttyz.toubasemvvm.utils.ToastUtil;


public abstract class BaseTouActivity<T extends ViewDataBinding> extends SwipeBackActivity implements LifecycleProvider<ActivityEvent> {
    protected T mBinding;

    public ObservableBoolean loadEnd = new ObservableBoolean(true);

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void netChange(NetEvent netEvent){
        if(netEvent.isAvailable()){
            missNoNetControl();
        } else {
            showNoNetControl();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.popActivity(this);
        lifecycleSubject.onNext(ActivityEvent.CREATE);
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
            setTheme(R.style.MimeTheme);//设置全屏， 防止api 26闪退
        } else setTheme(R.style.TouTheme);//使用swipeBack 需要设置主题，否则会显示titlebar
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarFontIconDark(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this, 0x55000000);
        }

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            //低版本网络监听
            Intent intent = new Intent();
            intent.setAction("service.NetworkStateService");
            startService(intent);
        }

        mBinding = DataBindingUtil.setContentView(this, initLayoutId());
        //页面基础渲染之后，延迟300毫秒做逻辑渲染，防止逻辑渲染卡顿，做延迟加载，会导致需要在onResume做功能的时候，onCreate没有初始化好，导致功能异常
//        Observable.timer(300, TimeUnit.MILLISECONDS)
//                .subscribe(new Observer<Long>() {
//                    @Override
//                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(@io.reactivex.annotations.NonNull Long aLong) {
//
//                    }
//
//                    @Override
//                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        doInit();
//                    }
//                });
        doInit();

        if(NetworkUtil.isNetWorkConnected(this)){
            missNoNetControl();
        } else {
            showNoNetControl();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(mBinding == null){return;}
        doInit();
    }

    //region private
    protected void doInit(){
        if(initPermission() != null && initPermission().length > 0){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                XXPermissions.with(this)
                        .permission(initPermission())
                        .request(new OnPermissionCallback() {
                            @Override
                            public void onGranted(List<String> permissions, boolean all) {
                                if(all) init();
                            }

                            @Override
                            public void onDenied(final List<String> permissions,final boolean never) {

                                StringBuilder stringBuilder = new StringBuilder();
                                for(String str: permissions){
                                    stringBuilder.append("\n").append(str);
                                }
                                DialogUtils.showDialog("当前页面需要授权：" + stringBuilder + "\n否则将不能进入", new DialogUtils.DialogButtonModule("前往授权", new DialogUtils.DialogClickDelegate() {
                                    @Override
                                    public void click(DialogUtils.DialogButtonModule dialogButtonModule) {
                                        if(never){
                                            XXPermissions.startPermissionActivity(ActivityManager.getInstance(), permissions);
                                        } else {
                                            doInit();
                                        }
                                    }
                                }), new DialogUtils.DialogButtonModule("暂不授权", new DialogUtils.DialogClickDelegate() {
                                    @Override
                                    public void click(DialogUtils.DialogButtonModule dialogButtonModule) {
                                        ActivityManager.getInstance().finish();
                                    }
                                }));
                            }
                        });
            } else  init();
        } else  init();
    }

    protected void init(){
        initData();
        initServer();
    }
    //endregion

    //region method
    protected abstract int initLayoutId();
    /**
     *
     * @return 当前页面所需权限，不满足则退出界面
     */
    protected abstract String[] initPermission();

    /**
     * 正常业务逻辑处理，往往在这里处理页面的第一层逻辑 数据初始化
     */
    protected abstract void initData();

    /**
     * 接口请求
     */
    protected abstract void initServer();


    public void showNoNetControl() {
        ToastUtil.showToast(getString(R.string.no_net_work));
    }

    public void missNoNetControl() {

    }
    //endregion

    //region public
    public OnRefreshListener refreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh(@NonNull RefreshLayout refreshLayout) {
            loadEnd.set(false);
            initServer();
        }
    };
    //endregion

    //region lifeCycleSubject
    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();

    @Override
    @NonNull
    @CheckResult
    public final Observable<ActivityEvent> lifecycle() {
        return lifecycleSubject.hide();
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindActivity(lifecycleSubject);
    }

    @Override
    @CallSuper
    protected void onStart() {
        super.onStart();
        lifecycleSubject.onNext(ActivityEvent.START);
    }

    @Override
    @CallSuper
    protected void onResume() {
        super.onResume();
        lifecycleSubject.onNext(ActivityEvent.RESUME);
    }

    @Override
    @CallSuper
    protected void onPause() {
        lifecycleSubject.onNext(ActivityEvent.PAUSE);
        super.onPause();
    }

    @Override
    @CallSuper
    protected void onStop() {
        lifecycleSubject.onNext(ActivityEvent.STOP);
        super.onStop();
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        lifecycleSubject.onNext(ActivityEvent.DESTROY);
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        ActivityManager.exitActivity(this);
    }

    //如果是第一个界面 防返回误点

    private boolean mBackKeyPressed;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(ActivityManager.getActivityList().size() == 1){
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (!mBackKeyPressed) {
                    ToastUtil.showToast( getString(R.string.back_again));
                    mBackKeyPressed = true;
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            mBackKeyPressed = false;
                        }
                    }, 2000);
                    return true;
                } else {
                    this.finish();
                    ActivityManager.exitActivity(this);
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(0);
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    //endregion

    //region 键盘外触隐藏
    InputMethodManager imm;

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (dealDispatchKeyEvent(event)) {
            return true;
        } else
            return super.dispatchKeyEvent(event);
    }

    protected boolean dealDispatchKeyEvent(KeyEvent event) {
//        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
//            //隐藏键盘
//            imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            if (imm != null && imm.isActive()) {
//                try {
//                    IBinder iBinder = getCurrentFocus().getWindowToken();
//                    imm.hideSoftInputFromWindow(iBinder, 0);
//                    EventBus.getDefault().post(new InputHideEvent());
//                } catch (NullPointerException e) {
//                    e.printStackTrace();
//                }
//            }
//            return true;
//        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    if (v != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                    EventBus.getDefault().post(new InputHideEvent());
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }

    protected boolean isShouldHideInput(View v, MotionEvent event) {
        if(v instanceof EditText){
            int height = Constants.WINDOW_HEIGHT - DensityUtil.dp2px( 120f);//120的鍵盤高度， 可以適當調節 当前需求没有超过的
            int eY = (int) event.getY();
            return eY < height;
        } else return false;
    }

    //endregion
}
