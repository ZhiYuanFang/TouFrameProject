package xyz.ttyz.tourfrxohc.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.provider.MediaStore;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.List;

import me.devilsen.czxing.Scanner;
import me.devilsen.czxing.code.BarcodeFormat;
import me.devilsen.czxing.util.BarCodeUtil;
import me.devilsen.czxing.view.ScanActivityDelegate;
import me.devilsen.czxing.view.ScanView;
import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.ui.BaseTouActivity;
import xyz.ttyz.toubasemvvm.utils.ToastUtil;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.Utils;
import xyz.ttyz.tourfrxohc.databinding.ActivityTicketScanBinding;

public class TicketScanActivity extends BaseTouActivity<ActivityTicketScanBinding> {
    public static void show(){
        Intent intent = new Intent(ActivityManager.getInstance(), TicketScanActivity.class);
        ActivityManager.getInstance().startActivity(intent);
        ActivityManager.popOtherActivity(TicketScanActivity.class);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_ticket_scan;
    }

    @Override
    protected String[] initPermission() {
        return new String[]{Manifest.permission.CAMERA};
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initData() {
        mBinding.setContext(this);
    }

    @Override
    protected void initServer() {

    }

    public OnClickAdapter.onClickCommand scanERCode = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            Utils.scanERCode();
        }
    };

    public OnClickAdapter.onClickCommand scanIDCard = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            ToastUtil.showToast("身份证");
        }
    };

}
