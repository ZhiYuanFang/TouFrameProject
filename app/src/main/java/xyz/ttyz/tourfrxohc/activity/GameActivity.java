package xyz.ttyz.tourfrxohc.activity;

import android.Manifest;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.fragment.app.FragmentTransaction;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.mylibrary.method.RxOHCUtils;
import xyz.ttyz.mylibrary.protect.StringUtil;
import xyz.ttyz.mylibrary.socket.SocketUtils;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.utils.DialogUtils;
import xyz.ttyz.toubasemvvm.utils.ToastUtil;
import xyz.ttyz.tourfrxohc.BaseApplication;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityGameBinding;
import xyz.ttyz.tourfrxohc.dialog.WaitDialogFragment;
import xyz.ttyz.tourfrxohc.event.GameEndEvent;
import xyz.ttyz.tourfrxohc.event.GoKeyRoomEvent;
import xyz.ttyz.tourfrxohc.event.KeyPuttingEvent;
import xyz.ttyz.tourfrxohc.event.RoleTypeConfirm;
import xyz.ttyz.tourfrxohc.event.SpeakEvent;
import xyz.ttyz.tourfrxohc.event.StartVoteEvent;
import xyz.ttyz.tourfrxohc.event.UserChangeEvent;
import xyz.ttyz.tourfrxohc.event.UserPaddleEvent;
import xyz.ttyz.tourfrxohc.event.VoteEndEvent;
import xyz.ttyz.tourfrxohc.event.WaitGetVoiceEvent;
import xyz.ttyz.tourfrxohc.fragment.RoleFragment;
import xyz.ttyz.tourfrxohc.fragment.WaitKeyRoomOperatorFragment;
import xyz.ttyz.tourfrxohc.http.BaseSubscriber;
import xyz.ttyz.tourfrxohc.models.UserModel;
import xyz.ttyz.tourfrxohc.models.game.HomeModel;
import xyz.ttyz.tourfrxohc.utils.DefaultUtils;
import xyz.ttyz.tourfrxohc.utils.UserUtils;

/**
 * https://docs.rongcloud.cn/v4/views/rtc/meeting/guide/advanced/usermanage/serverapi.html
 */
