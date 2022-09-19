package xyz.ttyz.tourfrxohc.activity;

import android.content.Intent;

import androidx.databinding.ObservableField;

import xyz.ttyz.mylibrary.encryption_decryption.EncryptionUtil;
import xyz.ttyz.mylibrary.method.RetrofitUtils;
import xyz.ttyz.mylibrary.method.RxOHCUtils;
import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.ui.BaseTouActivity;
import xyz.ttyz.toubasemvvm.utils.ToastUtil;
import xyz.ttyz.tourfrxohc.BaseApplication;
import xyz.ttyz.tourfrxohc.DefaultUtils;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityLoginBinding;
import xyz.ttyz.tourfrxohc.http.BaseSubscriber;
import xyz.ttyz.tourfrxohc.models.UserModel;

public class LoginActivity extends BaseActivity<ActivityLoginBinding> {
    public static void show(){
        DefaultUtils.setUser(null);
        Intent intent = new Intent(ActivityManager.getInstance(), LoginActivity.class);
        ActivityManager.getInstance().startActivity(intent);
        ActivityManager.popOtherActivity(LoginActivity.class);
    }

    public ObservableField<String> accountFiled = new ObservableField<>("000000");
    public ObservableField<String> password = new ObservableField<>("pswtxt");


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
            if(accountFiled.get().isEmpty() || password.get().isEmpty()){
                ToastUtil.showToast("请输入账号/密码");
                return;
            }
            new RxOHCUtils(LoginActivity.this).executeApi(BaseApplication.apiService.login(accountFiled.get(), EncryptionUtil.md5(password.get()) ), new BaseSubscriber<UserModel>(LoginActivity.this) {
                @Override
                public void success(UserModel data) {
                    if (data != null) {
                        System.out.println("登录成功");
                        DefaultUtils.setUser(data);
                        TicketScanActivity.show();
                    }
                }

                @Override
                public String initCacheKey() {
                    return null;//如果该页面涉及隐私，则不传cacheKey，就不会产生缓存数据
                }

            });
        }
    };
}
