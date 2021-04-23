package xyz.ttyz.tourfrxohc.activity;

import android.content.Intent;

import androidx.databinding.ObservableInt;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.toubasemvvm.ui.BaseTouActivity;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityKeyBinding;

public class KeyActivity extends BaseTouActivity<ActivityKeyBinding> {

    public static void show(long roomId){
        Intent intent = new Intent(ActivityManager.getInstance(), KeyActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("roomId", roomId);
        ActivityManager.getInstance().startActivity(intent);
    }

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
        //启动倒计时
        intervableDisposable = Observable.interval(0, 1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        intervalTimeFiled.set(10 - Integer.parseInt(String.valueOf(aLong)));
                        if(intervalTimeFiled.get() < 0){
                            //自动结束投放
// TODO: 2021/4/23  
                        }
                    }
                });
    }

    @Override
    protected void initServer() {

    }
}