public class GameActivity extends BaseActivity<ActivityGameBinding> {
    private static final int VoteLongTime = 10;//秒
    private static final String TAG = "GameActivity";
    public static boolean confirmLeave = false;
    List<UserModel> userModelList;//9个人的用户信息
    public ObservableInt timeCountDownFiled = new ObservableInt(0);
    public ObservableBoolean isVoteRoundFiled = new ObservableBoolean(false);
    public ObservableBoolean isWaitGetVoiceFiled = new ObservableBoolean(false);
    private HomeModel homeModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBackEnable(false);
        WaitDialogFragment.getInstance(null).dismiss();
    }

    /**
     * 凑齐9个人才能进入该页面
     *
     */
    public static void show() {
        if (StringUtil.safeString(DefaultUtils.roomId).isEmpty()) {
            return;
        }
        confirmLeave = false;
        Intent intent = new Intent(ActivityManager.getInstance(), GameActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        ActivityManager.getInstance().startActivity(intent);
    }

    //人员离线 返回
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void userChange(UserChangeEvent userChangeEvent) {
        UserModel userModel = userChangeEvent.getUserModel();
        userModelList.get(userModelList.indexOf(userModel)).setInHome(userChangeEvent.isComeIn());
    }

    //所有成员角色分配完毕
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void roleTypeGive(RoleTypeConfirm roleTypeConfirm) {
        mBinding.viewWait.setVisibility(View.GONE);
        //显示角色给用户 提供确认
        showRoleType(roleTypeConfirm.getSelfUserModel().isSaveType());
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void goToKeyRoom(GoKeyRoomEvent goKeyRoomEvent) {
        //判断我需不需要进入钥匙房间
        if (goKeyRoomEvent.getGoKeyUser().equals(UserUtils.getCurUserModel())) {

            KeyActivity.show(goKeyRoomEvent.getRoomId());
        } else {
            //我不需要进入
            showWaitKeyRoom(DefaultUtils.backPutTime);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void keyPutting(KeyPuttingEvent keyPuttingEvent){
        showWaitKeyRoom(keyPuttingEvent.getCountDownTime());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void paddle(UserPaddleEvent paddleEvent){
        if(paddleEvent.isDead()){
            //死亡
            initServer();
        } else {
            if(paddleEvent.getUser().equals(UserUtils.getCurUserModel())){
                //是我
                DialogUtils.showSingleDialog("警告","划水警告，下次淘汰", new DialogUtils.DialogButtonModule("确定"));
            }
        }

    }

    /**
     * 用户发言倒计时
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void speak(SpeakEvent speakEvent) {
        isWaitGetVoiceFiled.set(false);
        if(speakEvent.getStatus() == 0){
            //正在发言
            timeCountDownFiled.set(speakEvent.getCountDownTime());
        } else {
            //发言结束
            endSpeak();
        }
    }

    /**
     * 等待用户抢麦状态
     * */
    public void waitGet(WaitGetVoiceEvent waitGetVoiceEvent){
        timeCountDownFiled.set(waitGetVoiceEvent.getCountDownTime());
        isWaitGetVoiceFiled.set(true);
    }

    //投票环节
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void voteRound(StartVoteEvent startVoteEvent) {
        homeModel = startVoteEvent.getHomeModel();
        //重置所有人的状态
        List<UserModel> pkList = homeModel.getPkMemberList();
        boolean isPk = pkList != null && !pkList.isEmpty();


            for (UserModel cur : userModelList) {
                cur.setSpeaking(false);
                cur.setVoted(false);
                cur.setCanBeVoted(!isPk || pkList.contains(cur));
            }

        //开始投票
        startVote();
    }

    /**
     * 投票结束
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void voteEnd(VoteEndEvent voteEndEvent) {
        //投票结束，轮次判断判断
        HomeModel homeModel = voteEndEvent.getHomeModel();
        if (homeModel != null) {
            if (voteEndEvent.getPkList() != null && !voteEndEvent.getPkList().isEmpty()) {
                //是平票
                //重置所有人的状态
                List<UserModel> pkList = voteEndEvent.getPkList();


                    for (UserModel cur : userModelList) {
                        cur.setSpeaking(false);
                        cur.setVoted(false);
                        cur.setCanBeVoted(pkList.contains(cur));
                    }

                //平票者开始发言
                //判断我是不是平票者的第一个人
                if (pkList.get(0).equals(UserUtils.getCurUserModel())) {
                    //我是
                    startSpeak();
                } else {
                    //我此时不需要发言，只需要安静等待
                }
            } else {
                //不是平票，有人被选举
                //刷新所有人状态
                userModelList = homeModel.getRoomUserList();
                bindUser();
            }

        }
    }


    /**
     * 游戏结束
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void gameEnd(GameEndEvent gameEndEvent) {
        EndChatActivity.show();
        finish();
    }

    //region 投票
    Disposable voteDisposable;

    private void startVote() {
        isVoteRoundFiled.set(true);
        timeCountDownFiled.set(VoteLongTime);
        voteDisposable = Observable.interval(0, 1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        timeCountDownFiled.set(timeCountDownFiled.get() - 1);
                        if (timeCountDownFiled.get() < 0) {
                            endVote(null);
                        }
                    }
                });
    }

    /**
     * 确认投票对象
     *
     * @param voteUser 被投票者
     */
    private void endVote(UserModel voteUser) {
        isVoteRoundFiled.set(false);
        if (voteDisposable != null && !voteDisposable.isDisposed()) {
            voteDisposable.dispose();
            voteDisposable = null;
        }


        new RxOHCUtils<Object>(GameActivity.this).executeApi(BaseApplication.apiService.vote(DefaultUtils.roomId, UserUtils.getCurUserModel().getId(), voteUser != null ? voteUser.getId() : 0), new BaseSubscriber<Object>(this) {
            @Override
            public void success(Object data) {
                //成功投票， UI变化
                if (voteUser != null) {
                    for (UserModel cur : userModelList) {
                        cur.setVoted(cur.equals(voteUser));
                    }
                }
            }

            @Override
            public String initCacheKey() {
                return null;
            }
        });
    }


    //点击用户头像
    public void clickBody(UserModel user) {
        if (homeModel != null && isVoteRoundFiled.get()) {
            //获取当前房间可以被投票的集合，可能出现平票导致的二次投票
            //推送过来是有房间信息的
            //如果是投票阶段
            UserModel userModel = homeModel.getRoomUserList().get(homeModel.getRoomUserList().indexOf(user));
            //获取投票对象集合
            List<UserModel> voteList;
            if (homeModel.getPkMemberList() != null) {
                voteList = homeModel.getPkMemberList();
            } else {
                voteList = homeModel.getRoomUserList();
            }
            if (!voteList.contains(userModel)) {
                //当前对象不在投票列表中
                //不可对它投票
            } else {
                //当前对象投票生效
                endVote(user);
            }
        } else {
            //查看用户信息
            ToastUtil.showToast(user.getNickname());
        }

    }
    //endregion

    //region 播放音频
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void comingVoice(byte[] bytes) {
        playAudio(bytes);//仅仅负责播放音频
    }

    AudioTrack audioTrack;
    final int playBuffsize = AudioTrack.getMinBufferSize(44100, AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT);
    public ObservableBoolean playAudioFiled = new ObservableBoolean(false);

    public void playAudio(byte[] bytes) {
        Log.i(TAG, "playAudio: " + bytes);
        if (bytes == null) return;
        if (audioTrack == null) {
            audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT, playBuffsize, AudioTrack.MODE_STREAM);
            audioTrack.play();
            playAudioFiled.set(true);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                audioTrack.write(bytes, 0, bytes.length);
            }
        }).start();
    }

    public void stopAudio() {
        Log.i(TAG, "stopAudio: ");
        if (audioTrack != null) {
            playAudioFiled.set(false);
            audioTrack.stop();
            audioTrack.release();
            audioTrack = null;
        }
    }
    //endregion

    //region 音频
    public ObservableBoolean isSpeakingFiled = new ObservableBoolean(false);
    //指定音频源 这个和MediaRecorder是相同的 MediaRecorder.AudioSource.MIC指的是麦克风
    private static final int mAudioSource = MediaRecorder.AudioSource.MIC;
    //指定采样率 （MediaRecoder 的采样率通常是8000Hz AAC的通常是44100Hz。 设置采样率为44100，目前为常用的采样率，官方文档表示这个值可以兼容所有的设置）
    private static final int mSampleRateInHz = 44100;
    //指定捕获音频的声道数目。在AudioFormat类中指定用于此的常量
    private static final int mChannelConfig = AudioFormat.CHANNEL_IN_MONO; //单声道CHANNEL_IN_MONO
    //指定音频量化位数 ,在AudioFormaat类中指定了以下各种可能的常量。通常我们选择ENCODING_PCM_16BIT和ENCODING_PCM_8BIT PCM代表的是脉冲编码调制，它实际上是原始音频样本。
    //因此可以设置每个样本的分辨率为16位或者8位，16位将占用更多的空间和处理能力,表示的音频也更加接近真实。
    private static final int mAudioFormat = AudioFormat.ENCODING_PCM_16BIT;
    //指定缓冲区大小。调用AudioRecord类的getMinBufferSize方法可以获得。
    private final int mBufferSizeInBytes = AudioRecord.getMinBufferSize(mSampleRateInHz, mChannelConfig, mAudioFormat);//计算最小缓冲区
    AudioRecord audioRecord;


    protected void startSpeak() {
        if (audioRecord == null) {
            //创建AudioRecord。AudioRecord类实际上不会保存捕获的音频，因此需要手动创建文件并保存下载。
            audioRecord = new AudioRecord(mAudioSource, mSampleRateInHz, mChannelConfig,
                    mAudioFormat, mBufferSizeInBytes);//创建AudioRecorder对象
        }
        audioRecord.startRecording();
        isSpeakingFiled.set(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isSpeakingFiled.get()) {
                    // 上传音频流
                    byte[] recordData = new byte[mBufferSizeInBytes];
                    audioRecord.read(recordData, 0, mBufferSizeInBytes);


//                    SocketUtils.sendMessage(new Gson().toJson(socketEventModule));//发送告知，谁在说话
                    SocketUtils.sendMessage(recordData);//发送语音
                }
            }
        }).start();
    }

    protected void endSpeak() {
        if (audioRecord != null) {
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        }
        isSpeakingFiled.set(false);
        //告知后台， 我发言完毕，后台自行切换人员并做出发言通知和轮次判断
        new RxOHCUtils<Object>(this).executeApi(BaseApplication.apiService.speakEnd(DefaultUtils.roomId, UserUtils.getCurUserModel().getId()), new BaseSubscriber<Object>(this) {
            @Override
            public void success(Object data) {

            }

            @Override
            public String initCacheKey() {
                return null;
            }
        });
    }
    //endregion

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
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_game;
    }

    @Override
    protected String[] initPermission() {
        return new String[]{
                Manifest.permission.RECORD_AUDIO
        };
    }

    @Override
    protected void initData() {
        mBinding.setContext(this);
        if (StringUtil.safeString(DefaultUtils.roomId).isEmpty()) {
            ToastUtil.showToast("房间不存在");
            GameActivity.this.finish();
        }
    }


    @Override
    protected void initServer() {

        //根据roomId 请求接口，获取当前房间人员
        new RxOHCUtils<HomeModel>(this).executeApi(BaseApplication.apiService.roomInfo(DefaultUtils.roomId), new BaseSubscriber<HomeModel>(this) {
            @Override
            public void success(HomeModel data) {
                userModelList = data.getRoomUserList();
                //获取到了本房间的所有用户信息，绘制页面
                bindUser();
            }

            @Override
            public String initCacheKey() {
                return null;
            }
        });
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
        //退出游戏环节， 退出长连接
        SocketUtils.closeMinaReceiver(getApplication());
    }

    @Override
    public void onBackPressed() {
        //不支持常规退出
    }

    //region server
    private void confirmStartGame() {

    }
    //endregion

    //region action
    public OnClickAdapter.onClickCommand exitGameCommand = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            DialogUtils.showDialog("确定要退出游戏嘛？", new DialogUtils.DialogButtonModule("确定", new DialogUtils.DialogClickDelegate() {
                @Override
                public void click(DialogUtils.DialogButtonModule dialogButtonModule) {
                    new RxOHCUtils<Object>(GameActivity.this).executeApi(BaseApplication.apiService.leave(DefaultUtils.roomId, UserUtils.getCurUserModel().getId()), new BaseSubscriber<Object>(GameActivity.this) {
                        @Override
                        public void success(Object data) {
                            GameActivity.this.finish();
                        }

                        @Override
                        public String initCacheKey() {
                            return null;
                        }
                    });
                }
            }));
        }
    };


    public OnClickAdapter.onClickCommand endSpeakCommand = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            endSpeak();

        }
    };

    public OnClickAdapter.onClickCommand wantVoiceCommand = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            new RxOHCUtils<>(GameActivity.this).executeApi(BaseApplication.apiService.wantSpeak(DefaultUtils.roomId, UserUtils.getCurUserModel().getId()), new BaseSubscriber<Object>(GameActivity.this) {
                @Override
                public String initCacheKey() {
                    return null;
                }

                @Override
                public void success(Object data) {
                    initServer();
                }
            });
        }
    };
    //endregion

    //region public
    RoleFragment mainFragment;

    public void showRoleType(boolean isSaveType){
        mainFragment = new RoleFragment(isSaveType);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container, mainFragment);
        fragmentTransaction.commitAllowingStateLoss();
        fragmentTransaction.addToBackStack("");
    }

    public void missRoleType() {
        if (mainFragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.remove(mainFragment);
            fragmentTransaction.commit();
            mainFragment = null;
        }
    }

    WaitKeyRoomOperatorFragment waitKeyRoomOperatorFragment;

    public void showWaitKeyRoom(int backTime) {
        if(waitKeyRoomOperatorFragment == null){
            waitKeyRoomOperatorFragment = new WaitKeyRoomOperatorFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.container, waitKeyRoomOperatorFragment);
            fragmentTransaction.commit();
            fragmentTransaction.addToBackStack("");
        }
        waitKeyRoomOperatorFragment.resetTime(backTime);
    }

    public void missWaitKeyRoom() {
        if (null != waitKeyRoomOperatorFragment) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.remove(waitKeyRoomOperatorFragment);
            fragmentTransaction.commit();
            waitKeyRoomOperatorFragment = null;
        }
    }
    //endregion
}
