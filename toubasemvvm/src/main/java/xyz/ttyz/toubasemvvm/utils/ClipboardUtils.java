package xyz.ttyz.toubasemvvm.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import xyz.ttyz.tou_example.ActivityManager;


public class ClipboardUtils {
    public static void setClipboard(String txt){
//获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) ActivityManager.getInstance().getSystemService(Context.CLIPBOARD_SERVICE);
// 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", txt);
// 将ClipData内容放到系统剪贴板里。
        if (cm != null) {
            cm.setPrimaryClip(mClipData);
            ToastUtil.showToast("复制成功");
            System.out.println("复制：" + txt);
        } else {
            ToastUtil.showToast("当前设备不支持复制操作");
        }
    }
}
