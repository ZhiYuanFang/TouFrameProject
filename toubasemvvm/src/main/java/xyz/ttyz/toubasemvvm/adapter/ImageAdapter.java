package xyz.ttyz.toubasemvvm.adapter;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.databinding.BindingAdapter;


import java.io.File;

import xyz.ttyz.toubasemvvm.utils.Constants;
import xyz.ttyz.toubasemvvm.utils.ImageLoaderUtil;

public class ImageAdapter {
    private static ImageLoderDelegate loderDelegate;

    public static void init(ImageLoderDelegate imageLoderDelegate) {
        loderDelegate = imageLoderDelegate;
    }

    @BindingAdapter(value = {"notValid","loadProgressBar", "isCircle", "imageRadius", "imageUrl", "imageFilePath", "imageFile", "isBlur", "thumbnil", "dependenceWindow", "notJudgeGif"}, requireAll = false)
    public static void loadImage(ImageView imageView,boolean notValid, boolean loadProgressBar, boolean isCircle, float imageRadius, String imageUrl, String imageFilePath, File imageFile, boolean isBlur, float thumbnil, boolean dependenceWindow, boolean notJudgeGif) {
        if(notValid) return;
//        if(imageUrl == null || imageUrl.isEmpty()){
//            imageUrl = ImageLoaderUtil.testPic;
//        }

        ProgressBar progressBar = null;
        if (loadProgressBar) {
            progressBar = new ProgressBar(imageView.getContext());
            progressBar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        if (imageFilePath != null) {
            imageFile = new File(imageFilePath);
        }
        loderDelegate.loadImage(imageView, progressBar, isCircle, imageRadius, imageUrl, imageFile, isBlur, thumbnil, dependenceWindow, notJudgeGif);
    }

    @BindingAdapter(value = {"srcRes", "srcDrawable", "srcBitmap", "thumbnil"}, requireAll = false)
    public static void imageSrc(ImageView imageView, int srcRes, Drawable srcDrawable, Bitmap srcBitmap, float thumbnil) {

        int draHeight = 0, draWidth = 0;
        if (srcRes > 0) {
            imageView.setImageResource(srcRes);
        }
        if (srcDrawable != null) {
            imageView.setImageDrawable(srcDrawable);
            draHeight = srcDrawable.getIntrinsicHeight();
            draWidth = srcDrawable.getIntrinsicWidth();
        }
        if (srcBitmap != null) {
            imageView.setImageBitmap(srcBitmap);
            draHeight = srcBitmap.getHeight();
            draWidth = srcBitmap.getWidth();
        }

        if (thumbnil > 0 & draHeight > 0 & draWidth > 0) {
            int screenWidth = Constants.WINDOW_WIDTH * 5 / 8;//让图片的最大宽度为屏幕的3/8 会比较好看
            float bili = 1;
            if (draWidth > screenWidth) {
                bili = (float) screenWidth / draWidth;
            }
            int nowDraWidth = (int) (draWidth * bili);
            int nowDraHeight = (int) (draHeight * bili);
            ViewGroup.LayoutParams params = imageView.getLayoutParams();
            params.width = nowDraWidth;
            params.height = nowDraHeight;
            imageView.setLayoutParams(params);
        }
    }

    @BindingAdapter("imgLevel")
    public static void il(ImageView imageView, int imgLevel) {
        imageView.setImageLevel(imgLevel);
    }

    public interface ImageLoderDelegate {
        void loadImage(ImageView imageView, ProgressBar progressBar, boolean isCircle, float imageRadius, String imageUrl, File imageFile, boolean isBlur, float thumbnil, boolean dependenceWindow, boolean notJudgeGif);
    }
}
