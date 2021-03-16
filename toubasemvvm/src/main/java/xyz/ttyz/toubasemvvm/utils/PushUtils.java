package xyz.ttyz.toubasemvvm.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.core.app.NotificationManagerCompat;

import xyz.ttyz.tou_example.ActivityManager;


public class PushUtils {
    public static void judgePushPower(){
        if(!isOutPushOpen()){
            DialogUtils.showDialog("通知管理", "检测到您没有打开通知权限，请前往通知管理，开启多元幼教通知功能", new DialogUtils.DialogButtonModule("立即前往", new DialogUtils.DialogClickDelegate() {
                @Override
                public void click(DialogUtils.DialogButtonModule dialogButtonModule) {
                    Intent localIntent = new Intent();
                    localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    localIntent.setData(Uri.fromParts("package", ActivityManager.getInstance().getPackageName(), null));
                    ActivityManager.getInstance().startActivity(localIntent);
                }
            }));
        }
    }

    public static boolean isOutPushOpen() {
        NotificationManagerCompat manager = NotificationManagerCompat.from(ActivityManager.getInstance());
        return manager.areNotificationsEnabled();
    }
}
