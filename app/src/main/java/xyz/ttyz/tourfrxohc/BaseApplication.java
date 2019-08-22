package xyz.ttyz.tourfrxohc;

import android.app.Application;

import com.ihsanbal.logging.LoggingInterceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import xyz.ttyz.mylibrary.RfRxOHCUtil;

/**
 * Created by tou on 2019/5/20.
 */

public class BaseApplication extends Application {
    public static ApiService apiService;

    @Override
    public void onCreate() {
        super.onCreate();
        //http://172.16.10.12:5000
        //https://yysyservice.com:20001
        //https://hlqs.zyclp.top
        RfRxOHCUtil.initApiService(this, "https://yysyservice.com:20001", getPackageName() + "-cache",
                2 * 1024 * 1024, 30, BuildConfig.BUILD_TYPE.equals("release"), BuildConfig.DEBUG, BuildConfig.VERSION_NAME,
                BuildConfig.FLAVOR, "android", new RfRxOHCUtil.TouRRCDelegate() {
                    @Override
                    public void addMoreForOkHttpClient(OkHttpClient.Builder httpBuilder) {
                        //动态值
                        httpBuilder.addInterceptor(new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Request originalRequest = chain.request();
                                if (DefaultUtils.token == null) {
                                    return chain.proceed(originalRequest);
                                }
                                Request authorised = originalRequest.newBuilder()
                                        .header("Authorization", "Bearer " + DefaultUtils.token)
                                        .build();
                                return chain.proceed(authorised);
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
                });
    }
}
