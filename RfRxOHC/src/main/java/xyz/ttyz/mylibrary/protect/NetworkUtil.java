package xyz.ttyz.mylibrary.protect;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkUtil {
    /**
     * 判断是否连接互联网
     * */
    public static boolean isNetWorkConnected(Application c){
        if (null == c){
            return false;
        }
        ConnectivityManager con=(ConnectivityManager)c.getSystemService(Activity.CONNECTIVITY_SERVICE);
        if (con != null) {
            boolean wifi=con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
            boolean internet=con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
            return wifi || internet;
        }
        return false;
    }
}
