package xyz.ttyz.mylibrary;

import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.schedulers.Schedulers;
import xyz.ttyz.mylibrary.protect.CustomGsonConverterFactory;
import xyz.ttyz.mylibrary.protect.RfRxOHCIntercept;

/**
 * Created by tou on 2019/5/15.
 * 支持http请求的网络适配集成
 */

public class RfRxOHCUtil {
    private static final String TAG = "RfRxOHCUtil";
    /**
     * @param baseUrl        后台端口地址：http://127.0.0.1:8080/
     * @param directoryCache 网络缓存文件名
     * @param maxCache       最大缓存
     * @param timeOut        超时时间, 秒
     * @param isReleaseType  是否为正式版
     * @param debugable      是否支持日志打印
     * @param version        版本号
     * @param flavor         渠道名
     * @param terminal       设备名
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
    public static void initApiService(Application application,
                                      @NonNull String baseUrl,
                                      @NonNull String directoryCache,
                                      long maxCache,
                                      long timeOut,
                                      boolean isReleaseType,
                                      boolean debugable,
                                      String version,
                                      String flavor,
                                      String terminal,
                                      @NonNull TouRRCDelegate touRRCDelegate) {
        Log.i(TAG, "initApiService: directoryCache --> " + directoryCache);
        OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        //创建OkHttpClient对象
        Cache cache = new Cache(new File(application.getExternalCacheDir(), directoryCache), maxCache);//设置缓存策略
        httpBuilder.cache(cache)
                .addInterceptor(new RfRxOHCIntercept(application))
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
                .addInterceptor(new RfRxOHCIntercept(application))
                .build();
        //返回数据格式处理
        Retrofit.Builder retBuilder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(CustomGsonConverterFactory.create());
        touRRCDelegate.initApiService(retBuilder.build());
    }

    public interface TouRRCDelegate {
        /**
         * 自定义OkHttpClient 可做Https的支持
         * */
        void addMoreForOkHttpClient(OkHttpClient.Builder httpBuilder);

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
    }
}
