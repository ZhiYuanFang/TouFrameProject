package xyz.ttyz.tourfrxohc;

import android.Manifest;
import android.content.Intent;
import android.view.animation.AnimationUtils;

import androidx.databinding.ObservableInt;
import androidx.fragment.app.FragmentTransaction;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.mylibrary.method.BaseModule;
import xyz.ttyz.mylibrary.method.BaseTouSubscriber;
import xyz.ttyz.mylibrary.method.ProgressUtil;
import xyz.ttyz.mylibrary.method.RetrofitUtils;
import xyz.ttyz.mylibrary.method.RxOHCUtils;
import xyz.ttyz.mylibrary.socket.SocketUtils;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.utils.DialogUtils;
import xyz.ttyz.toubasemvvm.utils.NormalImageEngine;
import xyz.ttyz.toubasemvvm.utils.ToastUtil;
import xyz.ttyz.toubasemvvm.utils.TouUtils;
import xyz.ttyz.toubasemvvm.vm.ToolBarViewModel;
import xyz.ttyz.tourfrxohc.activity.BaseActivity;
import xyz.ttyz.tourfrxohc.activity.GameActivity;
import xyz.ttyz.tourfrxohc.activity.RoomWaitActivity;
import xyz.ttyz.tourfrxohc.databinding.ActivityMainBinding;
import xyz.ttyz.tourfrxohc.dialog.CreateHomeDialogFragment;
import xyz.ttyz.tourfrxohc.dialog.EditNickNameDialogFragment;
import xyz.ttyz.tourfrxohc.dialog.JoinHomeDialogFragment;
import xyz.ttyz.tourfrxohc.event.user.UserNickNameChangeEvent;
import xyz.ttyz.tourfrxohc.fragment.MainFragment;
import xyz.ttyz.tourfrxohc.http.BaseSubscriber;
import xyz.ttyz.tourfrxohc.models.UserModel;
import xyz.ttyz.tourfrxohc.models.game.HomeModel;
import xyz.ttyz.tourfrxohc.practice.PracticeRoomThread;
import xyz.ttyz.tourfrxohc.utils.DefaultUtils;
import xyz.ttyz.tourfrxohc.utils.HomeUtils;
import xyz.ttyz.tourfrxohc.utils.UserUtils;

/**
 * V1.0
 * 固定人数匹配机制，默认N = 9
 * 人机练习机制
 *
 * 武器随机掉落，由本轮获胜方随机一人获得
 * 【武器1：可选择一人查看身份】
 * 【武器2：可将自己身份暴露给友方】
 * 【武器3：选择一人斩杀，将其淘汰】
 *
 * 等级积分累计制度
 *
 * Android
 * 首页显示个人头像、积分、昵称、训练场按钮、匹配按钮
 * 点击个人头像可以更换
 * 点击昵称可以修改
 * 点击训练场按钮进入人机模式【机器以文字+语音阅读形式表达】
 * 点击匹配按钮进入匹配等待界面
 * 连接当前房间的长连接
 * 匹配等待界面显示当前处于匹配的人员
 *
 * 匹配成功
 * 进入游戏房间
 * 系统分配身份后显示身份
 * 系统安排进入环节
 * 【环节1：界面等待环节--房间人员座落，并显示可以看见的信息，可以抢麦】
 * 无人抢麦，进入环节2
 * 【环节2：钥匙投放环节--系统安排人员进入钥匙投放，未被安排的人员显示等待】
 * 所有人投票结束进入环节3
 * 【环节3：本轮结算环节--系统统计本轮获胜方，并从获胜方随机选取一人赋予随机武器】
 * 结算次数打到限额，进入环节4
 * 【环节4：无限投票环节，可抢麦，无人抢麦则投票】
 * 最终胜利结算
 * 【环节5：游戏结束，宣布结果，并给出游戏历程】
 * 关闭游戏历程，回到首页，并刷新积分
 * 关闭当前房间的长连接
 * */
public class MainActivity extends BaseActivity<ActivityMainBinding> {

