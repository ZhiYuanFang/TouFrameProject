package xyz.ttyz.tourfrxohc.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.databinding.ObservableInt;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.mylibrary.method.RxOHCUtils;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.ui.BaseTouActivity;
import xyz.ttyz.toubasemvvm.utils.DialogUtils;
import xyz.ttyz.tourfrxohc.BaseApplication;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityKeyBinding;
import xyz.ttyz.tourfrxohc.http.BaseSubscriber;
import xyz.ttyz.tourfrxohc.models.UserModel;
import xyz.ttyz.tourfrxohc.utils.UserUtils;

public class KeyActivity extends BaseTouActivity<ActivityKeyBinding> {

    public static void show(long roomId){
        Intent intent = new Intent(ActivityManager.getInstance(), KeyActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("roomId", roomId);
        ActivityManager.getInstance().startActivity(intent);
    }
    long roomId;

    public ObservableInt intervalTimeFiled = new ObservableInt(10);
    Disposable intervableDisposable;
    @Override
    protected int initLayoutId() {
        return R.layout.activity_key;
    }

    @Override
    protected String[] initPermission() {
        return new String[0];
    }

    @Override
    protected void initData() {
        mBinding.setContext(this);
        roomId = getIntent().getLongExtra("roomId", 0);
        //启动倒计时
        intervableDisposable = Observable.interval(0, 1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        intervalTimeFiled.set(10 - Integer.parseInt(String.valueOf(aLong)));
                        if(intervalTimeFiled.get() < 0){
                            //自动结束投放
//                            Random random = new Random();
//                            int number = random.nextInt(3) + 1;
                            put(mBinding.tv1, 1);
                        }
                    }
                });
    }

    @Override
    protected void initServer() {

    }

    public void selectOne(View view, final int number){
        DialogUtils.showDialog("请选择操作", new DialogUtils.DialogButtonModule("投放", new DialogUtils.DialogClickDelegate() {
            @Override
            public void click(DialogUtils.DialogButtonModule dialogButtonModule) {
                put( view,number);
            }
        }), new DialogUtils.DialogButtonModule("查看", new DialogUtils.DialogClickDelegate() {
            @Override
            public void click(DialogUtils.DialogButtonModule dialogButtonModule) {
                look( view,number);
            }
        }));
    }

    private void put(View view, int number){
        //请求接口，告诉后台投放
        confirmPut(view, number, true);
    }

    private void look(View view, int number){
        //请求接口，告诉后台查看
        confirmPut(view, number, false);
    }

    private void confirmPut(final View view, int number, boolean put){
        intervableDisposable.dispose();
        TextView textView = (TextView) view;
        new RxOHCUtils<UserModel>(this).executeApi(BaseApplication.apiService.confirmPutKey(roomId, UserUtils.getCurUserModel().getId(), number), new BaseSubscriber<UserModel>(this) {
            @Override
            public void success(UserModel data) {
                //打开门，看里面的内容，如果是投放，动画消除或生成
                //动画1秒， 立刻返回上一级界面

                boolean hasKey = data.isPutObjectHasKey();
                textView.setText(hasKey ? "有" : "");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);
            }

            @Override
            public String initCacheKey() {
                return null;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(intervableDisposable != null){
            intervableDisposable.dispose();
        }
    }
}
