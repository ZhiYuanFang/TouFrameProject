package xyz.ttyz.mylibrary.socket;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import xyz.ttyz.mylibrary.RfRxOHCUtil;

import static xyz.ttyz.mylibrary.socket.SocketManager.MESSAGE;


public class SocketBroadCastReceiver extends BroadcastReceiver {
    private static final String TAG = "SocketBroadCastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.showWebSocketLog("------------receiver----------- ");
        if (null != intent) {
            String message = intent.getStringExtra(MESSAGE);
            if (message == null) return;
            LogUtils.showWebSocketLog(message);
            RfRxOHCUtil.touRRCDelegate.socketReceived(message);
        }
    }
}
