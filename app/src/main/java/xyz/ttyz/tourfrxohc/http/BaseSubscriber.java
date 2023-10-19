package xyz.ttyz.tourfrxohc.http;

import androidx.databinding.ObservableBoolean;

import com.trello.rxlifecycle2.LifecycleProvider;

import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.mylibrary.method.BaseModule;
import xyz.ttyz.mylibrary.method.BaseTouSubscriber;
import xyz.ttyz.mylibrary.method.HttpDefaultUtils;
import xyz.ttyz.mylibrary.method.RecordsModule;
import xyz.ttyz.mylibrary.method.RfRxOHCBaseModule;
import xyz.ttyz.mylibrary.method.RxOHCUtils;
import xyz.ttyz.mylibrary.protect.SharedPreferenceUtil;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.tourfrxohc.BaseApplication;
import xyz.ttyz.tourfrxohc.MainActivity;
import xyz.ttyz.tourfrxohc.activity.LoginActivity;

import static xyz.ttyz.mylibrary.method.HttpDefaultUtils.HTTPCODE;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseSubscriber<D> extends BaseTouSubscriber<D> {

    ObservableBoolean loadEnd;
    public BaseSubscriber(LifecycleProvider lifeCycle) {
        this(lifeCycle, null);
    }
    public BaseSubscriber(LifecycleProvider lifeCycle, ObservableBoolean loadEnd) {
        super(lifeCycle);
        this.loadEnd = loadEnd;
    }

    @Override
    public String initCacheKey() {
        return null;
    }

    @Override
    public void onComplete() {
        super.onComplete();
        if(loadEnd != null)
            loadEnd.set(true);
    }

    @Override
    protected void fail(BaseModule<D> baseModule) {
        switch (baseModule.getCode()){
            case 302://过期
                //登录
                String username = SharedPreferenceUtil.getShareString(ActivityManager.getInstance(), "account");
                String password = SharedPreferenceUtil.getShareString(ActivityManager.getInstance(), "password");
                new RxOHCUtils<>(ActivityManager.getInstance()).executeApi(BaseApplication.apiService.login(username, password), new BaseSubscriber<Boolean>(lifeCycle) {
                    @Override
                    public void success(Boolean data) {
                        //登陆成功 已自动更换cookie

                    }

                    @Override
                    protected void fail(BaseModule<Boolean> baseModule) {
                        super.fail(baseModule);
                        // 登陆失败, 前往登陆界面
                        LoginActivity.toLogin();
                    }
                });
                break;
            case 401://无权限 正常提示即可
            default:
                super.fail(baseModule);
        }
    }


}
