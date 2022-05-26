package com.shouzhong.scanner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.shouzhong.idcard.IdCardUtils;

/**
 * 识别工具类
 */
public class ScannerUtils {

    /**
     * 识别身份证，建议在子线程运行
     *
     * @param bmp
     * @return
     */
    public static com.shouzhong.scanner.Result decodeIdCard(Context context, Bitmap bmp) throws Exception {
        if (bmp == null) return null;
        boolean boo = IdCardUtils.initDict(context);
        if (!boo) throw new Exception("init failure");
        final byte[] obtain = new byte[4096];
        int len = IdCardUtils.decode(bmp, obtain);
        if (len <= 0) {
            Matrix m = new Matrix();
            m.setRotate(90, (float) bmp.getWidth() / 2, (float) bmp.getHeight() / 2);
            bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), m, true);
            len = IdCardUtils.decode(bmp, obtain);
        }
        IdCardUtils.clearDict();
        if (len <= 0) return null;
        return Utils.decodeIdCard(obtain, len);
    }

    /**
     * 往图形中间添加logo，建议在子线程运行
     *
     * @param src
     * @param logo
     * @param scale 缩放比例，0~1
     * @return
     */
    public static Bitmap addLogo(Bitmap src, Bitmap logo, float scale) throws Exception {
        if (src == null) return null;
        if (logo == null) return src;
        //获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();
        if (srcWidth == 0 || srcHeight == 0) return null;
        if (logoWidth == 0 || logoHeight == 0 || scale == 0.0f) return src;
        scale = scale < 0 || scale > 1 ? 0.25f : scale;
        //logo大小为二维码整体大小的1/5
        float scaleFactor = srcWidth * scale / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(src, 0, 0, null);
        canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
        canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);
        canvas.save();
        canvas.restore();
        return bitmap;
    }
}
