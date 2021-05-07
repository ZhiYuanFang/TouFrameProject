package xyz.ttyz.tourfrxohc.activity;

import android.content.Intent;
import android.os.Handler;

import androidx.databinding.ObservableField;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.mylibrary.method.RxOHCUtils;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.tourfrxohc.BaseApplication;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityLoginBinding;
import xyz.ttyz.tourfrxohc.event.UserChangeEvent;
import xyz.ttyz.tourfrxohc.http.BaseSubscriber;
import xyz.ttyz.tourfrxohc.models.UserModel;
import xyz.ttyz.tourfrxohc.utils.UserUtils;

public class LoginActivity extends BaseActivity<ActivityLoginBinding>{

    public static void show(){
        Intent intent = new Intent(ActivityManager.getInstance(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        ActivityManager.getInstance().startActivity(intent);
    }

    public ObservableField<String> phoneFiled = new ObservableField<>("");
    public ObservableField<String> pwdFiled = new ObservableField<>("");

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void userChange(UserChangeEvent changeEvent){
        phoneFiled.set(changeEvent.getUserModel().getPhone());
    }
    @Override
    protected int initLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected String[] initPermission() {
        return new String[0];
    }

    @Override
    protected void initData() {
        mBinding.setContext(this);
        phoneFiled.set(UserUtils.getHisPhone());
    }

    @Override
    protected void initServer() {

    }

    public OnClickAdapter.onClickCommand clickLogin = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            new RxOHCUtils<UserModel>(LoginActivity.this).executeApi(BaseApplication.apiService.login(phoneFiled.get(), pwdFiled.get()), new BaseSubscriber<UserModel>(LoginActivity.this) {
                @Override
                public void success(UserModel data) {
                    UserUtils.login(data);
                    LoginActivity.this.finish();
                }

                @Override
                public String initCacheKey() {
                    return null;
                }
            });
        }
    };

    public OnClickAdapter.onClickCommand clickRegister = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            RegisterActivity.show(phoneFiled.get());
        }
    };
}
