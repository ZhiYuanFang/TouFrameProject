package xyz.ttyz.tourfrxohc.utils;

import java.util.Random;

import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.mylibrary.protect.SharedPreferenceUtil;

/**
 * @author 投投
 * @date 2023/12/26
 * @email 343315792@qq.com
 */
public class PwdUtils {
    //解密前的
    public static String getSuperPwd(){
        String waitStr = SharedPreferenceUtil.getShareString(ActivityManager.getInstance(), Constans.SuperOpenDoorPwdKey);
        return EncrUtil.decrypt(waitStr);
    }
    public static String generateRandomPwd(){
        //生成超级密码
        Random random = new Random();
        int randomNumber = random.nextInt(900000) + 100000;
        return randomNumber + "";
    }
    // 加密后的
    public static void setSuperPwd(String superPwd){
        SharedPreferenceUtil.setShareString(ActivityManager.getInstance(), Constans.SuperOpenDoorPwdKey, superPwd);
    }
    public static void clearSuperPwd(){
        setSuperPwd("");
    }
    //获取仓库码
    public static String getWareHouseCode(){
        return SharedPreferenceUtil.getShareString(ActivityManager.getInstance(), Constans.BindWareHouseCodeKey);
    }
    //获取仓库码
    public static void setWareHouseCode(String code){
        SharedPreferenceUtil.setShareString(ActivityManager.getInstance(), Constans.BindWareHouseCodeKey, code);
    }
    //清空仓库码
    public static void clearWareHouseCode(){
        SharedPreferenceUtil.setShareString(ActivityManager.getInstance(), Constans.BindWareHouseCodeKey, "");
    }

}
