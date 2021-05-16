package xyz.ttyz.tourfrxohc.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.ObservableBoolean;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import xyz.ttyz.mylibrary.method.RxOHCUtils;
import xyz.ttyz.mylibrary.socket.SocketUtils;
import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.adapter.utils.BaseEmptyAdapterParent;
import xyz.ttyz.toubasemvvm.adapter.utils.BaseRecyclerAdapter;
import xyz.ttyz.toubasemvvm.ui.BaseTouActivity;
import xyz.ttyz.tourfrxohc.BaseApplication;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityRoomWaitBinding;
import xyz.ttyz.tourfrxohc.dialog.WaitDialogFragment;
import xyz.ttyz.tourfrxohc.http.BaseSubscriber;
import xyz.ttyz.tourfrxohc.models.UserModel;
import xyz.ttyz.tourfrxohc.models.game.HomeModel;
import xyz.ttyz.tourfrxohc.utils.DefaultUtils;
import xyz.ttyz.tourfrxohc.utils.HomeUtils;
import xyz.ttyz.tourfrxohc.utils.UserUtils;
import xyz.ttyz.tourfrxohc.viewholder.ViewHolderWaitingPersonItem;

/**
 * 房间等待界面
 */
public class RoomWaitActivity extends BaseTouActivity<ActivityRoomWaitBinding> {
    private static final String TAG = "RoomWaitActivity";

    public static void show() {
        Intent intent = new Intent(ActivityManager.getInstance(), RoomWaitActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        ActivityManager.getInstance().startActivity(intent);
    }

    GridLayoutManager gridLayoutManager;
    BaseEmptyAdapterParent adapterParent;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_room_wait;
    }

    @Override
    protected String[] initPermission() {
        return new String[0];
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initData() {
        gridLayoutManager = new GridLayoutManager(this, 3);
        mBinding.setGridLayoutManager(gridLayoutManager);
        mBinding.setContext(this);
        adapterParent = new BaseEmptyAdapterParent(this, new BaseRecyclerAdapter.NormalAdapterDelegate() {
            @Override
            public int getItemViewType(int position) {
                return 0;
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ViewHolderWaitingPersonItem(RoomWaitActivity.this, parent);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((ViewHolderWaitingPersonItem) holder).bindData((UserModel) adapterParent.getItem(position));
            }
        });
        mBinding.setAdapter(adapterParent);
        mBinding.btnSpeak.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startSpeak();
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                    case MotionEvent.ACTION_UP:
                        endSpeak();
                        break;
                    default:
                }
                return false;
            }
        });
    }

    @Override
    protected void initServer() {
        //获取房间信息
        new RxOHCUtils<HomeModel>(this).executeApi(BaseApplication.apiService.roomInfo(DefaultUtils.roomId), new BaseSubscriber<HomeModel>(this) {
            @Override
            public void success(HomeModel data) {
                mBinding.setHomeModel(data);
                //绘制等待界面
                adapterParent.setList(data.getRoomUserList());
                if (data.getRoomUserList().size() < data.getLimitNumber()) {
                    for (int i = data.getRoomUserList().size(); i < data.getLimitNumber(); i++) {
                        adapterParent.add(new UserModel());
                    }
                }
            }

            @Override
            public String initCacheKey() {
                return null;
            }
        });
    }

    public OnClickAdapter.onClickCommand startGameCommand = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            new RxOHCUtils<HomeModel>(RoomWaitActivity.this).executeApi(BaseApplication.apiService.startCreatedGame(DefaultUtils.roomId), new BaseSubscriber<HomeModel>(RoomWaitActivity.this) {
                @Override
                public void success(HomeModel data) {
                    //成功进入匹配队列
                    if(data.getRoomUserList().size() == data.getLimitNumber()){
                        //直接进入房间
                        GameActivity.show();
                    } else {
                        WaitDialogFragment waitDialogFragment = WaitDialogFragment.getInstance(null);
                        waitDialogFragment.refreshList(data.getRoomUserList());
                        try {
                            waitDialogFragment.show(ActivityManager.getInstance().getSupportFragmentManager());
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }

                @Override
                public String initCacheKey() {
                    return null;
                }
            });
        }
    };

    public OnClickAdapter.onClickCommand leaveCommand = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            //退出
            new RxOHCUtils<Object>(RoomWaitActivity.this).executeApi(BaseApplication.apiService.leaveCreatedRoom(DefaultUtils.roomId, UserUtils.getCurUserModel().getId()), new BaseSubscriber<Object>(RoomWaitActivity.this) {
                @Override
                public void success(Object data) {
                    HomeUtils.outHome();
                    RoomWaitActivity.this.finish();
                }


                @Override
                public String initCacheKey() {
                    return null;
                }
            });

        }
    };


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
    }
    //endregion
}
