package xyz.ttyz.tourfrxohc.utils;

import android.util.Log;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.mylibrary.protect.SharedPreferenceUtil;
import xyz.ttyz.toubasemvvm.utils.StringUtil;

/**
 * @author 投投
 * @date 2023/12/26
 * @email 343315792@qq.com
 */
public class PwdUtils {
    private static final String TAG = "PwdUtils";
    //解密前的
    public static String getSuperPwd(){
        String waitStr = SharedPreferenceUtil.getShareString(ActivityManager.getInstance(), Constans.SuperOpenDoorPwdKey);
        Log.i(TAG, "getSuperPwd: " + waitStr);
        return EncrUtil.decrypt(waitStr);
    }
    public static String generateRandomPwd(){
        //生成密码
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
    //获取仓库码,钥匙柜ID
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

    //region 独立门密码
    public static String getDoorPwd(int doorNumber){
        //生成密码
        Random random = new Random();
        int randomNumber = random.nextInt(90000) + 10000;
        String pwd = randomNumber + "" + doorNumber;
        //将密码与门对应并存入本地
        putPwdLocal(doorNumber, pwd);
        return pwd;
    }
    private static final String pwdSplit = "-";
    //获取本地记录：密码-门
    public static HashMap<String, Integer> getPwdArray () {
        //"1235-1"
        HashSet<String> hashSet = SharedPreferenceUtil.getShareStringSet(ActivityManager.getInstance(), "pwdArray");
        HashMap<String, Integer> pwdMap = new HashMap<>();
        for (String localPwd : hashSet) {
            String[] strs = localPwd.split(pwdSplit);
            pwdMap.put(strs[0], Integer.valueOf(strs[1]));
        }
        return pwdMap;
    }

    //将门密码存入本地
    public static void putPwdLocal(int doorNumber, String pwd){
        HashMap<String, Integer> localMap = getPwdArray();
        localMap.put(pwd, doorNumber);
        HashSet<String> hashSet = new HashSet<>();
        for (String localPwd :localMap.keySet()) {
            hashSet.add(localPwd + pwdSplit + doorNumber);
        }
        SharedPreferenceUtil.setShareString(ActivityManager.getInstance(), "pwdArray", hashSet);
    }


    public static int getDoor(String pwd){
        HashMap<String, Integer> localDoorArr = getPwdArray();
        if(localDoorArr.containsKey(pwd)){
            return localDoorArr.get(pwd);
        } else return 0;
    }


    //endregion
}
