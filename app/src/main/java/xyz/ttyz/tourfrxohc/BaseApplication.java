package xyz.ttyz.tourfrxohc;

import android.app.Application;
import android.os.Build;

import com.ihsanbal.logging.LoggingInterceptor;

import okhttp3.OkHttpClient;
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
        RfRxOHCUtil.initApiService(this, "http://bgtest.yougbang.com", getPackageName() + "-cache",
                2 * 1024 * 1024, 30, BuildConfig.BUILD_TYPE.equals("release"), BuildConfig.DEBUG, BuildConfig.VERSION_NAME,
                BuildConfig.FLAVOR, "android", new RfRxOHCUtil.TouRRCDelegate() {
                    @Override
                    public void addMoreForOkHttpClient(OkHttpClient.Builder httpBuilder) {

                    }

                    @Override
                    public void addMoreForInterceptor(LoggingInterceptor.Builder logBuilder) {

                    }

                    @Override
                    public void initApiService(Retrofit retrofit) {
                        apiService = retrofit.create(ApiService.class);
                    }
                });
    }
}
