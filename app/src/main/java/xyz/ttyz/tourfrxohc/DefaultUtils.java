package xyz.ttyz.tourfrxohc;

import xyz.ttyz.toubasemvvm.utils.TouUtils;

public class DefaultUtils {
    public static String token;

    public static String getIp () {
        if( BuildConfig.BUILD_TYPE.equals("release")){
            return "https://eevee.maihaoche.net/api";
        } else {
            return "https://emerald-u.maihaoche.net";
        }
    }
}
