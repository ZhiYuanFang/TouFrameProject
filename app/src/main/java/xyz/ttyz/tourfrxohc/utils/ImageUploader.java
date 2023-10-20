package xyz.ttyz.tourfrxohc.utils;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.qiniu.android.storage.UploadManager;

import org.reactivestreams.Subscription;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import xyz.ttyz.mylibrary.method.RfRxOHCBaseModule;
import xyz.ttyz.mylibrary.method.RxOHCUtils;
import xyz.ttyz.tourfrxohc.BaseApplication;
import xyz.ttyz.tourfrxohc.http.BaseSubscriber;
import xyz.ttyz.tourfrxohc.models.TokenResponse;

/**
 * Created by sanji on 2016/11/8.
 * 上传图片
 */

public class ImageUploader {


    /* *************************** 上传图片路径模式 *************************** */

    /**
     * 上传图片，该方法为你做好了本地重复图片上传的校验
     *
     * @param context    上下文
     * @param originPath 图片的本地路径
     * @param callback   返回七牛的存储路径
     * @return Subscription不为空的情况下 需要被回收
     */
    public static void upload(Context context,@Nullable String originPath, @NonNull Callback callback) {
        final String path;
        try {
            path = checkFileName(context, originPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(path == null){
            System.out.println("图片路径不存在");
            return;
        } else {
            System.out.println("图片路径:" + path);
        }
        new RxOHCUtils<>(context).executeApi(BaseApplication.apiService.getToken(null), new BaseSubscriber(null) {

            @Override
            public void onNext(Object o) {
                System.out.println("测试：next");
                upload(path, "tokenResponse.mToken", callback);
            }

            @Override
            public void success(Object data) {

            }

            @Override
            public void onRfRxNext(RfRxOHCBaseModule rfRxOHCBaseModule) {

            }
        });
    }

    private static void upload(String path, String token,
                               @NonNull Callback callback) {
        String key = UUID.randomUUID().toString() + ".jpg";
        UploadManager uploadManager = new UploadManager();
        uploadManager.put(path, key, token,
                (key1, info, response) -> {
                    if (info.isOK()) {
                        String qiniuPath = Constans.QINIU_IMG_HEADER + key1;
                        callback.success(qiniuPath);

                    } else {
                        callback.fail(info.error);
                    }
                }, null);
    }

    /**
     * 七牛用的okhttp不支持中文fileName
     */
    public static String checkFileName(Context context, String inputPath) throws IOException {
        File inputFile = new File(inputPath);
        boolean isFileNameIllegal = false;
        String inputFileName = inputFile.getName();
        for (int i = 0, length = inputFileName.length(); i < length; i++) {
            char c = inputFileName.charAt(i);
            if ((c <= '\u001f' && c != '\t') || c >= '\u007f') {
                isFileNameIllegal = true;
                break;
            }
        }
        if (!isFileNameIllegal) {
            return inputPath;
        }
        String outputFileName;
        int index = inputFileName.lastIndexOf('.');
        if (index < 0) {
            throw new IOException("错误的文件名");
        }
        outputFileName = UUID.randomUUID() + inputFileName.substring(index);
        File outputFile = new File(context.getCacheDir(), outputFileName);
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(inputFile);
            outputStream = new FileOutputStream(outputFile);
            byte[] data = new byte[1024];
            while (inputStream.read(data) != -1) {
                outputStream.write(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("包含文法字符，请重命名文件");
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return outputFile.getAbsolutePath();
    }
    public interface Callback {
        void success(String qiniuPath);

        void fail(String msg);
    }
}
