package xyz.ttyz.tourfrxohc;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import androidx.multidex.MultiDexApplication;

import com.ihsanbal.logging.LoggingInterceptor;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;
import retrofit2.Retrofit;
import xyz.ttyz.mylibrary.RfRxOHCUtil;
import xyz.ttyz.mylibrary.protect.CustomTrust;
import xyz.ttyz.tou_example.init.ApplicationUtils;
import xyz.ttyz.tou_example.init.TouDelegate;
import xyz.ttyz.tourfrxohc.utils.Tls12SocketFactory;

/**
 * Created by tou on 2019/5/20.
 */

public class BaseApplication extends MultiDexApplication {
    private static final String TAG = "BaseApplication";
    public static ApiService apiService;
    static class miTM implements TrustManager, X509TrustManager {
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(X509Certificate[] certs) {
            return true;
        }

        @Override
        public void checkServerTrusted(X509Certificate[] certs, String authType)
                throws CertificateException {
            return;
        }

        @Override
        public void checkClientTrusted(X509Certificate[] certs, String authType)
                throws CertificateException {
            return;
        }
    }
    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationUtils.init(this, 1280, 800, new TouDelegate() {
            @Override
            public boolean isLogin() {
                return DefaultUtils.token != null;//当前用户是否登录
            }

            @Override
            public void gotoLoginActivity() {
                //前往登录界面
            }

            @Override
            public void checkVersion(VersionDelegate versionDelegate) {
                //请求接口判断是否需要更新
                if (true) {
                    versionDelegate.installVersion("", "更新了一些功能", BuildConfig.VERSION_CODE);
                }
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
        RfRxOHCUtil.initApiService(this, BuildConfig.BUILD_TYPE.equals("release") ? "https://eye.maihaoche.com/" : "https://eye-c.maihaoche.net/", "",getPackageName() + "-cache",
                2 * 1024 * 1024, 30, BuildConfig.BUILD_TYPE.equals("release"), BuildConfig.DEBUG, BuildConfig.VERSION_NAME,
                "huawei", "android", 0, new RfRxOHCUtil.TouRRCDelegate() {
                    @Override
                    public void addMoreForOkHttpClient(OkHttpClient.Builder httpBuilder) {
                        //https://github.com/square/okhttp/issues/2372#issuecomment-244807676
//                        if (Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT < 22) {
//                            try {
//                                TrustManager[] trustAllCerts = new TrustManager[1];
//                                TrustManager tm = new miTM();
//                                trustAllCerts[0] = tm;
//                                SSLContext sc = SSLContext.getInstance("TLSv1.2");
////                                sc.init(null, trustAllCerts, new SecureRandom());
//                                sc.init(null, null, null);
//                                httpBuilder.sslSocketFactory(new Tls12SocketFactory(sc.getSocketFactory()));
//
//                                ConnectionSpec cs = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
//                                        .tlsVersions(TlsVersion.TLS_1_2)
//                                        .build();
//
//                                List<ConnectionSpec> specs = new ArrayList<>();
//                                specs.add(cs);
//                                specs.add(ConnectionSpec.COMPATIBLE_TLS);
//                                specs.add(ConnectionSpec.CLEARTEXT);
//
//                                httpBuilder.connectionSpecs(specs);
//                                httpBuilder.hostnameVerifier(new HostnameVerifier() {
//                                    @Override
//                                    public boolean verify(String s, SSLSession sslSession) {
//                                        return true;
//                                    }
//                                });
//                            } catch (Exception exc) {
//                                Log.e("OkHttpTLSCompat", "Error while setting TLS 1.2", exc);
//                            }
//                        }
                        //动态值
//                        httpBuilder.addInterceptor(new Interceptor() {
//                            @Override
//                            public Response intercept(Chain chain) throws IOException {
//                                Request originalRequest = chain.request();
//                                if (DefaultUtils.token == null) {
//                                    return chain.proceed(originalRequest);
//                                }
//                                Request authorised = originalRequest.newBuilder()
//                                        .header("Authorization", "Bearer " + DefaultUtils.token)
//                                        .header("Role", "32")
//                                        .build();
//                                return chain.proceed(authorised);
//                            }
//                        });
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
                    public boolean isLogin() {
                        return true;
                    }
                });
    }
}
