package xyz.ttyz.toubasemvvm.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.concurrent.ExecutionException;

import xyz.ttyz.tou_example.ActivityManager;
import jp.wasabeef.glide.transformations.BlurTransformation;
import xyz.ttyz.toubasemvvm.transform.GlideCircleTransfromUtil;
import xyz.ttyz.toubasemvvm.transform.GlideRoundTransform;
import xyz.ttyz.toubasemvvm.weight.CardImageView;

/**
 * Description : 图片加载工具类 使用glide框架封装
 */
public class ImageLoaderUtil {
    public static final String testPic2 = "http://pic67.nipic.com/file/20150517/20795124_080722702002_2.jpg";
    public static final String testPic = "https://img-qn.51miz.com/preview/photo/00/01/57/36/P-1573636-42FEDBBA.jpg";
public static final String testGif = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1604055493280&di=4fcc6be45149c9a65813706d22e8dd77&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201611%2F04%2F20161104110413_XzVAk.gif";
    private static int placeholderResId, placeholderCircleResId, errorResId, errorCircleResId;
    private static ImageLoaderDelegate imageLoaderDelegate;

    public static void init(@DrawableRes int placeholderRes, @DrawableRes int placeholderCircleRes,
                            @DrawableRes int errorRes, @DrawableRes int errorCircleRes, ImageLoaderDelegate delegate) {
        placeholderResId = placeholderRes;
        placeholderCircleResId = placeholderCircleRes;
        errorResId = errorRes;
        errorCircleResId = errorCircleRes;
        imageLoaderDelegate = delegate;
    }

    /**
     * 加载String的图片地址
     *
     * @param imageView imageview
     * @param url       图片地址
     */
    public static void display(ImageView imageView, Object url) {
        url = safeUrl(url);
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        if (TouUtils.scanForActivity(imageView.getContext()).isFinishing()) {
            return;
        }
        Glide.with(imageView.getContext()).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)

