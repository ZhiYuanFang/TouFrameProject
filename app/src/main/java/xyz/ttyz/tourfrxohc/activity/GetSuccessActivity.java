package xyz.ttyz.tourfrxohc.activity;

import android.content.Intent;
import android.os.CountDownTimer;

import androidx.databinding.ObservableField;

import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityGetSuccessBinding;

/**
 * @author 投投
 * @date 2023/12/26
 * @email 343315792@qq.com
 */
public class GetSuccessActivity extends BaseActivity<ActivityGetSuccessBinding>{

    public static final int PwdComing = 1;
    public static final int ListComing = 2;
    public static void show(int comingType){
        Intent intent = new Intent(ActivityManager.getInstance(), GetSuccessActivity.class);
        intent.putExtra("comingType", comingType);
        ActivityManager.getInstance().startActivity(intent);
    }

    public int comingType;

    CountDownTimer timer;
    public ObservableField<String> autoFiled = new ObservableField<>("30s后自动退出本次验证");
    @Override
    protected int initLayoutId() {
        return R.layout.activity_get_success;
    }

    @Override
    protected String[] initPermission() {
        return new String[0];
    }

    @Override
    protected void initData() {
        mBinding.setContext(this);
        comingType = getIntent().getIntExtra("comingType", PwdComing);
        timer = new CountDownTimer(30 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                autoFiled.set( l / 1000 + "s后自动退出本次验证");
            }

            @Override
            public void onFinish() {
                finish();
            }
        };
        timer.start();
    }

    @Override
    protected void initServer() {

    }

    public OnClickAdapter.onClickCommand onClickExit = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            PannelActivity.show();
            finish();
        }
    };

    public OnClickAdapter.onClickCommand onClickContinue = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(timer != null){
            timer.cancel();
            timer = null;
        }
    }
}
