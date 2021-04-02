package xyz.ttyz.mylibrary.socket;

import android.app.Application;
import android.content.Intent;

import com.google.gson.Gson;

import java.util.HashMap;

import xyz.ttyz.mylibrary.socket.websocket.ErrorResponse;
import xyz.ttyz.mylibrary.socket.websocket.IResponseDispatcher;
import xyz.ttyz.mylibrary.socket.websocket.Response;
import xyz.ttyz.mylibrary.socket.websocket.ResponseDelivery;

import static xyz.ttyz.mylibrary.socket.SocketManager.BROADCAST_ACTION;
import static xyz.ttyz.mylibrary.socket.SocketManager.MESSAGE;


/**
 * Created by ZhangKe on 2018/6/27.
 */
public class AppResponseDispatcher implements IResponseDispatcher {

    private Application application;

    AppResponseDispatcher(Application application) {
        this.application = application;
    }

    @Override
    public void onConnected(ResponseDelivery delivery) {
        delivery.onConnected();
//        SocketUtils.sendMessage();
    }

    @Override
    public void onConnectError(Throwable cause, ResponseDelivery delivery) {
        delivery.onConnectError(cause);
    }

    @Override
    public void onDisconnected(ResponseDelivery delivery) {
        delivery.onDisconnected();
    }

    /**
     * 统一处理响应的数据
     */
    @Override
    public void onMessageResponse(Response message, ResponseDelivery delivery) {
        String str = message.getResponseText();
        if (application != null && str != null) {
            LogUtils.showWebSocketLog("接收到服务器端消息：" + str);

            Intent intent = new Intent(BROADCAST_ACTION);

            intent.putExtra(MESSAGE, str);

            application.sendBroadcast(intent);

        }
    }

    /**
     * 统一处理错误信息，
     * 界面上可使用 ErrorResponse#getDescription() 来当做提示语
     */
    @Override
    public void onSendMessageError(ErrorResponse error, ResponseDelivery delivery) {
        switch (error.getErrorCode()) {
            case 1:
                error.setDescription("网络错误");
                break;
            case 2:
                error.setDescription("网络错误");
                break;
            case 3:
                error.setDescription("网络错误");
                break;
            case 11:
                error.setDescription("数据格式异常");
                LogUtils.showWebSocketLog("数据格式异常" + error.getCause());
                break;
        }
        delivery.onSendMessageError(error);
        String str = error.getResponseText();

        if (application != null && str != null) {
            LogUtils.showWebSocketLog("统一处理错误信息：" + str);

            Intent intent = new Intent(BROADCAST_ACTION);

            intent.putExtra(MESSAGE, str);

            application.sendBroadcast(intent);

        }
    }
}
