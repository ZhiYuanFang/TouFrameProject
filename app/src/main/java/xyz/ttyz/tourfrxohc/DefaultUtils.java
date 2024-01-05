package xyz.ttyz.tourfrxohc;

import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.locks.Lock;

import xyz.ttyz.mylibrary.protect.SharedPreferenceUtil;
import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.tourfrxohc.models.CarModel;
import xyz.ttyz.tourfrxohc.utils.LockUtil;
import xyz.ttyz.tourfrxohc.utils.Tools;

public class DefaultUtils {

    public static void setCookie(HashSet<String> cookies) {
        SharedPreferenceUtil.setShareString(ActivityManager.getInstance(), "cookie", cookies);
    }

    public static HashSet<String> getCookie() {
        return SharedPreferenceUtil.getShareStringSet(ActivityManager.getInstance(), "cookie");
    }

    public static void removeCookie() {
        SharedPreferenceUtil.clear(ActivityManager.getInstance(), "cookie");
    }

    //通过model 获取门地址,如果是存需要本地生成
    public static int getDoorAddress(boolean isPut, CarModel carModel){
        int doorAddress = 0;
        if(isPut){
            Log.i(TAG, "getDoorAddress: 是存钥匙");
            //获取没有钥匙的列表
            List<Integer> noneKeyList =  getNoneKeyList();
            //避免故障柜
            HashSet<String> errorList = getErrorKeyList();
            for (String error : errorList) {
                Log.i(TAG, "getDoorAddress: 故障柜列表包含 -> " + error);
                int errorDoor;
                try {
                    errorDoor = Integer.parseInt(error);
                    if(noneKeyList.contains(errorDoor)){
                        noneKeyList.remove(noneKeyList.indexOf(errorDoor));//不能简化，数字
                        Log.i(TAG, "getDoorAddress: 从没有钥匙的柜子列表中移除故障柜 -> " + errorDoor );
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            if(noneKeyList.size() > 0){
                doorAddress = noneKeyList.get(0);
            }
        } else {
            doorAddress = carModel.getDoorNumber();
        }
        Log.i(TAG, "getDoorAddress: 当前待开门柜号 -> " + doorAddress );
        carModel.setDoorNumber(doorAddress);
        return doorAddress;
    }

    //获取没有钥匙的列表
    public static List<Integer> getNoneKeyList () {
        String hexStr = getLocalHex();
        Log.i(TAG, "getNoneKeyList: " + hexStr);
        byte[] doorBytes = convertHex2DoorByteArr(hexStr);
        List<Integer> noneKeyList = new ArrayList<>();
        //遍历字节数组, 一个字节代表8位门
        for(int byPos = 0 ; byPos < doorBytes.length; byPos ++){
            byte cur = doorBytes[byPos];
            for (int i = 0; i < 8; i++){
                int pos = i + 8 * byPos;
                if((cur & 0x0001) == 0){
                    //没有钥匙, 记录当前柜门
                    noneKeyList.add(pos + 1);
                }

                cur = (byte) (cur >> 1);//看下一个锁的状态
            }

        }
        return noneKeyList;
    }

    //获取故障柜列表
    public static HashSet<String> getErrorKeyList(){
        return SharedPreferenceUtil.getShareStringSet(ActivityManager.getInstance(), "errorDoor");
    }

    //设置为故障柜
    public static void setErrorDoor(int errorDoor){
        HashSet<String> errorList=  getErrorKeyList();
        errorList.add(errorDoor + "");
        SharedPreferenceUtil.setShareString(ActivityManager.getInstance(), "errorDoor", errorList);
    }
    //移除故障柜
    public static void removeErrorDoor(int recoverDoor){
        HashSet<String> errorList=  getErrorKeyList();
        errorList.remove(recoverDoor + "");
        SharedPreferenceUtil.setShareString(ActivityManager.getInstance(), "errorDoor", errorList);
    }

    //清除故障柜
    public static void clearErrorDoor(){
        SharedPreferenceUtil.setShareString(ActivityManager.getInstance(), "errorDoor", new HashSet<>());
    }

    // 获取本地的状态hex字符串
    private static String getLocalHex (){
        String hexStr = SharedPreferenceUtil.getShareString(ActivityManager.getInstance(), "doorStatus");
        if(hexStr.isEmpty()){
            //当前没有存储,设定为全部为没钥匙状态
            StringBuilder stringBuilder = new StringBuilder();
            for(int i = 0; i < LockUtil.BankNumber * LockUtil.MAX_JI ; i ++){//根据所有寄存器数量初始化
                stringBuilder.append("00");
            }
            hexStr = stringBuilder.toString();
        }
        return hexStr;
    }
    // 重置所有钥匙柜为空柜
    public static void resetAllDoorWithNoneKey(){
        //当前没有存储,设定为全部为没钥匙状态
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < LockUtil.BankNumber * LockUtil.MAX_JI ; i ++){//根据所有寄存器数量初始化
            stringBuilder.append("00");
        }
        SharedPreferenceUtil.setShareString(ActivityManager.getInstance(), "doorStatus", stringBuilder.toString());
    }
    private static final String TAG = "DefaultUtils";
    //更新对应门 有/没有 钥匙状态
    public static void resetDoorWithKey(int doorNumber, boolean hasKey){
        Log.i(TAG, "resetDoorWithKey: 设置 " + doorNumber + (hasKey ? "有钥匙" : "无钥匙"));
        if(doorNumber <= 0){
            return;
        }

        String hexStr = getLocalHex();
        //hexStr: 00, 00, 00, 00, 00 ...
        byte[] curBytes = convertHex2DoorByteArr(hexStr);// [11111111][11111111][11111111]
        int pos;
        if(doorNumber % 8 == 0){
            pos = doorNumber / 8 - 1;
        } else {
            pos = doorNumber / 8;
        }
        if(pos < curBytes.length){
            byte nowByte = curBytes[pos];//[00000001]
            //将对应门的二进制修改
            int curIndex = doorNumber - pos * 8 - 1;//10-8-1 = 1-> 中间怎么运算？ -> [00000001] -> [00000011][11111101][11111111]
            // 根据hasKey 把curIndex 转 二进制，和原来的nowByte做运算， 然后将结果取代curBytes[pos], 然后转hex，并存入本地
            int lastNumber = 1;
            while (curIndex -- > 0){
                lastNumber *=2;//对应门转2进制表达
            }
            byte curByte = (byte)lastNumber ;//[000000010]
            byte endByte;
            if(hasKey){
                endByte = (byte)(nowByte | curByte);
            } else {
                endByte = (byte)(~curByte & nowByte);
            }
            curBytes[pos] = endByte;
            //转hex
            String hex = convertDoorByteArr2Hex(curBytes);
            SharedPreferenceUtil.setShareString(ActivityManager.getInstance(), "doorStatus", hex);
            Log.i(TAG, "resetDoorWithKey: 所有钥匙状态 -> " + hex);
        } else {
            Log.e(TAG, "resetDoorWithKey: 不该出现");
        }

    }

    //将门byte[] 转hexString
    public static String convertDoorByteArr2Hex(byte[] bytes){
        return Tools.ByteArrToHex(bytes, 0, bytes.length);
    }

    //将hexString:00, 00, 00, 00, 00 ... 转 btye[]
    public static byte[] convertHex2DoorByteArr(String hex){
        return Tools.hexStringToByte(hex);
    }

    public static void toLauncher(){
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.addCategory(Intent.CATEGORY_HOME);
        ActivityManager.getInstance().startActivity(home);
    }
}

