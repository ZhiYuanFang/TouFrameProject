package xyz.ttyz.tourfrxohc;

import android.app.Application;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.ihsanbal.logging.LoggingInterceptor;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import xyz.ttyz.mylibrary.RfRxOHCUtil;
import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.mylibrary.method.ProgressUtil;
import xyz.ttyz.mylibrary.socket.LogUtils;
import xyz.ttyz.mylibrary.socket.SocketUtils;
import xyz.ttyz.tou_example.init.ApplicationUtils;
import xyz.ttyz.tou_example.init.TouDelegate;
import xyz.ttyz.toubasemvvm.utils.DialogUtils;
import xyz.ttyz.tourfrxohc.activity.GameActivity;
import xyz.ttyz.tourfrxohc.activity.LoginActivity;
import xyz.ttyz.tourfrxohc.event.VoiceEvent;
import xyz.ttyz.tourfrxohc.models.SocketEventModule;
import xyz.ttyz.tourfrxohc.utils.UserUtils;

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
                return UserUtils.isLogin();
            }

            @Override
            public void gotoLoginActivity() {
                //前往登录界面
                LoginActivity.show();
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
        RfRxOHCUtil.initApiService(this, "http://192.168.1.201:8080/tou3_war_exploded/", "ws://192.168.1.201:8080/tou3_war_exploded/devMessage",getPackageName() + "-cache",
                2 * 1024 * 1024, 30, BuildConfig.BUILD_TYPE.equals("release"), BuildConfig.DEBUG, BuildConfig.VERSION_NAME,
                "huawei", "android", 1, new RfRxOHCUtil.TouRRCDelegate() {
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
                                        .header("Role", "32")
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

                    @Override
                    public Map<String, Object> socketInitHeader() {
                        return null;
                    }

                    @Override
                    public void socketConnectTimeOut() {
                        DialogUtils.showSingleDialog("连接超时", "网络连接超时，无法正常使用，请尝试重新打开app", new DialogUtils.DialogButtonModule("确定退出", new DialogUtils.DialogClickDelegate() {
                            @Override
                            public void click(DialogUtils.DialogButtonModule dialogButtonModule) {
                                System.exit(0);
                            }
                        }));
                    }

                    @Override
                    public void socketReceived(String message) {
                        SocketEventModule socketEventModule = null;
                        try {
                            socketEventModule = new Gson().fromJson(message, SocketEventModule.class);
                        } catch (JsonSyntaxException e) {
                            LogUtils.showWebSocketLog("消息格式不能转JSON");
                        }
                        if(socketEventModule != null && socketEventModule.getUserModels().contains(UserUtils.getCurUserModel())){
                            //2021/4/8 我匹配成功了， 进入房间roomId
                            ProgressUtil.missCircleProgress();
                            GameActivity.show(socketEventModule.getRoomId());
                        }
                    }

                    @Override
                    public void socketReceived(byte[] bytes) {
                        EventBus.getDefault().post(new VoiceEvent(bytes));
                    }

                    @Override
                    public long userId() {
                        return UserUtils.getCurUserModel() == null ? 0 : UserUtils.getCurUserModel().getId();
                    }

                    @Override
                    public boolean isLogin() {
                        return UserUtils.isLogin();
                    }
                });
    }
}
