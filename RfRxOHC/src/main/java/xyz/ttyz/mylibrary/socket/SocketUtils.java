package xyz.ttyz.mylibrary.socket;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import xyz.ttyz.mylibrary.RfRxOHCUtil;
import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.mylibrary.socket.websocket.ResponseDelivery;
import xyz.ttyz.mylibrary.socket.websocket.WebSocketService;
import xyz.ttyz.mylibrary.socket.websocket.WebSocketSetting;
import xyz.ttyz.mylibrary.socket.websocket.WebSocketThread;

import static xyz.ttyz.mylibrary.socket.SocketManager.BROADCAST_ACTION;


public class SocketUtils {
    private static SocketBroadCastReceiver socketBroadCastReceiver;
    private static Intent intentWebSocketService;
    private static final String TAG = "SocketUtils";

    public interface SocketDelegate{
        void connectSuccess();

        long roomId();

        long userId();
    }


    /**
     * 开启长连接
     */
    public static void openMinaReceiver(final Application application,@NonNull final SocketDelegate socketDelegate) {
        closeMinaReceiver(application);
        if (null == socketBroadCastReceiver) {
            socketBroadCastReceiver = new SocketBroadCastReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(BROADCAST_ACTION);
        application.registerReceiver(socketBroadCastReceiver, filter);

        //启动 WebSocket 服务
        //先关后开
        if (null == intentWebSocketService) {
            //配置 WebSocket，必须在 WebSocket 服务启动前设置 ws://192.168.1.201:8080/tou3_war_exploded/devMessage/1/1
            String socketStr = RfRxOHCUtil.socketUrl + "/" + socketDelegate.userId() + "/" + socketDelegate.roomId();
            LogUtils.showWebSocketLog("socketUrl ==============> " + socketStr);
            WebSocketSetting.setConnectUrl(socketStr);
            Map<String, Object> header = RfRxOHCUtil.touRRCDelegate.socketInitHeader();
            LogUtils.showWebSocketLog("socket header ==============> " + header);
            WebSocketSetting.setHeaders(header);
            WebSocketSetting.setResponseProcessDelivery(new AppResponseDispatcher(application){
                @Override
                public void onConnected(ResponseDelivery delivery) {
                    super.onConnected(delivery);
                    socketDelegate.connectSuccess();
                }
            });
            WebSocketSetting.setReconnectWithNetworkChanged(true);
            WebSocketSetting.setSocketDelegate(new WebSocketThread.SocketDelegate() {
                @Override
                public void connectTimeOut() {
                    closeMinaReceiver(application);
                    RfRxOHCUtil.touRRCDelegate.socketConnectTimeOut();
                }

            });
            intentWebSocketService = new Intent(application, WebSocketService.class);
            application.startService(intentWebSocketService);

            SocketManager.initWebSocketService(application);
        }
    }

    public static void sendMessage(String str) {
        SocketManager.sendStr(str);
    }
    public static void sendMessage(byte[] bytes) {
        SocketManager.sendBytes(bytes);
    }
    /**
     * 关闭长连接
     */
    public static void closeMinaReceiver(Application application) {
        if (null == application) {
            return;
        }
        if (null != socketBroadCastReceiver) {
            application.unregisterReceiver(socketBroadCastReceiver);
            socketBroadCastReceiver = null;
        }
        if (null != intentWebSocketService) {
            application.stopService(intentWebSocketService);
            SocketManager.stopConnect();
            intentWebSocketService = null;
        }
    }
}
