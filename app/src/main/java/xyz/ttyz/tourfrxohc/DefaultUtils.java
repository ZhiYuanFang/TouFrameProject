package xyz.ttyz.tourfrxohc;

import android.app.Application;

import com.google.gson.Gson;

import org.json.JSONObject;

import xyz.ttyz.mylibrary.protect.SharedPreferenceUtil;
import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.tourfrxohc.models.UserModel;

public class DefaultUtils {
    public static void setUser(UserModel user) {
        if (user == null) {
            SharedPreferenceUtil.setShareString(ActivityManager.getInstance(), "user", null);
        } else
            SharedPreferenceUtil.setShareString(ActivityManager.getInstance(), "user", new Gson().toJson(user));

    }

    public static UserModel getUser() {
        String userStr = SharedPreferenceUtil.getShareString(ActivityManager.getInstance(), "user");
        return new Gson().fromJson(userStr, UserModel.class);
    }

}
