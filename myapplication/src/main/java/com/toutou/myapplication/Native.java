package com.toutou.myapplication;

import android.content.Context;

public class Native {

    static {
        System.loadLibrary("native-lib");
    }
    public static native String stringFromJNI(Context context);
}
