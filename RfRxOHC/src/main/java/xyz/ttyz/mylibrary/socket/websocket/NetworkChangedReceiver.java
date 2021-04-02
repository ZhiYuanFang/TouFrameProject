package xyz.ttyz.mylibrary.socket.websocket;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.annotation.RequiresPermission;

/**
 * 监听网络变化广播，网络变化时自动重连
 * Created by ZhangKe on 2018/7/2.
 */
public class NetworkChangedReceiver extends BroadcastReceiver {

    private static final String LOGTAG = "NetworkChangedReceiver";

    private WebSocketService socketService;

    public NetworkChangedReceiver() {
    }

    int netType;
    public NetworkChangedReceiver(int netType, WebSocketService socketService) {
        this.socketService = socketService;
       this.netType = netType;
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            ConnectivityManager manager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager == null) return;
            NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
            if (activeNetwork != null) {
                if (activeNetwork.isConnected()) {
//                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
//                        Log.i(LOGTAG, "网络连接发生变化，当前WiFi连接可用，正在尝试重连。");
//                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
//                    }
                    if (socketService != null && netType != activeNetwork.getType()) {
                        Log.i(LOGTAG, "网络连接发生变化，正在尝试重连。");
                        socketService.reconnect();
                    }
                } else {
                    Log.i(LOGTAG, "当前没有可用网络");
                }
            }
        }
    }
}
