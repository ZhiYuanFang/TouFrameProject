package xyz.ttyz.tourfrxohc.activity;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.trello.rxlifecycle2.LifecycleTransformer;

import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.mylibrary.protect.SharedPreferenceUtil;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityResetSuperPwdBinding;
import xyz.ttyz.tourfrxohc.utils.Constans;

/**
 * @author 投投
 * @date 2023/12/26
 * @email 343315792@qq.com
 * 重制应急开箱密码
 */
public class ResetSuperPwdActivity extends BaseActivity<ActivityResetSuperPwdBinding>{
    public static void show(){
        SharedPreferenceUtil.setShareString(ActivityManager.getInstance(), Constans.SuperOpenDoorPwdKey, "");
        Intent intent = new Intent(ActivityManager.getInstance(), ResetSuperPwdActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        ActivityManager.getInstance().startActivity(intent);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_reset_super_pwd;
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

    public OnClickAdapter.onClickCommand onClickReset = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            SharedPreferenceUtil.setShareString(ActivityManager.getInstance(), Constans.SuperOpenDoorPwdKey, "1234");
            PannelActivity.show();
        }
    };
}
