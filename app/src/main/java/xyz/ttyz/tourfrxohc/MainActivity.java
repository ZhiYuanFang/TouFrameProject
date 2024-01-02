package xyz.ttyz.tourfrxohc;

import android.Manifest;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import java.util.List;
import java.util.concurrent.locks.Lock;

import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.utils.ToastUtil;
import xyz.ttyz.toubasemvvm.vm.ToolBarViewModel;
import xyz.ttyz.tourfrxohc.activity.BaseActivity;
import xyz.ttyz.tourfrxohc.activity.PannelActivity;
import xyz.ttyz.tourfrxohc.activity.ResetSuperPwdActivity;
import xyz.ttyz.tourfrxohc.activity.WarehouseBindActivity;
import xyz.ttyz.tourfrxohc.databinding.ActivityMainBinding;
import xyz.ttyz.tourfrxohc.utils.LockUtil;
import xyz.ttyz.tourfrxohc.utils.PwdUtils;

public class MainActivity extends BaseActivity<ActivityMainBinding> {
    @Override
    protected int initLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected String[] initPermission() {
        return new String[]{
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};
    }

    public ObservableBoolean isSuccessConnectSerial = new ObservableBoolean(false);
    private static final String TAG = "MainActivity";
    @Override
    protected void initData() {
        mBinding.setContext(this);

        isSuccessConnectSerial.set(LockUtil.getInstance(null).connectSerialPort());
        if(!isSuccessConnectSerial.get()){
            return;
        }
        DefaultUtils.clearErrorDoor();
        // 判断是否绑定仓库
        Log.i(TAG, "initData: PwdUtils.getSuperPwd() -> " + PwdUtils.getSuperPwd().isEmpty());
        if(PwdUtils.getWareHouseCode().isEmpty()){
            // 未曾绑定
            WarehouseBindActivity.show();
        } else {
            // 已绑定过了, 判断是否有超级密码
            if(PwdUtils.getSuperPwd().isEmpty()){
                // 没有超级密码
                ResetSuperPwdActivity.show(false);
            } else {
                // 有超级密码
                PannelActivity.show();
            }
        }
        finish();
    }

    public OnClickAdapter.onClickCommand onClickRetry = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            initData();
        }
    };

    @Override
    protected void initServer() {
    }

}


