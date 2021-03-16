package xyz.ttyz.toubasemvvm.utils;

import android.graphics.Paint;

/**

 *	前提是设置文字大小才能测量

 */

public class MeasureTextUtils {



    public static int measureHeight(Paint paint) {
        Paint.FontMetricsInt fm = paint.getFontMetricsInt();

        int textHeight = ~fm.top - (~fm.top - ~fm.ascent) - (fm.bottom - fm.descent);

        return textHeight;

    }



    public static int measureWidth(String text, Paint paint) {

        return (int) paint.measureText(text);

    }



}
