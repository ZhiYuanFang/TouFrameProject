package xyz.ttyz.tourfrxohc;

import android.Manifest;
import android.content.Intent;

import androidx.fragment.app.FragmentTransaction;

import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.mylibrary.method.BaseModule;
import xyz.ttyz.mylibrary.method.ProgressUtil;
import xyz.ttyz.mylibrary.method.RxOHCUtils;
import xyz.ttyz.mylibrary.socket.SocketUtils;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.utils.DialogUtils;
import xyz.ttyz.toubasemvvm.vm.ToolBarViewModel;
import xyz.ttyz.tourfrxohc.activity.BaseActivity;
import xyz.ttyz.tourfrxohc.activity.GameActivity;
import xyz.ttyz.tourfrxohc.databinding.ActivityMainBinding;
import xyz.ttyz.tourfrxohc.fragment.MainFragment;
import xyz.ttyz.tourfrxohc.http.BaseSubscriber;
import xyz.ttyz.tourfrxohc.models.game.HomeModel;
import xyz.ttyz.tourfrxohc.utils.UserUtils;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    public static void show(){
        if(!UserUtils.isLogin()){
            return;
        }
        Intent intent = new Intent(ActivityManager.getInstance(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityManager.getInstance().startActivity(intent);
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

    //标题栏
    ToolBarViewModel toolBarViewModel;
    @Override
    protected void initData() {
        mBinding.setContext(this);
        toolBarViewModel = new ToolBarViewModel.Builder()
                .rightTxt("退出")
                .rightClick(new OnClickAdapter.onClickCommand() {
                    @Override
                    public void click() {
                        DialogUtils.showDialog("确定要退出登录吗", new DialogUtils.DialogButtonModule("确定", new DialogUtils.DialogClickDelegate() {
                            @Override
                            public void click(DialogUtils.DialogButtonModule dialogButtonModule) {
//                                MainFragment mainFragment = new MainFragment();
//                                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                                fragmentTransaction.add(R.id.container, mainFragment);
//                                fragmentTransaction.commitAllowingStateLoss();
//                                fragmentTransaction.addToBackStack("");

                                UserUtils.logOut();
                            }
                        }));
                    }
                })
                .build();
        mBinding.setToolBarViewModel(toolBarViewModel);
    }

    @Override
    protected void initServer() {

    }

    public OnClickAdapter.onClickCommand startGameCommand = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            ProgressUtil.showCircleProgress(ActivityManager.getInstance(), "正在匹配中...");
            new RxOHCUtils<HomeModel>(MainActivity.this).executeApi(BaseApplication.apiService.join(UserUtils.getCurUserModel().getId()), new BaseSubscriber<HomeModel>(MainActivity.this) {
                @Override
                public void success(HomeModel data) {
                    SocketUtils.closeMinaReceiver(getApplication());
                    //为了让socket附带房间信息
                    SocketUtils.openMinaReceiver(getApplication(), new SocketUtils.SocketDelegate() {
                        @Override
                        public void connectSuccess() {
                            if(data.getRoomUserList().size() == data.getLimitNumber()){//我在房间里， 且人数==getLimitNumber
                                //我已经在房间里了
                                ProgressUtil.missCircleProgress();
                                GameActivity.show(data.getRoomId());
                            } else {
                                if(data.getRoomUserList().size() > data.getLimitNumber()){
                                    DialogUtils.showSingleDialog("房间人数超出预期", new DialogUtils.DialogButtonModule("确定"));
                                } else {
                                    //正在匹配中
                                }
                            }
                        }

                        @Override
                        public long roomId() {
                            return data.getRoomId();
                        }

                        @Override
                        public long userId() {
                            return UserUtils.getCurUserModel().getId();
                        }
                    });
                }

                @Override
                protected boolean autoCloseCircleProgress() {
                    return false;
                }

                @Override
                public String initCacheKey() {
                    return null;
                }

                @Override
                protected void fail(BaseModule<HomeModel> baseModule) {
                    super.fail(baseModule);
                    ProgressUtil.missCircleProgress();
                }
            }.notShowProgress());
        }
    };
}


