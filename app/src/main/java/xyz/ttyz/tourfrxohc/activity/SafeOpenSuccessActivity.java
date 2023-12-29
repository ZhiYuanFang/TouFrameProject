package xyz.ttyz.tourfrxohc.activity;

import android.content.Intent;

import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivitySafeOpenSuccessBinding;

/**
 * @author 投投
 * @date 2023/12/26
 * @email 343315792@qq.com
 */
public class SafeOpenSuccessActivity extends BaseActivity<ActivitySafeOpenSuccessBinding>{
    public static void show(){
        Intent intent = new Intent(ActivityManager.getInstance(), SafeOpenSuccessActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityManager.getInstance().startActivity(intent);
    }
    @Override
    protected int initLayoutId() {
        return R.layout.activity_safe_open_success;
    }

    @Override
    protected String[] initPermission() {
        return new String[0];
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initServer() {

    }
}
