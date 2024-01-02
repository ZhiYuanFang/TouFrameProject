package xyz.ttyz.tourfrxohc.utils;

import xyz.ttyz.toubasemvvm.utils.StringUtil;

/**
 * @author 投投
 * @date 2024/1/2
 * @email 343315792@qq.com
 */
public class TimeUtils {
    public static String getServerNeedDate(){
        return StringUtil.timeStamp2Date(System.currentTimeMillis(),"yyyy-MM-dd HH:mm:ss");
    }
}
