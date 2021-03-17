package xyz.ttyz.mylibrary.protect;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by toutou on 2018/7/19.
 */

public class StringUtil {
    private static final String TAG = "StringUtil";
    /**
     * 获取string值,保证不是null且为字符串类型
     * */
    public static String safeString(Object obj){
        if(null == obj || obj.equals("null")){
            return "";
        }
        return String.valueOf(obj);
    }

    /**
     * 对象转换成string
     */
    public static String object2String(Object o) {
        String value = "";
        try {
            ByteArrayOutputStream navBaos = new ByteArrayOutputStream();
            ObjectOutputStream navOos = new ObjectOutputStream(navBaos);
            navOos.writeObject(o);
            int flags = Base64.DEFAULT;
            value = Base64.encodeToString(navBaos.toByteArray(), flags);
            navBaos.close();
            navOos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * string转换成对象
     */
    public static Object string2Object(String str) {
        if(str == null || str.isEmpty()) return null;
        Object o = null;
        try {
            int flags = Base64.DEFAULT;
            byte[] bytes = Base64.decode(str, flags);
            ByteArrayInputStream navBais = new ByteArrayInputStream(bytes);
            ObjectInputStream navOis = new ObjectInputStream(navBais);
            o = (Object) navOis.readObject();
            navBais.close();
            navOis.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "string2Object: " + e.getMessage());
        }
        return o;
    }


    /**
     * 通过关键字将字符串截取成数组
     * @param content
     * @param key
     *    list
     */
    public static List<String> getListFromStr(String content, String key) {
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        List<String> list = new ArrayList<>();
        String[] ary = content.split(key);
        for(String item: ary){
            list.add(item);
        }

        return list;
    }
}
