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
import xyz.ttyz.tourfrxohc.activity.EndChatActivity;
import xyz.ttyz.tourfrxohc.activity.GameActivity;
import xyz.ttyz.tourfrxohc.activity.KeyActivity;
import xyz.ttyz.tourfrxohc.activity.LoginActivity;
import xyz.ttyz.tourfrxohc.event.UserChangeEvent;
import xyz.ttyz.tourfrxohc.event.VoiceEvent;
import xyz.ttyz.tourfrxohc.models.SocketEventModule;
import xyz.ttyz.tourfrxohc.models.UserModel;
import xyz.ttyz.tourfrxohc.models.game.HomeModel;
import xyz.ttyz.tourfrxohc.models.game.VoiceModel;
import xyz.ttyz.tourfrxohc.utils.HomeUtils;
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
                System.out.println("=============== > 3");
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
        RfRxOHCUtil.initApiService(this, "http://192.168.0.110:8080/tou3_war_exploded/", "ws://192.168.0.110:8080/tou3_war_exploded/devMessage",getPackageName() + "-cache",
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
                        //长连接断开
                        DialogUtils.showSingleDialog("连接超时", "网络连接超时，无法正常使用", new DialogUtils.DialogButtonModule("重新连接", new DialogUtils.DialogClickDelegate() {
                            @Override
                            public void click(DialogUtils.DialogButtonModule dialogButtonModule) {
                                HomeUtils.joinHome();
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
                        if(socketEventModule != null){
                            ProgressUtil.missCircleProgress();
                            switch (socketEventModule.getActionType()){
                                case 0://人员变化0 针对已在房间，人员离开因素
                                    UserModel changeUser = socketEventModule.getChangeUser();
                                    if(UserUtils.getCurUserModel().equals(changeUser)){
                                        //如果是我
                                        if(changeUser.getComeInType() == 1){
                                            //我进入
                                            GameActivity.show(socketEventModule.getRoomId());
                                        } else {
                                            //我离开
                                            if(GameActivity.confirmLeave){
                                                //自主选择离开
                                            } else {
                                                //网络异常导致离开
                                                HomeUtils.joinHome();
                                            }
                                        }
                                    } else {
                                        //是别人,通知页面更新
                                        EventBus.getDefault().post(new UserChangeEvent(changeUser));
                                    }
                                    break;
                                case 1://语音变化1
                                    EventBus.getDefault().post(socketEventModule.getVoiceModel());
                                    break;
                                case 2://房间变化2
                                    HomeModel homeModel = socketEventModule.getRoomModel();
                                    switch (homeModel.getType()){
                                        case 0://语音房
                                            //环节到了进语音房的时候
                                            GameActivity.show(homeModel.getRoomId());
                                            break;
                                        case 1://钥匙房
                                            KeyActivity.show(homeModel.getRoomId());
                                            break;
                                        case 2://结束后的聊嗨房
                                            EndChatActivity.show(homeModel.getRoomId());
                                            break;
                                        default:
                                    }
                                    break;
                                default:
                            }
                        }
                    }

                    @Override
                    public void socketReceived(byte[] bytes) {
                        UserModel userModel = new UserModel();
                        userModel.setVoiceBytes(bytes);
                        VoiceModel voiceModel = new VoiceModel(userModel);
                        EventBus.getDefault().post(voiceModel);
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
