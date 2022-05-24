package xyz.ttyz.tou_example.init;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.Objects;

import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.toubasemvvm.R;
import xyz.ttyz.toubasemvvm.adapter.ImageAdapter;
import xyz.ttyz.toubasemvvm.event.NetEvent;
import xyz.ttyz.toubasemvvm.utils.Constants;
import xyz.ttyz.toubasemvvm.utils.ImageLoaderUtil;

public class ApplicationUtils {
    public static TouDelegate touDelegate;

    public static void init(Application application, float ui_width, float ui_height, final TouDelegate touDelegate) {
        ApplicationUtils.touDelegate = touDelegate;
        //注册网络监听
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            NetworkRequest.Builder netBuilder = new NetworkRequest.Builder();
            NetworkRequest request = netBuilder.build();
            ConnectivityManager connMgr = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connMgr != null) {
                connMgr.registerNetworkCallback(request, new ConnectivityManager.NetworkCallback(){
                    @Override
                    public void onAvailable(@NonNull Network network) {
                        super.onAvailable(network);
                        EventBus.getDefault().post(new NetEvent(true));
                    }

                    @Override
                    public void onLost(@NonNull Network network) {
                        super.onLost(network);
                        EventBus.getDefault().post(new NetEvent(false));
                    }

                    @Override
                    public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
                        super.onCapabilitiesChanged(network, networkCapabilities);
                        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                                EventBus.getDefault().post(new NetEvent(NetEvent.NetType.WIFI));
                            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                                EventBus.getDefault().post(new NetEvent(NetEvent.NetType.CMWAP));
                            } else {
                                EventBus.getDefault().post(new NetEvent(NetEvent.NetType.AUTO));
                            }
                        }
                    }
                });
            }
        }
        Resources resources = application.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Constants.WINDOW_WIDTH = displayMetrics.widthPixels;
        Constants.WINDOW_HEIGHT = displayMetrics.heightPixels;
        //android 7.0 系统拍照问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        initScreen(application, ui_width, ui_height);
        initSmartRefresh();
        ImageLoaderUtil.init(R.drawable.bg_loader_retangle, R.drawable.bg_loader_circle, R.drawable.bg_loader_retangle, R.drawable.bg_loader_circle, new ImageLoaderUtil.ImageLoaderDelegate() {
            @Override
            public String safeUrl(String url) {
                if (TextUtils.isEmpty(url)) {
                    return "";
                }
                if (!(url.contains("http://") || url.contains("https://") || url.contains("file://"))) {
//                    url = OssHandler.getPublicUrl(url);//阿里云场景需要拼连接
                }
                return url;
            }
        });
        ImageAdapter.init(new ImageAdapter.ImageLoderDelegate() {
            @Override
            public void loadImage(ImageView imageView, ProgressBar progressBar, boolean isCircle, float imageRadius, String imageUrl, File imageFile, boolean isBlur, float thumbnil, boolean dependenceWindow, boolean notJudgeGif) {
                Object obj = imageUrl == null ? imageFile : imageUrl;
                if (obj == null) return;//如果为空，则不修改当前图片。 这里不能删除
//                obj = ImageLoaderUtil.testGif;
                if (!notJudgeGif && obj instanceof String) {
                    String urlStr = (String) obj;
                    if (urlStr.toLowerCase().contains(".gif")) {
                        //是动图
                        Glide.with(imageView.getContext()).asGif().load(urlStr).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(imageView);
                        return;
                    }
                }
                if (progressBar != null) {
                    ImageLoaderUtil.display(imageView, progressBar, obj);
                } else if (thumbnil > 0 || dependenceWindow) {
                    ImageLoaderUtil.displayThumbRound(imageView, dependenceWindow, thumbnil, obj, imageRadius, null);
                } else if (isBlur) {
                    ImageLoaderUtil.displayBlur(imageView, obj);
                } else if (isCircle) {
                    ImageLoaderUtil.displayCircle(imageView, obj);
                } else {
                    ImageLoaderUtil.displayRound(imageView, obj, imageRadius);
                }
            }
        });
//        new Handler(Looper.getMainLooper()).post(new Runnable() {
//            @Override
//            public void run() { //主线程异常拦截
//                while (true) {
//                    try {
//                        Looper.loop();//主线程的异常会从这里抛出
//                    } catch (Throwable e) {
//                        e.printStackTrace();
//                        touDelegate.cacheMainThrowable(e);
//                        if (ActivityManager.getInstance() != null)
//                            ActivityManager.getInstance().finish();
//                    }
//                }
//            }
//        });
    }

    private static void initSmartRefresh() {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                layout.setPrimaryColorsId(android.R.color.white, android.R.color.black);//全局设置主题颜色
                return new MaterialHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }


    //region Helper

    /**
     * @param width  750
     * @param height 1334
     */
    private static void initScreen(Application application, float width, float height) {
        new ScreenAdaptation(application, width, height).register();
    }

    //endregion
}
