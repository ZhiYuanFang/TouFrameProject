package xyz.ttyz.tourfrxohc.activity;

import android.Manifest;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import androidx.databinding.ObservableBoolean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.mylibrary.method.RxOHCUtils;
import xyz.ttyz.mylibrary.protect.StringUtil;
import xyz.ttyz.mylibrary.socket.SocketUtils;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.event.NetEvent;
import xyz.ttyz.toubasemvvm.utils.DialogUtils;
import xyz.ttyz.toubasemvvm.utils.FileUtil;
import xyz.ttyz.toubasemvvm.utils.ToastUtil;
import xyz.ttyz.tourfrxohc.BaseApplication;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityGameBinding;
import xyz.ttyz.tourfrxohc.event.VoiceEvent;
import xyz.ttyz.tourfrxohc.http.BaseSubscriber;
import xyz.ttyz.tourfrxohc.models.UserModel;
import xyz.ttyz.tourfrxohc.models.game.HomeModel;
import xyz.ttyz.tourfrxohc.models.game.VoiceModel;
import xyz.ttyz.tourfrxohc.utils.PcmToWavUtil;

/**
 * https://docs.rongcloud.cn/v4/views/rtc/meeting/guide/advanced/usermanage/serverapi.html
 */
public class GameActivity extends BaseActivity<ActivityGameBinding> {
    private static final String TAG = "GameActivity";
    public static boolean confirmLeave = false;
    public static long roomId;

    /**
     * 凑齐9个人才能进入该页面
     *
     * @param roomId 房间id
     */
    public static void show(long roomId) {
        if (StringUtil.safeString(roomId).isEmpty()) {
            return;
        }
        confirmLeave = false;
        Intent intent = new Intent(ActivityManager.getInstance(), GameActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        GameActivity.roomId = roomId;
        ActivityManager.getInstance().startActivity(intent);
    }

    List<UserModel> userModelList;//9个人的用户信息

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void comingVoice(VoiceModel voiceModel) {
        if(voiceModel.getSys() != null){
            //系统播报
            playAudio(voiceModel.getSys().getVoiceBytes());
        } else {
            //播放玩家语音
            playAudio(voiceModel.getUser().getVoiceBytes());
            //界面绘制
            for(UserModel user : userModelList){
                user.setSpeaking(voiceModel.getUser().equals(user));
            }
        }
    }

    //region 播放音频
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
        roomId = getIntent().getIntExtra("roomId", 0);
        if (StringUtil.safeString(roomId).isEmpty()) {
            ToastUtil.showToast("房间不存在");
            GameActivity.this.finish();
            return;
        }

    }


    @Override
    protected void initServer() {
        //根据roomId 请求接口，获取当前房间人员
        new RxOHCUtils<HomeModel>(this).executeApi(BaseApplication.apiService.roomInfo(roomId), new BaseSubscriber<HomeModel>(this) {
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

    public OnClickAdapter.onClickCommand endSpeakCommand = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            endSpeak();

        }
    };

    public OnClickAdapter.onClickCommand startSpeakCommand = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            startSpeak();
        }
    };

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
                while (isSpeakingFiled.get()){
                    // 上传音频流
                    byte[] recordData = new byte[mBufferSizeInBytes];
                    audioRecord.read(recordData, 0, mBufferSizeInBytes);
                    SocketUtils.sendMessage(recordData);
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
    }
    //endregion

    public void testEvent() {
        EventBus.getDefault().post(new VoiceEvent(new byte[]{}));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //离开音视频房间时，SDK 内部会自动取消发布本端资源和取消订阅远端用户资源，必须在成功或失败回调完成之后再开始新的音视频通话逻辑
    }
}
