package xyz.ttyz.tourfrxohc.utils;

import android.util.Log;

import io.rong.imlib.RongIMClient;
import xyz.ttyz.toubasemvvm.utils.DialogUtils;
import xyz.ttyz.toubasemvvm.utils.ToastUtil;

public class RMUtils {
    private static final String TAG = "RMUtils";
    public static boolean connectSuccess = false;
    public static void connectRM(){
        //todo 判断用户已经登录
        //todo 根据本地用户信息，请求接口获取token
        String token = "PrQGvghn9GJGnXy0a9ZHlHO5NO2Ezznz@ev08.cn.rongnav.com;ev08.cn.rongcfg.com";
        //连接融云服务器
        RongIMClient.connect(token, new RongIMClient.ConnectCallback() {
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

            }
        });
    }
}
