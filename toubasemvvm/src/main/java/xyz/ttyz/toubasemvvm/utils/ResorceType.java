package xyz.ttyz.toubasemvvm.utils;

import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;

public enum ResorceType {
    IMAGE(1),
    AUDIO(2),
    VIDEO(3);

    int type;
    ResorceType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static ResorceType valueOf(int value){
        switch (value){
            case 1:return IMAGE;
            case 2:return AUDIO;
            case 3:return VIDEO;
            default:return IMAGE;
        }
    }

    public static ResorceType valueOfFilePath(String filePath){
        switch (PictureMimeType.getMimeType(filePath)){
            case PictureConfig.TYPE_IMAGE:
                return IMAGE;
            case PictureConfig.TYPE_VIDEO:
                return VIDEO;
            case PictureConfig.TYPE_AUDIO:
                return AUDIO;
            default:return IMAGE;
        }
    }
}
