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
import xyz.ttyz.mylibrary.method.ProgressUtil;
import xyz.ttyz.mylibrary.socket.LogUtils;
import xyz.ttyz.tou_example.init.ApplicationUtils;
import xyz.ttyz.tou_example.init.TouDelegate;
import xyz.ttyz.toubasemvvm.utils.DialogUtils;
import xyz.ttyz.tourfrxohc.activity.GameActivity;
import xyz.ttyz.tourfrxohc.activity.LoginActivity;
import xyz.ttyz.tourfrxohc.dialog.WaitDialogFragment;
import xyz.ttyz.tourfrxohc.event.GameEndEvent;
import xyz.ttyz.tourfrxohc.event.GoKeyRoomEvent;
import xyz.ttyz.tourfrxohc.event.RebackVoiceRoomEvent;
import xyz.ttyz.tourfrxohc.event.RoleTypeConfirm;
import xyz.ttyz.tourfrxohc.event.SpeakRoundEvent;
import xyz.ttyz.tourfrxohc.event.StartVoteEvent;
import xyz.ttyz.tourfrxohc.event.UserChangeEvent;
import xyz.ttyz.tourfrxohc.event.VoteEndEvent;
import xyz.ttyz.tourfrxohc.models.ActionType;
import xyz.ttyz.tourfrxohc.models.SocketEventModule;
import xyz.ttyz.tourfrxohc.models.game.HomeModel;
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
        RfRxOHCUtil.initApiService(this, "http://192.168.1.201:8080/tou3_war_exploded/", "ws://192.168.1.201:8080/tou3_war_exploded/devMessage", getPackageName() + "-cache",
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
                        if (socketEventModule != null) {
                            ProgressUtil.missCircleProgress();
                            HomeModel homeModel = socketEventModule.getRoomModel();
                            switch (socketEventModule.getActionType()) {
                                case ActionType.memberComeOut:
                                case ActionType.memberComeIn:
                                    //考虑到匹配界面
                                    WaitDialogFragment waitDialogFragment = WaitDialogFragment.getInstance(socketEventModule.getRoomId());
                                    waitDialogFragment.refreshList(homeModel.getRoomUserList());

                                    //如果人数够了， 进入游戏界面
                                    if (homeModel.getRoomUserList().size() == homeModel.getLimitNumber()) {
                                        GameActivity.show(homeModel.getRoomId());
                                    }

                                    //考虑到游戏环节中用户离开、进入
                                    EventBus.getDefault().post(new UserChangeEvent(socketEventModule.getChangeUser(), socketEventModule.getActionType() == ActionType.memberComeIn));
                                    break;
                                case ActionType.memberConfirmStartGame:
                                    //有成员确认开始游戏，统计现实确认游戏成员，直接掠过不处理
                                    break;
                                case ActionType.memberRoleTypeConfirm:
                                    //所有成员角色分配完毕
                                    EventBus.getDefault().post(new RoleTypeConfirm(socketEventModule.getChangeUser()));
                                    //倒计时5秒 确认角色
                                    break;
                                case ActionType.home_come_key:
                                    //所有用户角色确认完毕，进入钥匙房
                                    EventBus.getDefault().post(new GoKeyRoomEvent(socketEventModule.getRoomId(), socketEventModule.getUserModelList()));
                                    break;
                                case ActionType.key_put_end_and_next_speak:
                                    //所有人钥匙投放结束，回到语音房间，进入发言环节
                                    EventBus.getDefault().post(new RebackVoiceRoomEvent(socketEventModule.getUserModelList(), socketEventModule.getRoomModel()));
                                    break;
                                case ActionType.home_normal_speak:
                                    //顺序发言的时候，轮到了我，进入发言环节
                                    EventBus.getDefault().post(new SpeakRoundEvent(socketEventModule.getChangeUser(), socketEventModule.getRoomModel()));
                                    break;
                                case ActionType.home_select_criminal_start:
                                    //进入投票环节
                                    //APP根据 room.roundType 做风格调整
                                    EventBus.getDefault().post(new StartVoteEvent(socketEventModule.getRoomModel()));
                                    break;
                                case ActionType.home_vote_criminal_end:
                                    //投票结束，得到投票结果， 如果出现投票结果的集合》1 则进入平票环节, 由平票的第一个人开始发言【后面可以改一下】
                                    EventBus.getDefault().post(new VoteEndEvent(socketEventModule.getUserModelList(), socketEventModule.getRoomModel()));
                                    break;
                                case ActionType.game_end:
                                    //本局游戏结束
                                    EventBus.getDefault().post(new GameEndEvent(socketEventModule.getRoomModel()));
                                    break;
                            }
                        }
                    }

                    @Override
                    public void socketReceived(byte[] bytes) {
                        EventBus.getDefault().post(bytes);
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
