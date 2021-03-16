package xyz.ttyz.toubasemvvm.utils;

import android.graphics.Bitmap;
import android.graphics.Color;

public class ColorUtils {

    public static boolean isBlackTheme(Bitmap src){
        int bright = getBrightness(src);
        return bright <= 201;//161只能往高调 不得高于232
    }


    private static int getBrightness(Bitmap src) {

        if (src == null) {

            return 0;

        }

        int width = src.getWidth();

        int height = src.getHeight();

        int r, g, b;

        int pixel;

        int pixelCount = 0;

        double bright = 0;

        for (int x = 0; x < width; ++x) {

            for (int y = 0; y < height; ++y) {

                pixelCount++;

                pixel = src.getPixel(x, y);

                r = Color.red(pixel);

                g = Color.green(pixel);

                b = Color.blue(pixel);

                bright = bright + 0.299 * r + 0.587 * g + 0.114 * b;

            }

        }

        return (int) (bright / pixelCount);

    }

}
