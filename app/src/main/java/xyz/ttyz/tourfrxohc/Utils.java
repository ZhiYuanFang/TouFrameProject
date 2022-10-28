package xyz.ttyz.tourfrxohc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;



//import me.devilsen.czxing.Scanner;
//import me.devilsen.czxing.code.BarcodeFormat;
//import me.devilsen.czxing.util.BarCodeUtil;
//import me.devilsen.czxing.view.ScanActivityDelegate;
//import me.devilsen.czxing.view.ScanView;
import com.king.zxing.CaptureActivity;

import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.tourfrxohc.activity.CaptureNextActivity;
import xyz.ttyz.tourfrxohc.activity.CardActivity;
import xyz.ttyz.tourfrxohc.activity.IDCardScanActivity;
import xyz.ttyz.tourfrxohc.activity.ScanDetailActivity;
import xyz.ttyz.tourfrxohc.activity.TicketScanActivity;

public class Utils {

    public static final int SCAN_ERCODE_REQUEST = 2;/* 身份证识别 返回码*/
    public static void scanERCode(){
        ActivityManager.getInstance().startActivityForResult(new Intent(ActivityManager.getInstance(), CaptureNextActivity.class),SCAN_ERCODE_REQUEST);
//        Scanner.with(ActivityManager.getInstance())
//                .setBorderSize(BarCodeUtil.dp2px(ActivityManager.getInstance(), 200))            // 设置扫码框大小
////        .setBorderSize(BarCodeUtil.dp2px(this, 200), BarCodeUtil.dp2px(this, 100))     // 设置扫码框长宽（如果同时调用了两个setBorderSize方法优先使用上一个）
////        .setHorizontalScanLine()                              // 设置扫码线为水平方向（从左到右）
//                .setScanMode(ScanView.SCAN_MODE_TINY)                   // 扫描区域 0：混合 1：只扫描框内 2：只扫描整个屏幕
////        .setBarcodeFormat(BarcodeFormat.EAN_13)                 // 设置扫码格式
//                .setTitle("扫描二维码")                               // 扫码界面标题
//                .showAlbum(false)                                        // 显示相册(默认为true)
//                .setScanNoticeText("打开公众号二维码进行扫码")                         // 设置扫码文字提示
//                .setFlashLightOnText("打开闪光灯")                       // 打开闪光灯提示
//                .setFlashLightOffText("关闭闪光灯")                      // 关闭闪光灯提示
//                .setFlashLightInvisible()                               // 不使用闪光灯图标及提示
////                .continuousScan()                                       // 连续扫码，不关闭扫码界面
//                .enableOpenCVDetect(true)                              // 关闭OpenCV探测，避免没有发现二维码也放大的现象，但是这样可能降低扫码的成功率，请结合业务关闭（默认开启）
//                .setOnScanResultDelegate(new ScanActivityDelegate.OnScanDelegate() { // 接管扫码成功的数据
//                    @Override
//                    public void onScanResult(Activity activity, String result, BarcodeFormat format) {
//                        ScanDetailActivity.show(result);
//                        activity.finish();
//                    }
//                })
//                .start();
    }

    public static final int SCAN_IDCARD_REQUEST = 1;/* 身份证识别 返回码*/

    public static void scanIDCard(){
//        IDCardScanActivity.show();
        CardActivity.show();
    }
}