    public static void show() {
        if (!UserUtils.isLogin()) {
            return;
        }
        Intent intent = new Intent(ActivityManager.getInstance(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityManager.getInstance().startActivity(intent);
    }

    public ObservableInt roomLimitNumberFiled = new ObservableInt(4);

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void nickNameChange(UserNickNameChangeEvent userNickNameChangeEvent) {
        mBinding.setUser(UserUtils.getCurUserModel());
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected String[] initPermission() {
        return new String[]{
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};
    }

    @Override
    protected void initData() {
        mBinding.setContext(this);
        //请求接口，获取最新用户信息
        new RxOHCUtils<UserModel>(this).executeApi(BaseApplication.apiService.info(UserUtils.getCurUserModel().getId()), new BaseSubscriber<UserModel>(this) {
            @Override
            public void success(UserModel data) {

                mBinding.setUser(data);

                mBinding.viewStart.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_anim));
            }

            @Override
            public String initCacheKey() {
                return null;
            }
        }.notShowProgress());

    }

    @Override
    protected void initServer() {

    }

    //region Action
    PracticeRoomThread practiceRoomThread;
    //进入训练场
    public OnClickAdapter.onClickCommand practiceCommand = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            GameActivity.show();
            //开启本地线程控制训练场
            practiceRoomThread = new PracticeRoomThread();
        }
    };

    public OnClickAdapter.onClickCommand logOutCommand = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            DialogUtils.showDialog("确定要退出登录吗", new DialogUtils.DialogButtonModule("确定", new DialogUtils.DialogClickDelegate() {
                @Override
                public void click(DialogUtils.DialogButtonModule dialogButtonModule) {
                    UserUtils.logOut();
                }
            }));
        }
    };

    public OnClickAdapter.onClickCommand createHomeCommand = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            //创建房间
            CreateHomeDialogFragment createHomeDialogFragment = new CreateHomeDialogFragment();
            createHomeDialogFragment.show(getSupportFragmentManager());
        }
    };


    public OnClickAdapter.onClickCommand joinHomeCommand = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            // 加入房间
            new JoinHomeDialogFragment().show(getSupportFragmentManager());
        }
    };
    public OnClickAdapter.onClickCommand startGameCommand = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            HomeUtils.joinHome(roomLimitNumberFiled.get());
        }
    };

    public OnClickAdapter.onClickCommand clickNameCommand = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            new EditNickNameDialogFragment().show(getSupportFragmentManager());
        }
    };

    public OnClickAdapter.onClickCommand clickReduceCommand = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            if (roomLimitNumberFiled.get() > DefaultUtils.roomLimitMinNumber) {
                roomLimitNumberFiled.set(roomLimitNumberFiled.get() - 1);
            } else {
                ToastUtil.showToast("最少" + DefaultUtils.roomLimitMinNumber + "人");
            }
        }
    };


    public OnClickAdapter.onClickCommand clickAddCommand = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            if (roomLimitNumberFiled.get() < DefaultUtils.roomLimitMaxNumber) {
                roomLimitNumberFiled.set(roomLimitNumberFiled.get() + 1);
            } else {
                ToastUtil.showToast("最多" + DefaultUtils.roomLimitMaxNumber + "人");
            }
        }
    };
    public OnClickAdapter.onClickCommand clickAvatarCommand = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            //点击头像， 触发选择1拍照 2相册选择图片 3头像框--衍生服务
            //S1 直接进入相册选择
            PictureSelector.create(MainActivity.this)
                    .openGallery(PictureMimeType.ofImage())
                    .imageEngine(new NormalImageEngine())
//                    .theme(R.style.picture_custom_style)
                    .maxSelectNum(1)
                    .minSelectNum(1)
                    .previewImage(true)
                    .enablePreviewAudio(false)
                    .isCamera(true)
                    .enableCrop(true)
                    .circleDimmedLayer(true)
                    .showCropFrame(false)
                    .showCropGrid(false)
                    .compress(true)
                    .withAspectRatio(1, 1)
                    .minimumCompressSize(100)
                    .freeStyleCropEnabled(true)
                    .rotateEnabled(false)
                    .forResult(PictureConfig.CHOOSE_REQUEST);
        }
    };
    //endregion

    //region pic select
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == RESULT_OK && requestCode == PictureConfig.CHOOSE_REQUEST) {
            ArrayList<String> selectPics = new ArrayList<>();
            TouUtils.filterResSelectResult(data, selectPics);
            if (!selectPics.isEmpty()) {
                //选择图片
                //图片地址
                String filePath = selectPics.get(0);

                new RxOHCUtils<String>(this).executeApi(BaseApplication.apiService.picUp(RetrofitUtils.getFileBody(this, "img", new File(filePath))), new BaseSubscriber<String>(this) {
                    @Override
                    public void success(String picUrl) {
                        //图片上传成功
                        //修改用户头像信息
                        new RxOHCUtils<UserModel>(MainActivity.this).executeApi(BaseApplication.apiService.updateAvatar(UserUtils.getCurUserModel().getPhone(), UserUtils.getCurUserModel().getPassword(), picUrl), new BaseSubscriber<UserModel>(MainActivity.this) {
                            @Override
                            public void success(UserModel data) {
                                ToastUtil.showToast("头像修改成功");
                                UserUtils.getCurUserModel().setAvatar(data.getAvatar());
                                mBinding.setUser(UserUtils.getCurUserModel());
                            }

                            @Override
                            public String initCacheKey() {
                                return null;
                            }
                        });
                    }

                    @Override
                    public String initCacheKey() {
                        return null;
                    }
                });
            }
        }
    }
    //endregion


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(practiceRoomThread != null){
            practiceRoomThread.gameEnd();
        }
    }
}


