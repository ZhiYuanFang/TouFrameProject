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
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityTicketScanBinding;

public class TicketScanActivity extends BaseTouActivity<ActivityTicketScanBinding> {
    public static void show(){
        Intent intent = new Intent(ActivityManager.getInstance(), TicketScanActivity.class);
        ActivityManager.getInstance().startActivity(intent);
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

    public OnClickAdapter.onClickCommand reLoginClick = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            LoginActivity.show();
            finish();
        }
    };

    public OnClickAdapter.onClickCommand scanERCode = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            Scanner.with(TicketScanActivity.this)
                    .setBorderSize(BarCodeUtil.dp2px(TicketScanActivity.this, 200))            // 设置扫码框大小
//        .setBorderSize(BarCodeUtil.dp2px(this, 200), BarCodeUtil.dp2px(this, 100))     // 设置扫码框长宽（如果同时调用了两个setBorderSize方法优先使用上一个）
//        .setHorizontalScanLine()                              // 设置扫码线为水平方向（从左到右）
                    .setScanMode(ScanView.SCAN_MODE_TINY)                   // 扫描区域 0：混合 1：只扫描框内 2：只扫描整个屏幕
//        .setBarcodeFormat(BarcodeFormat.EAN_13)                 // 设置扫码格式
                    .setTitle("My Scan View")                               // 扫码界面标题
                    .showAlbum(true)                                        // 显示相册(默认为true)
                    .setScanNoticeText("扫描二维码")                         // 设置扫码文字提示
                    .setFlashLightOnText("打开闪光灯")                       // 打开闪光灯提示
                    .setFlashLightOffText("关闭闪光灯")                      // 关闭闪光灯提示
                    .setFlashLightInvisible()                               // 不使用闪光灯图标及提示
                    .continuousScan()                                       // 连续扫码，不关闭扫码界面
                    .enableOpenCVDetect(false)                              // 关闭OpenCV探测，避免没有发现二维码也放大的现象，但是这样可能降低扫码的成功率，请结合业务关闭（默认开启）

                    .setOnScanResultDelegate(new ScanActivityDelegate.OnScanDelegate() { // 接管扫码成功的数据
                        @Override
                        public void onScanResult(Activity activity, String result, BarcodeFormat format) {
                            // TODO: 2022/5/24 result
                            System.out.println("扫码结果：" + result);
                            ScanDetailActivity.show();
                            finish();
                        }
                    })
                    .start();
        }
    };

    public OnClickAdapter.onClickCommand scanIDCard = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {

        }
    };

    public OnClickAdapter.onClickCommand scanOKClick = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            ScanDetailActivity.show();
            finish();
        }
    };
}
