package xyz.ttyz.tourfrxohc.activity;

import xyz.ttyz.tourfrxohc.MainActivity;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivitySplashBinding;
import xyz.ttyz.tourfrxohc.utils.UserUtils;

public class SplashActivity extends BaseActivity<ActivitySplashBinding> {
    @Override
    protected int initLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected String[] initPermission() {
        return new String[0];
    }

    @Override
    protected void initData() {
        if(UserUtils.isLogin()){
            MainActivity.show();
        } else {
            LoginActivity.show();
        }
        finish();
    }

    @Override
    protected void initServer() {

    }
}
