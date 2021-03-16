package xyz.ttyz.toubasemvvm.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.NonNull;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class VideoUtils {
    public interface VideoDelegate {
        void loadEnd(boolean loadSuccess);
    }

    public static void startDownLoad(final Context context, String url, @NonNull final VideoDelegate videoDelegate) {
        String fileName = System.currentTimeMillis() + ".mp4";
        final File file = new File(FileUtil.getNewFile(context, "", Environment.DIRECTORY_MOVIES), fileName);
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ProgressUtil.missProgress();
                videoDelegate.loadEnd(false);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.MIME_TYPE, "video/mp4");
                    values.put(MediaStore.Images.ImageColumns.TITLE, file.getName());
                    values.put(MediaStore.Images.Media.DESCRIPTION, file.getName());
                    values.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, file.getName());
                    //兼容Android Q和以下版本
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        //android Q中不再使用DATA字段，而用RELATIVE_PATH代替
                        //RELATIVE_PATH是相对路径不是绝对路径
                        //DCIM是系统文件夹，关于系统文件夹可以到系统自带的文件管理器中查看，不可以写没存在的名字
                        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Movies/");

                    } else {
                        values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath() + file.getName());
                    }
                    Uri insertUri = context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
                    values.clear();
                    InputStream is = null;
                    if (response.body() != null) {
                        is = response.body().byteStream();
                    }
                    if (is == null) {
                        videoDelegate.loadEnd(false);
                        return;
                    }
                    byte[] buf = new byte[1024 * 20];
                    int len = 0;
                    long total = response.body().contentLength();
                    OutputStream fos;
                    if (insertUri != null) {
                        fos = context.getContentResolver().openOutputStream(insertUri);
                    } else {
                        fos = new FileOutputStream(file);
                    }
                    if (fos != null) {
                        ProgressUtil.missProgress();
                        ProgressUtil.showProgress(context, "下载视频", 0, 100);
                        try {
                            long sum = 0;
                            while ((len = is.read(buf)) != -1) {
                                fos.write(buf, 0, len);
                                sum += len;
                                int progress = (int) (sum * 1.0f / total * 100);
                                System.out.println("下载进度 : " + progress);
                                ProgressUtil.updateProgress(progress);
                            }
                            videoDelegate.loadEnd(true);
                            ProgressUtil.missProgress();
                        } catch (Exception e) {
                            videoDelegate.loadEnd(false);
                            ProgressUtil.missProgress();
                            e.printStackTrace();
                        } finally {
                            ProgressUtil.missProgress();
                            try {
                                is.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            System.out.println("下载完成");
                            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, insertUri));

                            file.deleteOnExit();
                        }
                    }
                } else {
                    InputStream is = null;
                    if (response.body() != null) {
                        is = response.body().byteStream();
                    }
                    if (is == null) {
                        videoDelegate.loadEnd(false);
                        return;
                    }
                    byte[] buf = new byte[1024 * 20];
                    int len = 0;
                    long total = response.body().contentLength();
                    OutputStream fos;
                    fos = new BufferedOutputStream(new FileOutputStream(file));
                    if (fos != null) {
                        ProgressUtil.missProgress();
                        ProgressUtil.showProgress(context, "下载视频", 0, 100);
                        try {
                            long sum = 0;
                            while ((len = is.read(buf)) != -1) {
                                fos.write(buf, 0, len);
                                sum += len;
                                int progress = (int) (sum * 1.0f / total * 100);
                                System.out.println("下载进度 : " + progress);
                                ProgressUtil.updateProgress(progress);
                            }
                            videoDelegate.loadEnd(true);
                            ProgressUtil.missProgress();
                        } catch (Exception e) {
                            videoDelegate.loadEnd(false);
                            ProgressUtil.missProgress();
                            e.printStackTrace();
                        } finally {
                            ProgressUtil.missProgress();
                            try {
                                is.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            System.out.println("下载完成");
//                            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, insertUri));
                            Uri uri = Uri.parse("file://" + file.getAbsolutePath());
                            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                            intent.setData(uri);
                            context.sendBroadcast(intent);
                            file.deleteOnExit();
                        }
                    }
                }
            }
        });
    }
}
