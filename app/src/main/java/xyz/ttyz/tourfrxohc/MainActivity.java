package xyz.ttyz.tourfrxohc;

import android.Manifest;
import android.content.Intent;

import androidx.fragment.app.FragmentTransaction;

import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.mylibrary.method.BaseModule;
import xyz.ttyz.mylibrary.method.ProgressUtil;
import xyz.ttyz.mylibrary.method.RxOHCUtils;
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
//            if(RMUtils.connectSuccess){
                // 获取匹配房间
                //匹配机制
                //给后端传当前用户信息，要请求进入房间
                //后端判断当前用户是否已经在房间
                //后端生成房间id
                //当前id下匹配人数达到9个
                //返回匹配成功，并告知房间id
            ProgressUtil.showCircleProgress(ActivityManager.getInstance(), "正在匹配中...");
                new RxOHCUtils<HomeModel>(MainActivity.this).executeApi(BaseApplication.apiService.join(UserUtils.getCurUserModel().getId()), new BaseSubscriber<HomeModel>(MainActivity.this) {
                    @Override
                    public void success(HomeModel data) {
                        if(data.isInHome() && data.getHisMemberList().size() >= 9){//我在房间里， 且人数>9
                            //我已经在房间里了
                            ProgressUtil.missCircleProgress();
                            GameActivity.show(data.getRoomId());
                        } else {
                            //我不在房间里，挂起
                        }
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

//            } else {
//                ToastUtil.showToast("服务器连接失败");
//            }
        }
    };
}


