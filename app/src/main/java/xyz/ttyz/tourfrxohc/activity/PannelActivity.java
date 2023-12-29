package xyz.ttyz.tourfrxohc.activity;

import android.content.Intent;

import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityPannelBinding;

/**
 * @author 投投
 * @date 2023/12/26
 * @email 343315792@qq.com
 */
public class PannelActivity extends BaseActivity<ActivityPannelBinding>{

    public static void show(){
        Intent intent = new Intent(ActivityManager.getInstance(), PannelActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        ActivityManager.getInstance().startActivity(intent);
    }
    @Override
    protected int initLayoutId() {
        return R.layout.activity_pannel;
    }

    @Override
    protected String[] initPermission() {
        return new String[0];
    }

    @Override
    protected void initData() {
        mBinding.setContext(this);
    }

    @Override
    protected void initServer() {

    }

    public OnClickAdapter.onClickCommand onClickGet = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            PwdActivity.show(PwdActivity.GET);
        }
    };

    public OnClickAdapter.onClickCommand onClickPut = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            PwdActivity.show(PwdActivity.PUT);
        }
    };

    public OnClickAdapter.onClickCommand onClickSafeOpen = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            PwdSuperActivity.show();
        }
    };
}
