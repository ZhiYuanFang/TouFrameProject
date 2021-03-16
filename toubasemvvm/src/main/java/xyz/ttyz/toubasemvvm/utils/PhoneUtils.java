package xyz.ttyz.toubasemvvm.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.XXPermissions;

import java.util.List;

import xyz.ttyz.tou_example.ActivityManager;

/**
 * author: MoGang
 * created on: 2018/11/28 16:00
 * description:
 */
public class PhoneUtils {

    public static void call(final String tel) {
        XXPermissions.with(ActivityManager.getInstance())
                .permission(Manifest.permission.CALL_PHONE)
                .request(new OnPermissionCallback() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel));
                        ActivityManager.getInstance().startActivity(intent);
                    }

                    @Override
                    public void onDenied(final List<String> permissions, final boolean never) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for(String str: permissions){
                            stringBuilder.append("\n").append(str);
                        }
                        DialogUtils.showDialog("当前功能需要授权：" + stringBuilder + "\n否则将无法使用", new DialogUtils.DialogButtonModule("前往授权", new DialogUtils.DialogClickDelegate() {
                            @Override
                            public void click(DialogUtils.DialogButtonModule dialogButtonModule) {
                                if(never){
                                    XXPermissions.startPermissionActivity(ActivityManager.getInstance(), permissions);
                                } else {
                                    call(tel);
                                }
                            }
                        }), new DialogUtils.DialogButtonModule("取消", new DialogUtils.DialogClickDelegate() {
                            @Override
                            public void click(DialogUtils.DialogButtonModule dialogButtonModule) {
                                ToastUtil.showToast("无权限操作，请重新尝试");
                            }
                        }));
                    }
                });
    }
}
