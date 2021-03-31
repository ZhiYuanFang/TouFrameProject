package xyz.ttyz.tourfrxohc.activity;

import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import cn.rongcloud.rtc.api.RCRTCConfig;
import cn.rongcloud.rtc.api.RCRTCEngine;
import cn.rongcloud.rtc.api.RCRTCRemoteUser;
import cn.rongcloud.rtc.api.RCRTCRoom;
import cn.rongcloud.rtc.api.callback.IRCRTCResultCallback;
import cn.rongcloud.rtc.api.callback.IRCRTCResultDataCallback;
import cn.rongcloud.rtc.api.callback.IRCRTCRoomEventsListener;
import cn.rongcloud.rtc.api.stream.RCRTCAudioStreamConfig;
import cn.rongcloud.rtc.api.stream.RCRTCInputStream;
import cn.rongcloud.rtc.api.stream.RCRTCVideoStreamConfig;
import cn.rongcloud.rtc.api.stream.RCRTCVideoView;
import cn.rongcloud.rtc.base.RCRTCParamsType;
import cn.rongcloud.rtc.base.RTCErrorCode;
import io.rong.imlib.model.Conversation;
import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.mylibrary.protect.StringUtil;
import xyz.ttyz.toubasemvvm.utils.DialogUtils;
import xyz.ttyz.toubasemvvm.utils.ToastUtil;
import xyz.ttyz.tourfrxohc.MainActivity;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityGameBinding;
import xyz.ttyz.tourfrxohc.models.PlayStatus;
import xyz.ttyz.tourfrxohc.models.UserModel;
import xyz.ttyz.tourfrxohc.utils.UserUtils;

/**
 * https://docs.rongcloud.cn/v4/views/rtc/meeting/guide/advanced/usermanage/serverapi.html
 */
