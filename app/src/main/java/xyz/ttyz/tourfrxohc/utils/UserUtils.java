package xyz.ttyz.tourfrxohc.utils;

import com.google.gson.Gson;

import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.mylibrary.protect.SharedPreferenceUtil;
import xyz.ttyz.tourfrxohc.MainActivity;
import xyz.ttyz.tourfrxohc.models.UserModel;

public class UserUtils {
    public static boolean isLogin(){
        return getCurUserModel() != null;
    }

    private static UserModel curUserModel;//当前用户信息

    //用户登录之后调用，保存用户信息，以及其它登录后操作
    public static void login(UserModel userModel){
        SharedPreferenceUtil.setShareString(ActivityManager.getInstance(), "user", new Gson().toJson(userModel));
        MainActivity.show();
    }

    public static UserModel getCurUserModel(){
        if(curUserModel == null){
            curUserModel = new Gson().fromJson(SharedPreferenceUtil.getShareString(ActivityManager.getInstance(), "user"), UserModel.class);
        }
        return curUserModel;
    }
}
