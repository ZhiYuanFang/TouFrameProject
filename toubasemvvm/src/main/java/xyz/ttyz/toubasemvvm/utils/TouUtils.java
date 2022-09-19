package xyz.ttyz.toubasemvvm.utils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

//import com.luck.picture.lib.PictureSelector;
//import com.luck.picture.lib.entity.LocalMedia;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import xyz.ttyz.tou_example.ActivityManager;

import static android.content.Context.SENSOR_SERVICE;

public class TouUtils {
    public static List<String> stringList = new ArrayList<>();//投屏设备
    public static AppCompatActivity scanForActivity(Context cont) {
        if (cont == null)
            return null;
        else if (cont instanceof AppCompatActivity)
            return (AppCompatActivity) cont;
        else if (cont instanceof ContextWrapper)
            return scanForActivity(((ContextWrapper) cont).getBaseContext());

        return null;
    }


    /**
     * 判断蓝牙是否有效来判断是否为模拟器
     *
     * @return true 为模拟器
     */
//    public static boolean isMonitor() {
//        Activity activity = ActivityManager.getInstance();
//        boolean notHsaBluetooth;
//        BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
//        if (ba == null) {
//            notHsaBluetooth = true;
//        } else {
//// 如果有蓝牙不一定是有效的。获取蓝牙名称，若为null 则默认为模拟器
//            String name = ba.getName();
//            notHsaBluetooth = TextUtils.isEmpty(name);
//        }
//
//        boolean notHasLightSensor;
//        SensorManager sensorManager = (SensorManager) activity.getSystemService(SENSOR_SERVICE);
//        Sensor sensor8 = null; //光
//        if (sensorManager != null) {
//            sensor8 = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
//        }
//        notHasLightSensor = null == sensor8;
//
//        return notHsaBluetooth || notHasLightSensor;
//    }

    //获取网络视频第一帧
    public static Bitmap getNetVideoBitmap(String videoUrl) {
        if (TextUtils.isEmpty(videoUrl)) {
            return null;
        }
        Bitmap bitmap = null;

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //根据url获取缩略图
            retriever.setDataSource(videoUrl, new HashMap());
            //获得第一帧图片
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return bitmap;
    }

    /**
     * 截取scrollview的屏幕
     **/
    public static Bitmap getScrollViewBitmap(ScrollView scrollView) {
        int h = 0;
        Bitmap bitmap;
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
        }
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
                Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }

    public static int getVideoDuration(String url){
        Uri uri = Uri.parse(url);
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(ActivityManager.getInstance(), uri);
            mediaPlayer.prepare();
            return mediaPlayer.getDuration()/1000;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String jumpString;//通知点击跳转的链接

    public static boolean equalsList(List list1, List list2) throws Exception{
        ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
        ObjectOutputStream oos1 = new ObjectOutputStream(bos1);
        oos1.writeObject(list1);
        byte[] bytes1 = bos1.toByteArray();

        ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
        ObjectOutputStream oos2 = new ObjectOutputStream(bos2);
        oos2.writeObject(list2);
        byte[] bytes2 = bos2.toByteArray();

        return Arrays.equals(bytes1, bytes2);
    }

}
