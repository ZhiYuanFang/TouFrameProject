package xyz.ttyz.mylibrary.socket;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Arrays;

import xyz.ttyz.mylibrary.RfRxOHCUtil;

import static xyz.ttyz.mylibrary.socket.SocketManager.MESSAGE;
import static xyz.ttyz.mylibrary.socket.SocketManager.MESSAGE_bytes;


public class SocketBroadCastReceiver extends BroadcastReceiver {
    private static final String TAG = "SocketBroadCastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null != intent) {
            String message = intent.getStringExtra(MESSAGE);
            if (message == null) {
                byte[] bytes = intent.getByteArrayExtra(MESSAGE_bytes);
                if(bytes != null){
                    RfRxOHCUtil.touRRCDelegate.socketReceived(bytes);
                }
            } else {
                RfRxOHCUtil.touRRCDelegate.socketReceived(message);
            }
        }
    }
}
