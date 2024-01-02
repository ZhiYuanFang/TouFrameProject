package xyz.ttyz.tourfrxohc.activity;

import android.content.Intent;
import android.os.CountDownTimer;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.mylibrary.method.RetrofitUtils;
import xyz.ttyz.mylibrary.method.RxOHCUtils;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.tourfrxohc.BaseApplication;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityGetSuccessBinding;
import xyz.ttyz.tourfrxohc.http.BaseSubscriber;
import xyz.ttyz.tourfrxohc.models.CarModel;
import xyz.ttyz.tourfrxohc.utils.PwdUtils;
import xyz.ttyz.tourfrxohc.utils.ToastUtils;

/**
 * @author 投投
 * @date 2023/12/26
 * @email 343315792@qq.com
 */
public class GetSuccessActivity extends BaseActivity<ActivityGetSuccessBinding>{

    public static final int PwdComing = 1;
    public static final int ListComing = 2;
    public static void show(int comingType){
        show(comingType, "");
    }
    public static void show(int comingType, String keyOutBoxCheckCode){
        Intent intent = new Intent(ActivityManager.getInstance(), GetSuccessActivity.class);
        intent.putExtra("comingType", comingType);
        intent.putExtra("keyOutBoxCheckCode", keyOutBoxCheckCode);
        ActivityManager.getInstance().startActivity(intent);
    }

    public ObservableInt comingType = new ObservableInt(ListComing);
    String keyOutBoxCheckCode;
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
        keyOutBoxCheckCode = getIntent().getStringExtra("keyOutBoxCheckCode");
        comingType.set(getIntent().getIntExtra("comingType", PwdComing));
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

        Map map = new HashMap();
        map.put("keyCabinetType", 2);
        map.put("keyOutBoxCheckCode", keyOutBoxCheckCode);
        map.put("keyCabinetId", PwdUtils.getWareHouseCode());
        new RxOHCUtils<>(ActivityManager.getInstance()).executeApi(BaseApplication.apiService.canOutBoxKeyList(RetrofitUtils.getNormalBody(map)), new BaseSubscriber<List<CarModel>>(GetSuccessActivity.this) {
            @Override
            public void success(List<CarModel> data) {
                if(data == null || data.isEmpty()){
//                    ToastUtils.showError("已无待取用列表");
                    comingType.set(PwdComing);
                }
            }
        });
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
