package xyz.ttyz.toubasemvvm.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.XXPermissions;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.List;

import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.toubasemvvm.R;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Environment.DIRECTORY_PICTURES;

public class FileUtil {

    public static File getNewFile(Context application, String filePath) {
        return getNewFile(application, filePath, DIRECTORY_PICTURES);
    }
    public static File getNewFile(Context application, String filePath, String type) {
        if (filePath == null) {
            return null;
        }
        if (ContextCompat.checkSelfPermission(application, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(application, application.getString(R.string.no_save_permission), Toast.LENGTH_SHORT).show();
            return null;
        }
        filePath = getDiskCacheDir(application, type) + "/" + filePath;
        File file = new File(filePath);
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                if (!file.getParentFile().mkdirs()) {
                    Toast.makeText(application, application.getString(R.string.create_folder_false), Toast.LENGTH_SHORT).show();
                    return null;
                }
            }
        }

        return file;
    }
    /**
     * 检查文件是否存在
     */
    public static String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }

    /**
     * 获取系统文件目录
     */
    public static String getDiskCacheDir(Context context) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            if (Build.BRAND.equalsIgnoreCase("Xiaomi")) { // 小米手机
                cachePath = context.getExternalFilesDir(DIRECTORY_PICTURES).getPath() + "/DCIM/Camera/";
            }  else {  // Meizu 、Oppo
                cachePath = context.getExternalFilesDir(DIRECTORY_PICTURES).getPath() + "/DCIM/";
            }
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }
    /**
     * 获取系统文件目录
     */
    public static String getDiskCacheDir(Context context, String type) {
        String cachePath = "";
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
//            File file = context.getExternalFilesDir(type);//会保存失败
            File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            if(file != null)
            cachePath = file.getAbsolutePath();
        } else {
            cachePath = context.getCacheDir().getAbsolutePath();
        }
        return cachePath;
    }
    public static Uri getUriFromFile(Activity activity, File file) {
        if (file == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {

            return FileProvider.getUriForFile(activity,
                    "com.x16.coe.fsc.prod.fileProvider",
                    file);
        } else {
            return Uri.fromFile(file);
        }
    }

    public static File getRealFile(Activity activity, Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String imagePath = null;
        if (scheme == null)
            imagePath = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            imagePath = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = activity.getContentResolver().query(uri,
                    new String[]{MediaStore.Images.ImageColumns.DATA},
                    null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        imagePath = cursor.getString(index);
                        cursor.close();
                    } else {
                        return new File(getPathFromInputStreamUri(activity, uri, System.currentTimeMillis() + ""));
                    }
                }
            }

        }
        if (imagePath == null) {
            return null;
        }
        return new File(imagePath);
    }

    /**
     * 用流拷贝文件一份到自己APP目录下
     *
     * @param context
     * @param uri
     * @param fileName
     * @return
     */
    public static String getPathFromInputStreamUri(Context context, Uri uri, String fileName) {
        InputStream inputStream = null;
        String filePath = null;

        if (uri.getAuthority() != null) {
            try {
                inputStream = context.getContentResolver().openInputStream(uri);
                File file = createTemporalFileFrom(context, inputStream, fileName);
                filePath = file.getPath();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return filePath;
    }

    private static File createTemporalFileFrom(Context context, InputStream inputStream, String fileName) {
        File targetFile = null;

        if (inputStream != null) {
            int read;
            byte[] buffer = new byte[8 * 1024];
            //自己定义拷贝文件路径
            targetFile = new File(getDiskCacheDir(context), fileName);
            if (targetFile.exists()) {
                targetFile.delete();
            }
            try {
                OutputStream outputStream = new FileOutputStream(targetFile);

                while ((read = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, read);
                }
                outputStream.flush();

                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

            }


        }

        return targetFile;
    }

    /**
     * 限制压缩文件 最大5兆
     * @param file 文件（图片）
     * @return 处理后的图片
     */
    public static File compress(Activity activity, File file) {
       return compress(activity, 5, file);
    }

    /**
     * 限制压缩文件
     * @param maxM 最大X兆
     * @param file 文件（图片）
     * @return 处理后的图片
     */
    public static File compress(Activity activity, long maxM, File file) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        float hh = dm.heightPixels;
        float ww = dm.widthPixels;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        opts.inJustDecodeBounds = false;
        int w = opts.outWidth;
        int h = opts.outHeight;
        int size = 0;
        if (w <= ww && h <= hh) {
            size = 1;
        } else {
            double scale = w >= h ? w / ww : h / hh;
            double log = Math.log(scale) / Math.log(2);
            double logCeil = Math.ceil(log);
            size = (int) Math.pow(2, logCeil);
        }
        opts.inSampleSize = size;
        String filePath = file.getPath();
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, opts);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;
        if (bitmap == null) {
            return null;
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        System.out.println(baos.toByteArray().length);
        while (baos.toByteArray().length > maxM * 1024 * 1024) {
            baos.reset();
            if (quality < 1) {
                quality = 1;
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            System.out.println(baos.toByteArray().length);
            if (quality <= 1) {
                break;
            }
            quality -= 20;
        }
        File currentFile = new File(file.getParentFile() + "/" + System.currentTimeMillis() + ".png");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(currentFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (fileOutputStream == null) {
            return null;
        }
        try {
            baos.writeTo(fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                baos.flush();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return currentFile;
    }

    /**
     * 保存ImageView到系统相册
     *
     * @param imageView
     * @return 语义化保存结果
     */
    public static void saveImageViewToPhotoAlbum(Application application, ImageView imageView, SaveFileDelegate saveFileDelegate) {
        Drawable drawable = imageView.getDrawable();
        saveBitmapFile(application, ((BitmapDrawable) drawable).getBitmap(), saveFileDelegate);
    }
    @SuppressLint("CheckResult")
    public static void saveBitmapFile(final Application application, final Bitmap bitmap, final SaveFileDelegate saveFileDelegate) {
        if(bitmap == null){
            if(saveFileDelegate != null){
                saveFileDelegate.saveEnd(false, null);
            }
        } else {
            XXPermissions.with(ActivityManager.getInstance())
                    .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                    .request(new OnPermissionCallback() {
                        @Override
                        public void onGranted(List<String> permissions, boolean all) {
                            if(all){
                                File file = getNewFile(application, System.currentTimeMillis() + ".png");
                                boolean isSaveSucess = false;
                                //兼容Android Q和以下版本
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    ContentValues values = new ContentValues();
                                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/*");
                                    values.put(MediaStore.Images.ImageColumns.TITLE, file.getName());
                                    values.put(MediaStore.Images.Media.DESCRIPTION, file.getName());
                                    values.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, file.getName());
                                    //android Q中不再使用DATA字段，而用RELATIVE_PATH代替
                                    //RELATIVE_PATH是相对路径不是绝对路径
                                    //DCIM是系统文件夹，关于系统文件夹可以到系统自带的文件管理器中查看，不可以写没存在的名字
                                    values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_DCIM + "/");
                                    Uri contentUri;
                                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                                    } else
                                        contentUri = MediaStore.Images.Media.INTERNAL_CONTENT_URI;
                                    Uri uri = application.getContentResolver().insert(contentUri, values);
                                    if (uri != null) {
                                        try (OutputStream os = application.getContentResolver().openOutputStream(uri)) {
                                            isSaveSucess = bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                                            // 告诉系统，文件准备好了，可以提供给外部了
                                            values.clear();
                                            values.put(MediaStore.MediaColumns.IS_PENDING, 0);
                                            application.getContentResolver().update(uri, values, null, null);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            // 失败的时候，删除此 uri 记录
                                            application.getContentResolver().delete(uri, null, null);
                                        }
                                    }
                                } else {
                                    try (OutputStream os = new BufferedOutputStream(new FileOutputStream(file))) {
                                        isSaveSucess = bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                                        if (file.exists()) {
                                            Uri uri = Uri.parse("file://" + file.getAbsolutePath());
                                            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                            intent.setData(uri);
                                            application.sendBroadcast(intent);
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }


                                if (!bitmap.isRecycled()) bitmap.recycle();
                                file.deleteOnExit();
                                if(saveFileDelegate != null){
                                    saveFileDelegate.saveEnd(isSaveSucess, file);
                                }
                            } else {
                                ToastUtil.showToast("权限不足");
                            }
                        }

                        @Override
                        public void onDenied(final List<String> permissions, final boolean never) {
                            StringBuilder stringBuilder = new StringBuilder();
                            for(String str: permissions){
                                stringBuilder.append("\n").append(str);
                            }
                            DialogUtils.showDialog("当前功能需要授权：" + stringBuilder + "\n否则将无法使用", new DialogUtils.DialogButtonModule("前往授权", new DialogUtils.DialogClickDelegate() {
                                @Override
                                public void click(DialogUtils.DialogButtonModule dialogButtonModule) {
                                    if(never){
                                        XXPermissions.startPermissionActivity(ActivityManager.getInstance(), permissions);
                                    } else {
                                        saveBitmapFile(application, bitmap, saveFileDelegate);
                                    }
                                }
                            }), new DialogUtils.DialogButtonModule("暂不授权", new DialogUtils.DialogClickDelegate() {
                                @Override
                                public void click(DialogUtils.DialogButtonModule dialogButtonModule) {
                                    ToastUtil.showToast("无权限操作，请重新尝试");
                                }
                            }));
                        }
                    });

        }

    }

    public interface SaveFileDelegate{
        void saveEnd(boolean saveSuccess, File file);
    }

    //file文件读取成byte[]

    public static byte[] file2bytes(Activity context, File file) {

        if (ContextCompat.checkSelfPermission(context, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, context.getString(R.string.no_read_permission), Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(context, new String[]{READ_EXTERNAL_STORAGE}, 1032);
            return null;
        }
        RandomAccessFile rf = null;

        byte[] data = null;

        try {

            rf = new RandomAccessFile(file, "r");

            data = new byte[(int) rf.length()];

            rf.readFully(data);

        } catch (Exception exception) {

            exception.printStackTrace();

        } finally {

            try {

                if (rf != null) {

                    rf.close();

                }

            } catch (Exception exception) {

                exception.printStackTrace();

            }

        }

        return data;

    }

    /**
     * 获取文件格式名
     */
    public static String getFormatName(String fileName) {
        //去掉首尾的空格
        fileName = fileName.trim();
        String[] s = fileName.split("\\.");
        if (s.length >= 2) {
            return s[s.length - 1];
        }
        return "";
    }

    public static byte[] file2byte(File tradeFile) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(tradeFile);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }



    /**
     *
     * @param url xxx.mp4  @return audio/mpeg
     */
    public static String getMimeType(String url){
        if(url == null) url = "";
        String name = url.toLowerCase();
        if (name.endsWith(".mp4") || name.endsWith(".avi")
                || name.endsWith(".3gpp") || name.endsWith(".3gp") || name.startsWith(".mov")) {
            return "video/mp4";
        } else if (name.endsWith(".PNG") || name.endsWith(".png") || name.endsWith(".jpeg")
                || name.endsWith(".gif") || name.endsWith(".GIF") || name.endsWith(".jpg")
                || name.endsWith(".webp") || name.endsWith(".WEBP") || name.endsWith(".JPEG")
                || name.endsWith(".bmp")) {
            return "image/jpeg";
        } else if (name.endsWith(".mp3") || name.endsWith(".amr")
                || name.endsWith(".aac") || name.endsWith(".war")
                || name.endsWith(".flac") || name.endsWith(".lamr")) {
            return "audio/mpeg";
        }

        return "image/jpeg";
    }
    /**
     * android中如何把`content://media/external/images/media/Y`转换为`file:///storage/sdcard0/Pictures/X.jpg`？
     * */
    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            if(cursor != null){
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            } else {
                String originPath = contentUri.toString();
                if(originPath == null){
                    originPath = contentUri.getPath();
                }
                return originPath == null ? "" : originPath;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    //endregion 保存圖片

    private static boolean isEmptyBitmap(Bitmap bitmap) {
        return bitmap == null || bitmap.isRecycled() || bitmap.getWidth() == 0 || bitmap.getHeight() == 0;
    }

    private static boolean createFile(File file, boolean isDeleteOldFile) {
        if (file == null) return false;
        if (file.exists()) {
            if (isDeleteOldFile) {
                if (!file.delete()) return false;
            } else
                return file.isFile();
        }
        if (!createDir(file.getParentFile())) return false;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            return false;
        }
    }

    private static boolean createDir(File file) {
        if (file == null) return false;
        if (file.exists())
            return file.isDirectory();
        else
            return file.mkdirs();
    }

    private static boolean isGranted(Context context) {
        return (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE));
    }

    //endregion
}
