package xyz.ttyz.mylibrary.method;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import xyz.ttyz.mylibrary.protect.EncodeUtils;
import xyz.ttyz.mylibrary.protect.JsonEncryptUtils;

/**
 * Created by tou on 2019/1/14.
 */

public class RetrofitUtils {
    private static final String TAG = "RetrofitUtils";
    public static RequestBody getNormalBody(Map hashMap){
        String str = new Gson().toJson(hashMap);
        Log.i(TAG, "request中传递的json数据：" + str);
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), str);
    }
    public static RequestBody getRSARequestBody(Map hashMap) {
        //加密
        String str = new Gson().toJson(hashMap);
        Log.i(TAG, "request中传递的json数据：" + str);
        String postBody = JsonEncryptUtils.rsaEncrypt(str, JsonEncryptUtils.RSAKey);
        Log.i(TAG, "转化后的数据：" + postBody);
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), postBody);
    }
    public static RequestBody getAESRequestBody(Map hashMap) {
        //加密
        String aesRandom = EncodeUtils.AESKey;
        Log.i(TAG, "AES  密钥：" + aesRandom);
        String str = new Gson().toJson(hashMap)/*"{code:123}"*/;
        Log.i(TAG, "AES  request中传递的json数据：" + str);
        String postBody = JsonEncryptUtils.aesEncrypt(str, aesRandom);
        Log.i(TAG, "AES  转化后的数据：" + postBody);
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), postBody);
    }

    public static MultipartBody.Part getFileBody(Activity activity, String key, File file){
        File currentFile = FileUtil.compress(activity, file);
        if(currentFile == null){
            currentFile = file;
        }
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("application/otcet-stream"), currentFile);

        return MultipartBody.Part.createFormData(key, currentFile.getName(), requestFile);
    }

    public static RequestBody getMultiForString(String str){
        return RequestBody.create(
                MediaType.parse("multipart/form-data"), str);
    }
}
