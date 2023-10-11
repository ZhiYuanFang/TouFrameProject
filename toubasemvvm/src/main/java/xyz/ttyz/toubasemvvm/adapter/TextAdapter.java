package xyz.ttyz.toubasemvvm.adapter;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.InputFilter;
import android.text.SpannableString;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;


import java.io.InputStream;
import java.net.URL;

import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.toubasemvvm.utils.Constants;
import xyz.ttyz.toubasemvvm.utils.DensityUtil;

public class TextAdapter {
    @BindingAdapter("spannerText")
    public static void sp(TextView textView, SpannableString spannableString){
        textView.setText(spannableString);
    }

    @BindingAdapter("typeFace")
    public static void t(TextView textView, String typeFace){
        Typeface tf = Typeface.createFromAsset(textView.getContext().getAssets(), typeFace);
        textView.setTypeface(tf);
    }

    @BindingAdapter("inputType")
    public static void s(TextView textView, int type){
        textView.setInputType(type);
    }

    @BindingAdapter("htmlTxt")
    public static void html(final TextView textView,String htmlTxt){
        if(htmlTxt == null)
            htmlTxt = "";//不能return 会导致viewholder内的数据遗留历史
        final String finalHtmlTxt = htmlTxt;
        new Thread(new Runnable() {
            @Override
            public void run() {
                final CharSequence charSequence = Html.fromHtml(finalHtmlTxt.replace("\n","<br />"), new Html.ImageGetter() {
                    @Override
                    public Drawable getDrawable(String source) {
//                        GifDrawable gifDrawable = new GifDrawable(textView.getContext(), null, null, 123,122,null)
                        Drawable drawable = null;
                        URL url;
                        try {
                            url = new URL(source);
                            InputStream inputStream = url.openStream();
                            drawable = Drawable.createFromStream(inputStream, "");  //获取网路图片
                            inputStream.close();
                        } catch (Exception e) {
                            return null;
                        }
                        if(drawable == null){
                            return null;
                        }
                        textView.measure(0, 0);

                        int left = 0;
                        int right = 0;
                        if(textView.getLayoutParams()  instanceof  LinearLayout.LayoutParams){
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) textView.getLayoutParams();
                            left = params.leftMargin;
                            right = params.rightMargin;
                        }

                        int width = drawable.getIntrinsicWidth();
                        int endWidth = Constants.WINDOW_WIDTH - right - left;
                        float bili = (float) endWidth/width;

                        drawable.setBounds(left, 0, endWidth, (int) (drawable
                                                        .getIntrinsicHeight() * bili));
                        return drawable;
                    }
                }, null);
                ActivityManager.getInstance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(charSequence);
                    }
                });
            }
        }).start();
    }


    @BindingAdapter("drawableVisible")
    public static void v(TextView textView, boolean drawableVisible){
        Drawable[] drawables = textView.getCompoundDrawables();

        boolean hasDra = false;
        for(Drawable drawable : drawables){
            if(drawable != null){
                hasDra = true;
                break;
            }
        }
        if(hasDra){
            textView.setTag(drawables);
        }

        Drawable[] firstDras = (Drawable[]) textView.getTag();
        if(drawableVisible){
            if(firstDras != null && firstDras.length == 4){
                textView.setCompoundDrawables(firstDras[0], firstDras[1], firstDras[2], firstDras[3]);
            } else {
                //2020/11/11 不该出现
            }
        } else {
            textView.setCompoundDrawables(null, null, null, null);
        }
    }


    @BindingAdapter("maxLength")
    public static void m(TextView textView, int maxLength){
        textView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DensityUtil.dp2px(maxLength))});
    }
}
