package xyz.ttyz.tourfrxohc;

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
    public static String token = "默认";

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
            //获取没有钥匙的列表
            List<Integer> noneKeyList =  getNoneKeyList();
            if(noneKeyList.size() > 0){
                doorAddress = noneKeyList.get(0);
            }
        } else {
            doorAddress = carModel.getDoorNumber();//todo 根据数据推断门地址, 如果推断不出来，就返回0
        }
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
    private static final String TAG = "DefaultUtils";
    //更新对应门 有/没有 钥匙状态
    public static void resetDoorWithKey(int doorNumber, boolean hasKey){
        if(doorNumber <= 0){
            return;
        }
        String hexStr = getLocalHex();
        //hexStr: 00, 00, 00, 00, 00 ...
        byte[] curBytes = convertHex2DoorByteArr(hexStr);// [11111111][11111111][11111111]
        int pos = doorNumber / 9;
        if(pos < curBytes.length){
            byte nowByte = curBytes[pos];//[11111111]
            //将对应门的二进制修改
            int curIndex = doorNumber - pos * 8 - 1;//10-8-1 = 1-> 中间怎么运算？ -> [11111101] -> [11111111][11111101][11111111]
            // 根据hasKey 把curIndex 转 二进制，和原来的nowByte做运算， 然后将结果取代curBytes[pos], 然后转hex，并存入本地
            int lastNumber = 1;
            while (curIndex -- > 0){
                lastNumber *=lastNumber;
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
}

