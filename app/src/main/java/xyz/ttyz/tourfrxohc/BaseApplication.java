package xyz.ttyz.tourfrxohc;

import android.app.Application;
import android.util.Log;

import com.ihsanbal.logging.LoggingInterceptor;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import xyz.ttyz.mylibrary.RfRxOHCUtil;
import xyz.ttyz.tou_example.init.ApplicationUtils;
import xyz.ttyz.tou_example.init.TouDelegate;
import xyz.ttyz.tourfrxohc.activity.LoginActivity;

/**
 * Created by tou on 2019/5/20.
 */

public class BaseApplication extends Application {
    private static final String TAG = "BaseApplication";
    public static ApiService apiService;

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationUtils.init(this, 750, 1334, new TouDelegate() {
            @Override
            public boolean isLogin() {
                return DefaultUtils.getCookie() != null;
            }

            @Override
            public void gotoLoginActivity() {
                //前往登录界面
                LoginActivity.show();
            }

            @Override
            public void checkVersion(VersionDelegate versionDelegate) {
                //请求接口判断是否需要更新
//                if (true) {
//                    versionDelegate.installVersion("", "更新了一些功能", BuildConfig.VERSION_CODE);
//                }
            }

            @Override
            public String applicationId() {
                return BuildConfig.APPLICATION_ID;
            }

            @Override
            public void cacheMainThrowable(Throwable e) {
                //捕获主线程异常，上传bugly
            }
        });
        RfRxOHCUtil.initApiService(this, DefaultUtils.getIp(), "",getPackageName() + "-cache",
                2 * 1024 * 1024, 30, BuildConfig.BUILD_TYPE.equals("release"), BuildConfig.DEBUG, BuildConfig.VERSION_NAME,
                "huawei", "android", 0, new RfRxOHCUtil.TouRRCDelegate() {
                    @Override
                    public void addMoreForOkHttpClient(OkHttpClient.Builder httpBuilder) {
                        //动态值
                        httpBuilder.addInterceptor(new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Request.Builder builder = chain.request().newBuilder();
                                HashSet<String> preferences = DefaultUtils.getCookie();
                                if (preferences != null) {
                                    for (String cookie : preferences) {
                                        builder.addHeader("Cookie", cookie);
                                        Log.v("OkHttp", "Adding Header: " + cookie); // This is done so I know which headers are being added; this interceptor is used after the normal logging of OkHttp
                                    }
                                }
                                return chain.proceed(builder.build());

                            }
                        });
                    }

                    @Override
                    public void addMoreForInterceptor(LoggingInterceptor.Builder logBuilder) {
                        //静态值
                        logBuilder.addHeader("Data-Type", "json")
                                .addHeader("Accept", "*/*")
                                .addHeader("Cache-Control", "no-cache")
                                .addHeader("x-app-version", "1.0");
                    }

                    @Override
                    public void initApiService(Retrofit retrofit) {
                        apiService = retrofit.create(ApiService.class);
                    }

                    @Override
                    public Map<String, Object> socketInitHeader() {
                        return null;
                    }

                    @Override
                    public void socketConnectTimeOut() {

                    }

                    @Override
                    public void socketReceived(String message) {

                    }

                    @Override
                    public void dealRebackHeader(Response originalResponse) {
                        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                            HashSet<String> cookies = new HashSet<>();

                            for (String header : originalResponse.headers("Set-Cookie")) {
                                cookies.add(header);
                            }

                           DefaultUtils.setCookie(cookies);
                        }

                    }

                    @Override
                    public boolean isLogin() {
                        return DefaultUtils.getCookie() != null;
                    }
                });


    }
}