public class GameActivity extends BaseActivity<ActivityGameBinding> {
    /**
     * 凑齐9个人才能进入该页面
     *
     * @param roomId 房间id
     */
    public static void show(String roomId) {
        if (StringUtil.safeString(roomId).isEmpty()) {
            return;
        }
        Intent intent = new Intent(ActivityManager.getInstance(), GameActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("roomId", roomId);
        ActivityManager.getInstance().startActivity(intent);
    }

    String roomId;
    List<UserModel> userModelList;//9个人的用户信息

    private void bindUser() {
        if (userModelList == null || userModelList.size() < 9) {
            return;
        }
        for (int i = 0; i < 9; i++) {
            UserModel userModel = userModelList.get(i);
            switch (i) {
                case 0:
                    mBinding.setUser1(userModel);
                    break;
                case 1:
                    mBinding.setUser2(userModel);
                    break;
                case 2:
                    mBinding.setUser3(userModel);
                    break;
                case 3:
                    mBinding.setUser4(userModel);
                    break;
                case 4:
                    mBinding.setUser5(userModel);
                    break;
                case 5:
                    mBinding.setUser6(userModel);
                    break;
                case 6:
                    mBinding.setUser7(userModel);
                    break;
                case 7:
                    mBinding.setUser8(userModel);
                    break;
                case 8:
                    mBinding.setUser9(userModel);
                    break;
                default:
            }
        }
        for (UserModel userModel : userModelList) {

        }
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_game;
    }

    @Override
    protected String[] initPermission() {
        return new String[0];
    }

    @Override
    protected void initData() {
        roomId = getIntent().getStringExtra("roomId");
        if (StringUtil.safeString(roomId).isEmpty()) {
            ToastUtil.showToast("房间不存在");
            GameActivity.this.finish();
            return;
        }
        joinRoom();

    }

    //region 麦克风控制
    //通过长连接通知，轮到发言打开麦克风
    private void openMicrophone() {
        RCRTCEngine.getInstance().getDefaultAudioStream().setMicrophoneDisable(true);

        for (UserModel userModel : userModelList) {
            userModel.setSpeaking(UserUtils.getCurUserModel().equals(userModel));//非当前用户为不发言状态
        }
    }

    //发言完毕，关闭麦克风
    private void closeMicrophone() {
        RCRTCEngine.getInstance().getDefaultAudioStream().setMicrophoneDisable(false);
        // TODO: 2021/3/31 关闭麦克风，长连接通知，当前用户结束发言
        UserModel selfUserModel = UserUtils.getCurUserModel();
        userModelList.get(userModelList.indexOf(selfUserModel)).setSpeaking(false);//当前用户为不发言状态
    }
    //endregion

    //加入房间语音
    private void joinRoom() {
        RCRTCConfig config = RCRTCConfig.Builder.create()
                .enableMicrophone(true)//是否启用麦克风，默认：true 不启用麦克风则不创建 AudioRecoder 实例，RTCLib 加入房间 或 CallLib 开始通话 后无法再操作麦克风
                .setAudioBitrate(30)//设置音频码率，默认 30 kbps
                .setAudioSampleRate(16000)//设置音频采样率，支持的音频采样率有：8000，16000， 32000， 44100， 48000。 默认为 48000
                .build();
        RCRTCEngine.getInstance().init(getApplicationContext(), config);
        //噪声抑制
        RCRTCAudioStreamConfig audioStreamConfig = RCRTCAudioStreamConfig.Builder.create()
                .setNoiseSuppression(RCRTCParamsType.NSMode.NS_MODE2)//设置回声消除模式，默认为 AECMode#AEC_MODE2 (使用AEC)
                .setPreAmplifierLevel(1.0f)//设置采集音频信号放大级别， 默认 1.0f
                .enablePreAmplifier(true)//采集音频信号放大开关，默认 true
                .enableAGCControl(true)//设置增益控制开关，默认 true
                .enableEchoFilter(true)//设置回声扩展滤波器是否可用，默认 false
                .build();
        RCRTCEngine.getInstance().getDefaultAudioStream().setAudioConfig(audioStreamConfig);
        //默认关闭麦克风
        closeMicrophone();
        //默认打开扬声器
        RCRTCEngine.getInstance().enableSpeaker(true);
        //mRoomId：长度 64 个字符，可包含：`A-Z`、`a-z`、`0-9`、`+`、`=`、`-`、`_`
        RCRTCEngine.getInstance().joinRoom(roomId, new IRCRTCResultDataCallback<RCRTCRoom>() {
            @Override
            public void onSuccess(RCRTCRoom rcrtcRoom) {
                //注册房间事件回调
                rcrtcRoom.registerRoomListener(new IRCRTCRoomEventsListener() {
                    /**
                     * 房间内用户发布资源
                     *
                     * @param rcrtcRemoteUser 远端用户
                     * @param list    发布的资源
                     */
                    @Override
                    public void onRemoteUserPublishResource(RCRTCRemoteUser rcrtcRemoteUser, List<RCRTCInputStream> list) {
                        //按需在此订阅远端用户发布的资源
                        rcrtcRoom.getLocalUser().subscribeStreams(list, new IRCRTCResultCallback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onFailed(RTCErrorCode rtcErrorCode) {
                            }
                        });
                    }

                    @Override
                    public void onRemoteUserMuteAudio(RCRTCRemoteUser rcrtcRemoteUser, RCRTCInputStream rcrtcInputStream, boolean b) {

                    }

                    @Override
                    public void onRemoteUserMuteVideo(RCRTCRemoteUser rcrtcRemoteUser, RCRTCInputStream rcrtcInputStream, boolean b) {

                    }

                    @Override
                    public void onRemoteUserUnpublishResource(RCRTCRemoteUser rcrtcRemoteUser, List<RCRTCInputStream> list) {

                    }

                    /**
                     * 用户加入房间
                     *
                     * @param rcrtcRemoteUser 远端用户
                     */
                    @Override
                    public void onUserJoined(RCRTCRemoteUser rcrtcRemoteUser) {
                        //获取这个用户的id
                        long userId = Long.parseLong(rcrtcRemoteUser.getUserId());
                        //根据id得到对应的userModel
                        UserModel userModel = getUserModel(userId);
                        //修改userModel下用户是否离开状态
                        userModel.setPlayStatus(PlayStatus.IN);
                    }

                    /**
                     * 用户离开房间
                     *
                     * @param rcrtcRemoteUser 远端用户
                     */
                    @Override
                    public void onUserLeft(RCRTCRemoteUser rcrtcRemoteUser) {
                        //获取这个用户的id
                        long userId = Long.parseLong(rcrtcRemoteUser.getUserId());
                        //根据id得到对应的userModel
                        UserModel userModel = getUserModel(userId);
                        //修改userModel下用户是否离开状态
                        userModel.setPlayStatus(PlayStatus.LEAVE);
                    }

                    @Override
                    public void onUserOffline(RCRTCRemoteUser rcrtcRemoteUser) {
                        //获取这个用户的id
                        long userId = Long.parseLong(rcrtcRemoteUser.getUserId());
                        //根据id得到对应的userModel
                        UserModel userModel = getUserModel(userId);
                        //修改userModel下用户是否离开状态
                        userModel.setPlayStatus(PlayStatus.OffLine);
                    }

                    /**
                     * 自己退出房间。 例如断网退出等
                     * @param i 状态码
                     */
                    @Override
                    public void onLeaveRoom(int i) {
                        // TODO: 2021/3/31 判断当前房间是否已经关闭
                        boolean roomIsClose = false;
                        if (roomIsClose) {
                            GameActivity.this.finish();
                        } else {
                            //当前房间未关闭
                            DialogUtils.showSingleDialog("网络异常退出房间", new DialogUtils.DialogButtonModule("重新连接", new DialogUtils.DialogClickDelegate() {
                                @Override
                                public void click(DialogUtils.DialogButtonModule dialogButtonModule) {
                                    joinRoom();
                                }
                            }));
                        }
                    }
                });
                //加入房间成功后，发布默认音视频流
                rcrtcRoom.getLocalUser().publishDefaultStreams(new IRCRTCResultCallback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailed(RTCErrorCode rtcErrorCode) {
                        DialogUtils.showSingleDialog("麦克风无法正常使用", new DialogUtils.DialogButtonModule("确定"));
                    }
                });
                //加入房间成功后，如果房间中已存在用户且发布了音、视频流，就订阅远端用户发布的音视频流.
                if (rcrtcRoom == null || rcrtcRoom.getRemoteUsers() == null) {
                    return;
                }
                List<RCRTCInputStream> inputStreams = new ArrayList<>();
                for (RCRTCRemoteUser remoteUser : rcrtcRoom.getRemoteUsers()) {
                    if (remoteUser.getStreams().size() == 0) {
                        continue;
                    }
                    inputStreams.addAll(remoteUser.getStreams());
                }
                rcrtcRoom.getLocalUser().subscribeStreams(inputStreams, new IRCRTCResultCallback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailed(RTCErrorCode errorCode) {
                        DialogUtils.showSingleDialog("订阅其他人音频失败", new DialogUtils.DialogButtonModule("确定"));
                    }
                });
            }

            @Override
            public void onFailed(RTCErrorCode rtcErrorCode) {
                DialogUtils.showSingleDialog("加入房间失败", new DialogUtils.DialogButtonModule("确定", new DialogUtils.DialogClickDelegate() {
                    @Override
                    public void click(DialogUtils.DialogButtonModule dialogButtonModule) {
                        GameActivity.this.finish();
                    }
                }));
            }
        });
    }

    @Override
    protected void initServer() {
        //根据roomId 请求接口，获取当前房间人员
        userModelList = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            UserModel userModel = new UserModel();
            userModel.setId(i + 1);
            userModelList.add(userModel);
        }
        //获取到了本房间的所有用户信息，绘制页面
        bindUser();
    }

    /**
     * @param userId 根据用户id获取用户model
     */
    private UserModel getUserModel(long userId) {
        if (userModelList == null || userModelList.isEmpty()) return null;
        for (UserModel userModel : userModelList) {
            if (userModel.getId() == userId) {
                return userModel;
            }
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //离开音视频房间时，SDK 内部会自动取消发布本端资源和取消订阅远端用户资源，必须在成功或失败回调完成之后再开始新的音视频通话逻辑
        RCRTCEngine.getInstance().leaveRoom(new IRCRTCResultCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailed(RTCErrorCode rtcErrorCode) {

            }
        });
    }
}
