package xyz.ttyz.tourfrxohc.utils;

import android.util.Log;

import io.rong.imlib.RongIMClient;
import xyz.ttyz.toubasemvvm.utils.DialogUtils;
import xyz.ttyz.toubasemvvm.utils.ToastUtil;

public class RMUtils {
    private static final String TAG = "RMUtils";
    public static boolean connectSuccess = false;
    public static void connectRM(){
        Log.i(TAG, "connectRM: ");
        // 判断用户已经登录
        if(!UserUtils.isLogin()) return;
        //连接融云服务器
        RongIMClient.connect(UserUtils.getCurUserModel().getRmToken(), new RongIMClient.ConnectCallback() {
            @Override
            public void onSuccess(String userId) {
                connectSuccess = true;
                Log.i(TAG, "onSuccess: userId ==> " + userId);

            }

            @Override
            public void onError(RongIMClient.ConnectionErrorCode connectionErrorCode) {
                DialogUtils.showSingleDialog("连接失败", new DialogUtils.DialogButtonModule("确定"));
            }

            @Override
            public void onDatabaseOpened(RongIMClient.DatabaseOpenStatus databaseOpenStatus) {
                Log.i(TAG, "onDatabaseOpened: " + databaseOpenStatus.getValue());
            }
        });
    }
}
