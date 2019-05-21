package xyz.ttyz.mylibrary.method;

import android.content.Context;
import android.text.TextUtils;


import java.util.ArrayList;
import java.util.List;

import xyz.ttyz.mylibrary.protect.SharedPreferenceUtil;
import xyz.ttyz.mylibrary.protect.StringUtil;

public class LocalSaveConfig {
    private static final String CacheKey = "cacheKey";
    public static void addSaveKey(Context c, String saveKey) {
        String localSaveKey = SharedPreferenceUtil.getShareString(c, CacheKey);
        if (!localSaveKey.contains(saveKey)) {
            List<String> localSaveList = getSaveKeyList(c);
            if (!localSaveList.contains(saveKey)) {
                localSaveList.add(saveKey);
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < localSaveList.size(); i++) {
                    stringBuilder.append(localSaveList.get(i));
                    if (i < localSaveList.size() - 1) {
                        stringBuilder.append(",");
                    }
                }
                SharedPreferenceUtil.setShareString(c, CacheKey, stringBuilder.toString());
            }
        }
    }

    public static List<String> getSaveKeyList(Context c){
        String localSaveKey = SharedPreferenceUtil.getShareString(c, CacheKey);
        List<String> localSaveList;
        if (TextUtils.isEmpty(localSaveKey)) {
            localSaveList = new ArrayList<>();
        } else {
            localSaveList = StringUtil.getListFromStr(localSaveKey, ",");
            if (localSaveList == null) {
                localSaveList = new ArrayList<>();
            }
        }
        return localSaveList;
    }

    public static void clearSave(Context c){
        List<String> localSaveList = getSaveKeyList(c);
        for(String saveKey : localSaveList){
            SharedPreferenceUtil.setShareString(c, saveKey, "");
        }
    }

    public static long getLocalShareMemory(Context c) {
        List<String> localSaveList = getSaveKeyList(c);
        List<Integer> integerList = new ArrayList<>();
        for(String str : localSaveList){
            integerList.add(getStringMemory(c, str));
        }
        long memoryInteger = 0;
        for(Integer integer : integerList){
            memoryInteger += integer;
        }
        return memoryInteger;
    }

    private static int getStringMemory(Context c, String key){
        if(TextUtils.isEmpty(key)){
            return 0;
        }
        String value = SharedPreferenceUtil.getShareString(c, key);
        return value.getBytes().length;
    }
}
