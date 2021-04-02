package xyz.ttyz.mylibrary.socket.websocket;

import java.util.Map;

/**
 * WebSocket 使用配置
 * Created by ZhangKe on 2018/6/26.
 */
public class WebSocketSetting {

    private static String connectUrl;
    private static Map headers;
    private static SocketListener socketListener;
    private static WebSocketThread.SocketDelegate socketDelegate;
    private static IResponseDispatcher responseProcessDelivery;
    private static boolean reconnectWithNetworkChanged;

    public static WebSocketThread.SocketDelegate getSocketDelegate() {
        return socketDelegate;
    }

    public static void setSocketDelegate(WebSocketThread.SocketDelegate socketDelegate) {
        WebSocketSetting.socketDelegate = socketDelegate;
    }

    /**
     * 获取 WebSocket 链接地址
     */
    public static String getConnectUrl() {
        return connectUrl;
    }

    public static Map getHeaders() {
        return headers;
    }

    public static SocketListener getSocketListener() {
        return socketListener;
    }

    public static void setSocketListener(SocketListener socketListener) {
        WebSocketSetting.socketListener = socketListener;
    }

    public static void setHeaders(Map headers) {
        WebSocketSetting.headers = headers;
    }

    /**
     * 设置 WebSocket 链接地址，第一次设置有效，
     * 必须在启动 WebSocket 线程之前设置
     */
    public static void setConnectUrl(String connectUrl) {
        WebSocketSetting.connectUrl = connectUrl;
    }

    /**
     * 获取当前已设置的消息分发器
     */
    public static IResponseDispatcher getResponseProcessDelivery() {
        if (responseProcessDelivery == null) {
            responseProcessDelivery = new DefaultResponseDispatcher();
        }
        return responseProcessDelivery;
    }

    /**
     * 设置消息分发器
     */
    public static void setResponseProcessDelivery(IResponseDispatcher responseProcessDelivery) {
        WebSocketSetting.responseProcessDelivery = responseProcessDelivery;
    }

    public static boolean isReconnectWithNetworkChanged() {
        return reconnectWithNetworkChanged;
    }

    /**
     * 设置网络连接变化后是否自动重连。</br>
     * 如果设置 true 则需要注册广播：{@link NetworkChangedReceiver}，</br>
     * 并添加 ACCESS_NETWORK_STATE 权限。
     */
    public static void setReconnectWithNetworkChanged(boolean reconnectWithNetworkChanged) {
        WebSocketSetting.reconnectWithNetworkChanged = reconnectWithNetworkChanged;
    }
}
