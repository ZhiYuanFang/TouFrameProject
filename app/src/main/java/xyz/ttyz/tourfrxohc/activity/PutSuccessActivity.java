package xyz.ttyz.tourfrxohc.activity;

import android.content.Intent;
import android.os.CountDownTimer;

import androidx.databinding.ObservableField;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.mylibrary.method.RetrofitUtils;
import xyz.ttyz.mylibrary.method.RxOHCUtils;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.tourfrxohc.BaseApplication;
import xyz.ttyz.tourfrxohc.DefaultUtils;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityPutSuccessBinding;
import xyz.ttyz.tourfrxohc.http.BaseSubscriber;
import xyz.ttyz.tourfrxohc.utils.EncrUtil;
import xyz.ttyz.tourfrxohc.utils.PwdUtils;
import xyz.ttyz.tourfrxohc.utils.TimeUtils;
import xyz.ttyz.tourfrxohc.utils.ToastUtils;

/**
 * @author 投投
 * @date 2023/12/26
 * @email 343315792@qq.com
 */
public class PutSuccessActivity extends BaseActivity<ActivityPutSuccessBinding>{
    public static void show(int doorNumber, String keyInterBoxCode, String boxPassword){
        Intent intent = new Intent(ActivityManager.getInstance(), PutSuccessActivity.class);
        intent.putExtra("door", doorNumber);
        intent.putExtra("keyInterBoxCode", keyInterBoxCode);
        intent.putExtra("boxPassword", boxPassword);

        ActivityManager.getInstance().startActivity(intent);
    }

    CountDownTimer timer;
    int doorNumber;
    String keyInterBoxCode, boxPassword;
    public ObservableField<String> autoFiled = new ObservableField<>("30s后自动退出本次验证");
    @Override
    protected int initLayoutId() {
        return R.layout.activity_put_success;
    }

    @Override
    protected String[] initPermission() {
        return new String[0];
    }

    @Override
    protected void initData() {
        mBinding.setContext(this);
        keyInterBoxCode = getIntent().getStringExtra("keyInterBoxCode");
        boxPassword = getIntent().getStringExtra("boxPassword");
        doorNumber = getIntent().getIntExtra("door", 0);
        DefaultUtils.resetDoorWithKey(doorNumber, true);
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
        //入柜成功
        Map map = new HashMap();
        map.put("boxNum", doorNumber);
        map.put("boxPassword", boxPassword);
        map.put("interBoxDate", TimeUtils.getServerNeedDate());
        map.put("keyCabinetId", PwdUtils.getWareHouseCode());
        map.put("keyCabinetType", 2);
        map.put("keyInterBoxCode", keyInterBoxCode);
        new RxOHCUtils<>(ActivityManager.getInstance()).executeApi(BaseApplication.apiService.afterInterBoxKey(RetrofitUtils.getNormalBody(map)), new BaseSubscriber<Object>(PutSuccessActivity.this) {
            @Override
            public void success(Object data) {

            }
        });
    }

    public OnClickAdapter.onClickCommand onClickExit = new OnClickAdapter.onClickCommand() {
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
