package xyz.ttyz.tourfrxohc.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.databinding.ObservableField;

import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.tourfrxohc.DefaultUtils;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityPutDetailBinding;
import xyz.ttyz.tourfrxohc.models.CarModel;
import xyz.ttyz.tourfrxohc.utils.LockUtil;
import xyz.ttyz.tourfrxohc.utils.ToastUtils;

/**
 * @author 投投
 * @date 2023/12/26
 * @email 343315792@qq.com
 */
public class PutDetailActivity extends BaseActivity<ActivityPutDetailBinding>{
    private static final String TAG = "PutDetailActivity";
    public static void show(CarModel carModel){
        Log.i(TAG, "show: ");
        Intent intent = new Intent(ActivityManager.getInstance(), PutDetailActivity.class);
        intent.putExtra("carModel", carModel);
        ActivityManager.getInstance().startActivity(intent);
    }
    CountDownTimer timer;

    public ObservableField<String> autoFiled = new ObservableField<>("30s后自动退出本次验证");

    @Override
    protected int initLayoutId() {
        return R.layout.activity_put_detail;
    }

    @Override
    protected String[] initPermission() {
        return new String[0];
    }

    @Override
    protected void initData() {
        mBinding.setContext(this);
        mBinding.setCarModel((CarModel) getIntent().getSerializableExtra("carModel"));
        int doorNumber = DefaultUtils.getDoorAddress(true, mBinding.getCarModel());
        Log.i(TAG, "initData: 存入钥匙 -> " + doorNumber);
        if(doorNumber < 1){
            ToastUtils.showError("当前柜已满");
            finish();
            return;
        }
        final boolean[] isOpen = new boolean[1];
        LockUtil.getInstance(new LockUtil.LockDelegate() {
            @Override
            public void callBackOpen(int keyNumber) {
                isOpen[0] = true;
                ToastUtils.showSuccess(keyNumber + "号门已开");
                // 门开成功 生成密码
                // 门开成功 请求一次
            }

            @Override
            public void callBackState(boolean[] readArr) {
                if(isOpen[0]){
                    if(!readArr[doorNumber - 1]){
                        // 从开到关,表示存放成功【将钥匙放入柜门并关闭】
                        PutSuccessActivity.show(doorNumber);
                        finish();
                    }
                }
            }
        }).openKey(doorNumber);

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(timer != null){
            timer.cancel();
            timer = null;
        }
        LockUtil.clearCallBack();
    }
}
