package xyz.ttyz.mylibrary.method;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class HttpDefaultUtils {
    private static final String TAG = "HttpDefaultUtils";
    public static int HTTPCODE;

    private static List<BaseTouSubscriber> waitUiSubscriber = new ArrayList<>();
    //只要请求就加入
    public static void pushSubscriber(BaseTouSubscriber baseSubscriber){
        if(!waitUiSubscriber.contains(baseSubscriber)){
            waitUiSubscriber.add(baseSubscriber);
        }
    }
    //只要请求完成就推出
    public static void popSubscriber(BaseTouSubscriber BaseSubscriber){
        if(waitUiSubscriber.contains(BaseSubscriber)){
            waitUiSubscriber.remove(BaseSubscriber);
            Log.i(TAG, "popSubscriber: 剩余数据请求数量--------------->" + waitUiSubscriber.size());
        }
    }

    public static List<BaseTouSubscriber> getWaitUiSubscriber() {
        return waitUiSubscriber;
    }


    public static boolean isRequestIng;
}
