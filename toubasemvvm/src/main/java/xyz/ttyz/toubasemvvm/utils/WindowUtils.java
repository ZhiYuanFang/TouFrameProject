package xyz.ttyz.toubasemvvm.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.view.Window;

import xyz.ttyz.tou_example.ActivityManager;


public class WindowUtils {
    //状态栏+标题栏高度
    public static float topBarHeight(){
        Activity activity = ActivityManager.getInstance();
        return activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
    }

    //标题栏高度 中
    public static float titleBarHeight(){
        return topBarHeight() - statusBarHeight();
    }

    //状态栏高度 小
    public static float statusBarHeight(){
        Activity activity = ActivityManager.getInstance();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }
}
