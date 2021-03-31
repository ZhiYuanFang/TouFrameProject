package xyz.ttyz.tourfrxohc;

import android.Manifest;
import android.content.Intent;
import android.os.Handler;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.rong.imlib.model.Conversation;
import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.mylibrary.method.ProgressUtil;
import xyz.ttyz.mylibrary.method.RetrofitUtils;
import xyz.ttyz.mylibrary.method.RxOHCUtils;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.adapter.utils.BaseEmptyAdapterParent;
import xyz.ttyz.toubasemvvm.adapter.utils.BaseRecyclerAdapter;
import xyz.ttyz.toubasemvvm.utils.DialogUtils;
import xyz.ttyz.toubasemvvm.utils.ToastUtil;
import xyz.ttyz.toubasemvvm.vm.ToolBarViewModel;
import xyz.ttyz.tourfrxohc.activity.BaseActivity;
import xyz.ttyz.tourfrxohc.activity.GameActivity;
import xyz.ttyz.tourfrxohc.databinding.ActivityMainBinding;
import xyz.ttyz.tourfrxohc.fragment.MainFragment;
import xyz.ttyz.tourfrxohc.http.BaseSubscriber;
import xyz.ttyz.tourfrxohc.models.Hardware;
import xyz.ttyz.tourfrxohc.models.MainModel;
import xyz.ttyz.tourfrxohc.models.ResorceModel;
import xyz.ttyz.tourfrxohc.models.Software;
import xyz.ttyz.tourfrxohc.models.UserModel;
import xyz.ttyz.tourfrxohc.utils.RMUtils;
import xyz.ttyz.tourfrxohc.utils.UserUtils;
import xyz.ttyz.tourfrxohc.viewholder.ResorceViewHolder;

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
        RMUtils.connectRM();
        mBinding.setContext(this);
        toolBarViewModel = new ToolBarViewModel.Builder()
                .rightTxt("图片")
                .rightClick(new OnClickAdapter.onClickCommand() {
                    @Override
                    public void click() {
                        DialogUtils.showDialog("点击返回按钮可以关闭图片", new DialogUtils.DialogButtonModule("显示图片", new DialogUtils.DialogClickDelegate() {
                            @Override
                            public void click(DialogUtils.DialogButtonModule dialogButtonModule) {
                                MainFragment mainFragment = new MainFragment();
                                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.add(R.id.container, mainFragment);
                                fragmentTransaction.commitAllowingStateLoss();
                                fragmentTransaction.addToBackStack("");
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
            if(RMUtils.connectSuccess){
                // TODO: 2021/3/31 获取匹配房间
                //匹配机制
                //给后端传当前用户信息，要请求进入房间
                //后端判断当前用户是否已经在房间
                //后端生成房间id
                //当前id下匹配人数达到9个
                //返回匹配成功，并告知房间id
                ProgressUtil.showCircleProgress(ActivityManager.getInstance());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String roomId = "room";
                        GameActivity.show(roomId);
                        ProgressUtil.missCircleProgress();
                    }
                }, 4300);

            } else ToastUtil.showToast("服务器连接失败");
        }
    };
}


