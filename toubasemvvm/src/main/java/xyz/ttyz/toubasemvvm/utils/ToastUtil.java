package xyz.ttyz.toubasemvvm.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import xyz.ttyz.tou_example.ActivityManager;


/**
 * Toast统一管理类
 */
public class ToastUtil {
    static CustomDialogInterface dialogInterface;
    private static Toast mToast;

    public static void setCustomDialogInterface(CustomDialogInterface customDialogInterface) {
        dialogInterface = customDialogInterface;
    }

    /**
     * 显示提示框，防止同一时间多次触发toast，引起toast一直显示的问题
     */
    @SuppressLint("ShowToast")
    public static void showToast(boolean center, final Context c, final String msg){
        if(dialogInterface != null && center){//调用自定义吐司， 防止通知权限问题导致不显示异常
            dialogInterface.show(msg);
            return;
        }
        //-------一下忽略
        if(TextUtils.isEmpty(msg) || c == null){
            return;
        }
        if(!PushUtils.isOutPushOpen()){
            if(dialogInterface != null){
                dialogInterface.showDialog();
                return;
            }
        }
        ((Activity)c).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null==mToast){
                    mToast = Toast.makeText(c,msg,Toast.LENGTH_LONG);
                }else{
                    mToast.setText(msg);
                }
                mToast.show();
            }
        });
    }

    public static void showToast(String msg){
        Activity activity = ActivityManager.getInstance();
        if(activity != null){
            showToast(true, activity, msg);
        }
    }
}