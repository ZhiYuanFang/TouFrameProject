package xyz.ttyz.tourfrxohc.activity;

import android.content.Intent;

import androidx.databinding.ObservableField;

import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.mylibrary.method.RxOHCUtils;
import xyz.ttyz.mylibrary.protect.StringUtil;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.ui.BaseTouActivity;
import xyz.ttyz.toubasemvvm.utils.ToastUtil;
import xyz.ttyz.tourfrxohc.BaseApplication;
import xyz.ttyz.tourfrxohc.MainActivity;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityEndChatBinding;
import xyz.ttyz.tourfrxohc.http.BaseSubscriber;
import xyz.ttyz.tourfrxohc.models.game.HomeModel;
import xyz.ttyz.tourfrxohc.utils.DefaultUtils;
import xyz.ttyz.tourfrxohc.utils.HomeUtils;
import xyz.ttyz.tourfrxohc.utils.UserUtils;

/**
 * 结束界面
 */
public class EndChatActivity extends BaseTouActivity<ActivityEndChatBinding> {
    public static void show(){
        Intent intent = new Intent(ActivityManager.getInstance(), EndChatActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        ActivityManager.getInstance().startActivity(intent);
    }

    public ObservableField<Boolean> saveWinFiled = new ObservableField<>(false);

    @Override
    protected int initLayoutId() {
        return R.layout.activity_end_chat;
    }

    @Override
    protected String[] initPermission() {
        return new String[0];
    }

    @Override
    protected void initData() {
        mBinding.setContext(this);

        if (StringUtil.safeString(DefaultUtils.roomId).isEmpty()) {
            ToastUtil.showToast("房间不存在");
            EndChatActivity.this.finish();
        }
    }

    @Override
    protected void initServer() {
        //根据roomId 请求接口，获取当前房间人员
        new RxOHCUtils<HomeModel>(this).executeApi(BaseApplication.apiService.roomInfo(DefaultUtils.roomId), new BaseSubscriber<HomeModel>(this) {
            @Override
            public void success(HomeModel data) {
                saveWinFiled.set(data.isSaveWin());
            }

            @Override
            public String initCacheKey() {
                return null;
            }
        });
    }

    public OnClickAdapter.onClickCommand exitGameCommand = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            new RxOHCUtils<Object>(EndChatActivity.this).executeApi(BaseApplication.apiService.leave(DefaultUtils.roomId, UserUtils.getCurUserModel().getId()), new BaseSubscriber<Object>(EndChatActivity.this) {
                @Override
                public void success(Object data) {
                    //退出房间了, 回到主页
                    MainActivity.show();
                }

                @Override
                public String initCacheKey() {
                    return null;
                }
            });
        }
    };

    public OnClickAdapter.onClickCommand againGameCommand = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            HomeUtils.joinHome(4);
        }
    };
}
