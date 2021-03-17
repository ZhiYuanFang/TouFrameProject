package xyz.ttyz.toubasemvvm.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import androidx.core.app.ActivityCompat;

import java.util.Locale;

/**
 * @Author: Bill
 * @CreateDate: 2019-07-18 10:01
 * @Description:
 */
public class MobileInfoUtil {
    /**
     * 是否是平板
     *
     * @param context 上下文
     * @return 是平板则返回true，反之返回false
     */
    public static boolean isPad(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y); // 屏幕尺寸
        return screenInches >= 7.0;
    }

    public static String getUUID(Context context) {
        String uuid = null;

        try {
            uuid = getIMEI(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(uuid)) {
            uuid = getSerial();
            if (TextUtils.isEmpty(uuid)) {
                uuid = "Andorid_" + getDeviceBrand() + "_Not_GET_UUID";
            }
        }
        return uuid;
    }

    /**
     * 获取手机IMEI
     *
     * @param context
     * @return
     */
    @SuppressLint("HardwareIds")
    public static String getIMEI(Context context) {
        try {
            //实例化TelephonyManager对象
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            //获取IMEI号
            String imei = null;
            if (telephonyManager != null) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return "";
                }
                imei = telephonyManager.getDeviceId();
            }
            //在次做个验证，也不是什么时候都能获取到的啊
            if (imei == null) {
                imei = "";
            }
            return imei;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    /**
     * 获取手机IMSI
     */
    @SuppressLint("HardwareIds")
    public static String getIMSI(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            //获取IMSI号
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return "";
            }
            String imsi = null;
            if (telephonyManager != null) {
                imsi = telephonyManager.getSubscriberId();
            }
            if (null == imsi) {
                imsi = "";
            }
            return imsi;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取当前系统语言和地区
     *
     * @return 系统语言和地区 如：“zh_rCN”
     */
    public static String getSystemLanguageAndCountry() {
        return Locale.getDefault().toString();
    }

    /**
     * 获取当前系统语言
     *
     * @return 系统语言 如：“zh”
     */
    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取当前系统语言地区
     *
     * @return 系统语言地区 如：“CN”
     */
    public static String getSystemCountry() {
        return Locale.getDefault().getCountry();
    }

    /**
     * 获取系统语言列表
     *
     * @return 语言列表
     */
    public static Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    /**
     * 获取系统版本
     *
     * @return 系统版本
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取系统型号
     *
     * @return 系统型号
     */
    public static String getSystemModel() {
        return Build.MODEL;
    }

    /**
     * 获取当前设备的生成厂商
     *
     * @return 生成厂商
     */
    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    /**
     * Serial Number
     *
     * @return Serial Number
     */
    public static String getSerial() {
        return Build.SERIAL;
    }
}
