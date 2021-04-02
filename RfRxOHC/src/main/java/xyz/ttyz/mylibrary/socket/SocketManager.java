package xyz.ttyz.mylibrary.socket;

import android.annotation.SuppressLint;
import android.app.Application;

import xyz.ttyz.mylibrary.socket.websocket.ErrorResponse;
import xyz.ttyz.mylibrary.socket.websocket.IWebSocketPage;
import xyz.ttyz.mylibrary.socket.websocket.Response;
import xyz.ttyz.mylibrary.socket.websocket.WebSocketServiceConnectManager;


public class SocketManager {
    static final String BROADCAST_ACTION = "SocketReceiver";

    static final String MESSAGE = "message";

    @SuppressLint("StaticFieldLeak")
    private static WebSocketServiceConnectManager webSocketServiceConnectManager;

    public static void initWebSocketService(Application application){
        if(null == webSocketServiceConnectManager){
            webSocketServiceConnectManager = new WebSocketServiceConnectManager(application, new IWebSocketPage() {
                @Override
                public void onServiceBindSuccess() {
                    LogUtils.showWebSocketLog("ServiceBindSuccess");
                }

                @Override
                public void sendText(String text) {
                    webSocketServiceConnectManager.sendText(text);
                }

                @Override
                public void reconnect() {
                    LogUtils.showWebSocketLog("重新链接");
                }

                @Override
                public void onConnected() {
                }

                @Override
                public void onConnectError(Throwable cause) {
                }

                @Override
                public void onDisconnected() {
                }

                @Override
                public void onMessageResponse(Response message) {
                    LogUtils.showWebSocketLog(message.getResponseText());
                }

                @Override
                public void onSendMessageError(ErrorResponse error) {
                    LogUtils.showWebSocketLog("Error : " + error.getCause());
                    if(lastRequestStr == null || !lastRequestStr.equals(error.getRequestText())){
                        lastRequestStr = error.getRequestText();
                        sendStr(error.getRequestText());
                    }
                }
            });
        }
        webSocketServiceConnectManager.onCreate();
    }

    static String lastRequestStr;

    public static void sendStr(String str){
        if(null != webSocketServiceConnectManager){
            webSocketServiceConnectManager.sendText(str);
        }
    }

    public static void stopConnect(){
        if(null != webSocketServiceConnectManager){
            webSocketServiceConnectManager.onDestroy();
        }
    }
}
