package xyz.ttyz.toubasemvvm.ui;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

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
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;
import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.toubasemvvm.R;
import xyz.ttyz.toubasemvvm.event.NetEvent;
import xyz.ttyz.toubasemvvm.utils.DialogUtils;
import xyz.ttyz.toubasemvvm.utils.ToastUtil;

public abstract class BaseTouFragment<T extends ViewDataBinding> extends Fragment implements LifecycleProvider<ActivityEvent> {
    public BaseTouFragment() {
    }

    //Fragment的View加载完毕的标记
    protected boolean isViewCreated;

    //Fragment对用户可见的标记
    protected boolean isUIVisible;

    protected T mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(initLayoutId(), container, false);
        mBinding = DataBindingUtil.bind(view);
        initVariable(mBinding);

        isViewCreated = true;
        lazyLoad();
        return view;
    }
    //import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT; 在viewPagerAdapter使用该行为才会走visibleHint回调
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isUIVisible = isVisibleToUser;
        lazyLoad();//猜测这个方法之所以被删除， 因为系统在修改字体后， 重新打开APP 会走这个方法， 但是此时的fragment是丢弃的，它走完这个方法之后 会重新走创建fragment， 所以在lazyLoad中，加了判空保护
    }

    protected abstract void initVariable(T mBinding);
    protected void lazyLoad() {
        if (!isInViewPager() || (isUIVisible && isViewCreated)) {
            isUIVisible = false;
            onCreate();
        }
    }

    //是否在ViewPager中使用Fragment
    protected abstract boolean isInViewPager();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        onDestroy();
    }

    //region same activity

    public ObservableBoolean loadEnd = new ObservableBoolean(true);

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void netChange(NetEvent netEvent){
        if(netEvent.isAvailable()){
            missNoNetControl();
        } else {
            showNoNetControl();
        }
    }

    protected void onCreate() {
        lifecycleSubject.onNext(ActivityEvent.CREATE);
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        //页面基础渲染之后，延迟300毫秒做逻辑渲染，防止逻辑渲染卡顿
        Observable.timer(300, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull Long aLong) {

                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        doInit();
                    }
                });
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

    private void init(){
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
    public void onStart() {
        super.onStart();
        lifecycleSubject.onNext(ActivityEvent.START);
    }

    @Override
    @CallSuper
    public void onResume() {
        super.onResume();
        lifecycleSubject.onNext(ActivityEvent.RESUME);
    }

    @Override
    @CallSuper
    public void onPause() {
        lifecycleSubject.onNext(ActivityEvent.PAUSE);
        super.onPause();
    }

    @Override
    @CallSuper
    public void onStop() {
        lifecycleSubject.onNext(ActivityEvent.STOP);
        super.onStop();
    }

    @Override
    @CallSuper
    public void onDestroy() {
        lifecycleSubject.onNext(ActivityEvent.DESTROY);
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }
    //endregion

    //endregion


}