                .placeholder(placeholderResId)
                .error(errorResId)
                .into(imageView);
    }

    public static void display(CardImageView cardImageView, Object url) {
        url = safeUrl(url);
        if (cardImageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        if (TouUtils.scanForActivity(cardImageView.getContext()).isFinishing()) {
            return;
        }
        if (url instanceof String) {
            if (isLocalFile(url)) {
                cardImageView.imageFilePath.set((String) url);
            } else
                cardImageView.imageUrl.set((String) url);
        } else if (url instanceof File) {
            cardImageView.imageFile.set((File) url);
        }
    }

    public static Object safeUrl(Object url) {
        if (url instanceof String) {
            if (isLocalFile(url)) {
                url = FileUtil.getRealPathFromUri(ActivityManager.getInstance(), Uri.parse((String) url));
                return new File((String) url);
            }
            return imageLoaderDelegate.safeUrl((String) url);
        } else {
            return url;
        }
    }

    public static boolean isLocalFile(Object obj) {
//        /storage/emulated/0/Android/data/com.x16.coe.fsc.dev/cache/luban_disk_cache/159125218233572.jpeg
//        /storage/emulated/0/PictureSelector/CameraImage/PictureSelector_20200602_113913.mp4
        //判断是否为本地文件的条件有待商榷
        return obj instanceof String && !(((String) obj).startsWith("http://") || ((String) obj).startsWith("https://"));
    }

    /**
     * 加载String的圆形图片
     *
     * @param imageView imageview
     * @param url       图片地址
     */
    public static void displayCircle(ImageView imageView, Object url) {
        url = safeUrl(url);
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        if (TouUtils.scanForActivity(imageView.getContext()).isFinishing()) {
            return;
        }
        Glide.with(imageView.getContext()).load(url)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(placeholderCircleResId)
                .error(errorCircleResId)
                .thumbnail(0.2f)

                .transform(new GlideCircleTransfromUtil()).into(imageView);
    }

    /**
     * 加载0.2质量缩小的图片
     * 自适应图片大小布局
     *
     * @param imageView imageview
     * @param url       图片地址
     */
    public static void displayThumbRound(final ImageView imageView, boolean dependenceWindow, Object url, float radius) {
        displayThumbRound(imageView, dependenceWindow, 0f, url, radius, null);
    }

    /**
     * @param imageView                 图片载体 如果dependenceWindow==false 则用图片载体的宽度做显示宽度
     * @param dependenceWindow          是否依赖手机宽度做显示
     * @param thumbnil                  最大显示比例 0则为3/8
     * @param url                       图片
     * @param radius                    圆角
     * @param displayThumbRoundDelegate 反馈图片宽度
     */
    public static void displayThumbRound(final ImageView imageView, final boolean dependenceWindow, float thumbnil, Object url, float radius, final DisplayThumbRoundDelegate displayThumbRoundDelegate) {
        url = safeUrl(url);
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        if (TouUtils.scanForActivity(imageView.getContext()).isFinishing()) {
            return;
        }
        if (thumbnil == 0) {
            thumbnil = (float) 3 / 8;
        }
        final float finalThumbnil = thumbnil;
        Glide.with(imageView.getContext()).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(errorResId)
                .transform(new GlideRoundTransform(radius))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        int draHeight = resource.getIntrinsicHeight();
                        int draWidth = resource.getIntrinsicWidth();
//                        DisplayMetrics dm = imageView.getContext().getResources().getDisplayMetrics();
                        int windowWidth;
                        if (dependenceWindow) {
                            windowWidth = Constants.WINDOW_WIDTH;
                        } else {
                            windowWidth = imageView.getWidth();
                        }
                        int screenWidth = (int) (windowWidth * finalThumbnil);

                        int minScreenWidth = screenWidth * 2 / 3;//最小宽度为2/8
                        float bili = 1;
                        if (draWidth > screenWidth) {
                            bili = (float) screenWidth / draWidth;
                        } else {
                            if (draWidth < minScreenWidth) {
                                bili = (float) minScreenWidth / draWidth;
                            }
                        }
                        int nowDraWidth = (int) (draWidth * bili);
                        int nowDraHeight = (int) (draHeight * bili);
                        if (displayThumbRoundDelegate == null) {
                            ViewGroup.LayoutParams params = imageView.getLayoutParams();
                            params.width = nowDraWidth;
                            params.height = nowDraHeight;
                            imageView.setLayoutParams(params);
                        } else {
                            displayThumbRoundDelegate.loadImageFinish(nowDraWidth, nowDraHeight);
                        }
                        return false;
                    }
                })
                .into(imageView);
    }

    public interface DisplayThumbRoundDelegate {
        void loadImageFinish(int width, int height);
    }

    /**
     * 加载圆角的图片
     * 自适应图片大小布局
     *
     * @param imageView imageview
     * @param url       图片地址
     */
    public static void displayRound(final ImageView imageView, Object url, float radius) {
        url = safeUrl(url);
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        if (TouUtils.scanForActivity(imageView.getContext()).isFinishing()) {
            return;
        }
        if (radius > 0) {
            Glide.with(imageView.getContext()).load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(errorResId)

                    .transform(new GlideRoundTransform(radius))
                    .into(imageView);
        } else
            Glide.with(imageView.getContext()).load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(errorResId)
                    .into(imageView);
    }

    public static void display(ImageView imageView, @NonNull final View progressBar, Object url, float radius) {
        url = safeUrl(url);
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        if (TouUtils.scanForActivity(imageView.getContext()).isFinishing()) {
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        Glide.with(imageView.getContext()).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                
//                .placeholder(placeholderResId)
                .error(errorResId)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imageView);
    }

    /**
     * 加载String的图片地址
     *
     * @param imageView imageview
     * @param url       图片地址
     */
    public static void display(ImageView imageView, @NonNull final View progressBar, Object url) {
        url = safeUrl(url);
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        if (TouUtils.scanForActivity(imageView.getContext()).isFinishing()) {
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        Glide.with(imageView.getContext()).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                
//                .placeholder(placeholderResId)
                .error(errorResId)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imageView);
    }

    /**
     * 加载高斯模糊的图片地址
     *
     * @param imageView imageview
     * @param url       图片地址
     */
    public static void displayBlur(ImageView imageView, Object url) {
        url = safeUrl(url);
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        if (TouUtils.scanForActivity(imageView.getContext()).isFinishing()) {
            return;
        }
        Glide.with(imageView.getContext())
                .asBitmap()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(placeholderResId)
                .error(errorResId)
                .load(url)
                .transform(new BlurTransformation(30)).into(imageView);
    }

    public static void getBitmap(Context context, Object url, final Handler.Callback callback) {
        url = safeUrl(url);
        if (context == null) {
            throw new IllegalArgumentException("argument error");
        }
        if (TouUtils.scanForActivity(context).isFinishing()) {
            return;
        }
        Glide.with(context)
                .asBitmap() //必须
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Message message = new Message();
                        message.obj = resource;
                        callback.handleMessage(message);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });


    }

    public static Bitmap getBitmap(Context context, Object url) throws ExecutionException, InterruptedException {

        Bitmap myBitmap = Glide.with(context)
                .asBitmap() //必须
                .load(url)
                .centerCrop()
                .submit()
                .get();
        return myBitmap;
    }

    public interface ImageLoaderDelegate {
        String safeUrl(String url);
    }

    public static String getVideoSnapOfUrl(String videoUrl) {//取1秒帧为封面
        return videoUrl + "?x-oss-process=video/snapshot,t_1000,f_jpg,m_fast,ar_auto";
    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}
