package xyz.ttyz.mylibrary;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import xyz.ttyz.mylibrary.method.HttpDefaultUtils;
import xyz.ttyz.mylibrary.protect.CustomGsonConverterFactory;
import xyz.ttyz.mylibrary.protect.RfRxOHCIntercept;
import xyz.ttyz.mylibrary.socket.SocketUtils;

/**
 * Created by tou on 2019/5/15.
 * 支持http请求的网络适配集成
 */

public class RfRxOHCUtil {
    private static final String TAG = "RfRxOHCUtil";
    public static int successCode = 0;
    public static String socketUrl;//长连接地址
    public static TouRRCDelegate touRRCDelegate;//总项目代理接口
    /**
     * @param baseUrl        后台端口地址：http://127.0.0.1:8080/
     * @param socketUrl 长连接地址：ws://localhost:8080/tou3_war_exploded/devMessage
     * @param directoryCache 网络缓存文件名
     * @param maxCache       最大缓存
     * @param timeOut        超时时间, 秒
     * @param isReleaseType  是否为正式版
     * @param debugable      是否支持日志打印
     * @param version        版本号
     * @param flavor         渠道名
     * @param terminal       设备名
     * @param successCode    接口请求成功返回的code值 ：code = 0
     * @param touRRCDelegate 建议根据产品需求在addMoreForInterceptor添加其它头部信息,
     *                       如果需要支持HTTPS，可以在addMoreForOkHttpClient中做自定义添加
     *                       eg:CustomTrust ct = new CustomTrust(application);
     *                       ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
     *                       .tlsVersions(TlsVersion.TLS_1_1)
     *                       .tlsVersions(TlsVersion.TLS_1_2)
     *                       .cipherSuites(
     *                       CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
     *                       CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
     *                       CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
     *                       .build();
     *                       builder.connectionSpecs(Collections.singletonList(spec))
     *                       .sslSocketFactory(ct.sslSocketFactory, ct.trustManager)
     *                       .hostnameVerifier((s, sslSession) -> true);
     */
    public static void initApiService(final Application application,
                                      String baseUrl,
                                      String socketUrl,
                                      String directoryCache,
                                      long maxCache,
                                      long timeOut,
                                      boolean isReleaseType,
                                      boolean debugable,
                                      String version,
                                      String flavor,
                                      String terminal,
                                      int successCode,
                                      final TouRRCDelegate touRRCDelegate) {
        RfRxOHCUtil.socketUrl = socketUrl;
        RfRxOHCUtil.successCode = successCode;
        RfRxOHCUtil.touRRCDelegate = touRRCDelegate;
        Log.i(TAG, "initApiService: directoryCache --> " + directoryCache);
        OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        //创建OkHttpClient对象
        Cache cache = new Cache(new File(application.getExternalCacheDir(), directoryCache), maxCache);//设置缓存策略
        httpBuilder.cache(cache)
//                .addInterceptor(new RfRxOHCIntercept(application))
                .retryOnConnectionFailure(true)
                .connectTimeout(timeOut, TimeUnit.SECONDS)
                .readTimeout(timeOut, TimeUnit.SECONDS)
                .writeTimeout(timeOut, TimeUnit.SECONDS);
        //设置输出日志打印
        LoggingInterceptor.Builder logBuilder = new LoggingInterceptor.Builder();
        logBuilder.setLevel(isReleaseType ? Level.BODY : Level.BASIC)//判断是否为正式版，保护后台接口不暴露
                .loggable(debugable)
                .addHeader("version", version)
                .addHeader("flavor", flavor)
                .addHeader("terminal", terminal)
                .log(Platform.INFO);
        touRRCDelegate.addMoreForInterceptor(logBuilder);
        touRRCDelegate.addMoreForOkHttpClient(httpBuilder);
        LoggingInterceptor loggingInterceptor = logBuilder.build();

        OkHttpClient okHttpClient = httpBuilder
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Response response = chain.proceed(chain.request());
                        touRRCDelegate.dealRebackHeader(response);
                        return response;
                    }
                })
//                .addInterceptor(new RfRxOHCIntercept(application))
                .build();
        //返回数据格式处理
        Retrofit.Builder retBuilder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(CustomGsonConverterFactory.create());
        touRRCDelegate.initApiService(retBuilder.build());
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                if(touRRCDelegate.isLogin()){
                    SocketUtils.openMinaReceiver(application);
                }
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {
                //退到后台，所有请求关闭
                HttpDefaultUtils.isRequestIng = false;
                HttpDefaultUtils.getWaitUiSubscriber().clear();
                System.out.println("app退到后台 ###############################");
            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        });
    }

    public interface TouRRCDelegate {
        /**
         * 自定义OkHttpClient 可做Https的支持
         * */
        void addMoreForOkHttpClient(OkHttpClient.Builder httpBuilder);

        void dealRebackHeader(Response originalResponse);
        /**
         * 添加自定义header
         * logBuilder.addHeader("terminal", terminal)
         * */
        void addMoreForInterceptor(LoggingInterceptor.Builder logBuilder);

        /**
         * 实现该方法，定义Retrofit接口文件
         * touService = retrofit.create(TouService.class);
         */
        void initApiService(Retrofit retrofit);

        /**
         *
         * @return 长连接的头部信息
         */
        Map<String, Object> socketInitHeader();

        /**
         * 长连接超时处理，已经做好了超时后，关闭长连接
         * 建议此处弹框提示超时，并提供重连机制
         * Activity activity = ActivityManager.getInstance();
         *                     if (activity != null) {
         *                         activity.runOnUiThread(new Runnable() {
         *                             public void run() {
         *                                 DialogUtils.showDialog(activity, activity.getString(R.string.time_out), activity.getString(R.string.socket_time_out),
         *                                         new DialogUtils.DialogButtonModule(activity.getString(R.string.re_login), new DialogUtils.DialogClickDelegate() {
         *                                             public void click(DialogUtils.DialogButtonModule dialogButtonModule) {
         *                                                 PersonUtils.outLoginAndGoToLoginActivity(false);
         *                                             }
         *                                         }), new DialogUtils.DialogButtonModule(activity.getString(R.string.retry), new DialogUtils.DialogClickDelegate() {
         *                                             public void click(DialogUtils.DialogButtonModule dialogButtonModule) {
         *                                                 SocketUtils.openMinaReceiver(application, isForce);
         *                                             }
         *                                         }), false);
         *                             }
         *                         });
         *                     }
         */
        void socketConnectTimeOut();

        /**
         * 接收到长连接消息
         */
        void socketReceived(String message);

        /**
         * 是否已经登录
         * 如果已经登录，APP打开后，连接socket
         */
        boolean isLogin();
    }
}
