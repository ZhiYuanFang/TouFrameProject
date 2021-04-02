package xyz.ttyz.mylibrary.socket;

import android.util.Log;

public class LogUtils {
    public static void showWebSocketLog(String str){
        if (null == str) {
            str = "null";
        }
        Log.i("WebSocket", str);
    }
}
