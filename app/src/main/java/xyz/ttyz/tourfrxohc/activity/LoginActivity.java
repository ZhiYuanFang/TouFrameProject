package xyz.ttyz.tourfrxohc.activity;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import androidx.databinding.ObservableField;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.plugins.RxJavaPlugins;
import retrofit2.Response;
import retrofit2.adapter.rxjava2.HttpException;
import xyz.ttyz.mylibrary.method.BaseModule;
import xyz.ttyz.mylibrary.method.RetrofitUtils;
import xyz.ttyz.mylibrary.method.RfRxOHCBaseModule;
import xyz.ttyz.mylibrary.method.RxOHCUtils;
import xyz.ttyz.mylibrary.protect.SharedPreferenceUtil;
import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.utils.ToastUtil;
import xyz.ttyz.toubasemvvm.utils.TouUtils;
import xyz.ttyz.tourfrxohc.BaseApplication;
import xyz.ttyz.tourfrxohc.DefaultUtils;
import xyz.ttyz.tourfrxohc.MainActivity;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityLoginBinding;
import xyz.ttyz.tourfrxohc.http.BaseSubscriber;
import xyz.ttyz.tourfrxohc.models.MainModel;
import xyz.ttyz.tourfrxohc.models.UserModel;

/**
 * @author 投投
 * @date 2023/10/12
 * @email 343315792@qq.com
 */
public class LoginActivity extends BaseActivity<ActivityLoginBinding>{
    public static void toLogin(){
        Intent intent = new Intent(ActivityManager.getInstance(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityManager.getInstance().startActivity(intent);
    }
    public ObservableField<String> account = new ObservableField<>("mahe");
    public ObservableField<String> password = new ObservableField<>("111111");

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
    }

    @Override
    protected void initServer() {

    }

    public OnClickAdapter.onClickCommand loginClick = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            //登录
            Map map = new HashMap();
            map.put("username", account.get());
            map.put("password", password.get());
            new RxOHCUtils<>(LoginActivity.this).executeApi(BaseApplication.apiService.login(map), new BaseSubscriber<Boolean>(LoginActivity.this) {
                @Override
                public void success(Boolean data) {
                    SharedPreferenceUtil.setShareString(LoginActivity.this, "account", account.get());
                    SharedPreferenceUtil.setShareString(LoginActivity.this, "password", password.get());
                   MainActivity.goMain();
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                    onComplete();
                    ToastUtil.showToast("登录失败，用户名或密码不正确");
                }
            });
        }
    };
}
