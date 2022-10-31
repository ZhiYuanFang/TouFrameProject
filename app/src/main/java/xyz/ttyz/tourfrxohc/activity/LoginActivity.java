package xyz.ttyz.tourfrxohc.activity;

import android.content.Intent;
import android.os.CountDownTimer;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableLong;

import xyz.ttyz.mylibrary.RfRxOHCUtil;
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
        DefaultUtils.clearCache();
        Intent intent = new Intent(ActivityManager.getInstance(), LoginActivity.class);
        ActivityManager.getInstance().startActivity(intent);
        ActivityManager.popOtherActivity(LoginActivity.class);
    }

    public ObservableField<String> accountFiled = new ObservableField<>("000000");
    public ObservableField<String> password = new ObservableField<>("Pz@123456");
    public ObservableField<String> doorID = new ObservableField<>(DefaultUtils.getDoorID());
    public ObservableField<String> ip = new ObservableField<>(DefaultUtils.getIp());
    public ObservableBoolean useSMS = new ObservableBoolean(false);
    public ObservableField<String> smsFiled = new ObservableField<>("");
    public ObservableLong smsCountDown = new ObservableLong(SMS);

    private static final long SMS = 60;

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

    int errTimes = 0;
    public OnClickAdapter.onClickCommand loginClick = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            if(accountFiled.get().isEmpty() || password.get().isEmpty()){
                ToastUtil.showToast("请输入账号/密码");
                return;
            }
            DefaultUtils.setIp(ip.get());
            DefaultUtils.setDoorID(doorID.get());
            RfRxOHCUtil.resetIP(ip.get());
            new RxOHCUtils(LoginActivity.this).executeApi(BaseApplication.apiService.login(accountFiled.get(), EncryptionUtil.md5(password.get()).toUpperCase(), smsFiled.get()), new BaseSubscriber<UserModel>(LoginActivity.this) {
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
//            new RxOHCUtils(LoginActivity.this).executeApi(BaseApplication.apiService.getUserLoginError(accountFiled.get()), new BaseSubscriber<Integer>(LoginActivity.this) {
//                @Override
//                public void success(Integer data) {
//                    if (data != null) {
//                        errTimes = data;
//                    }
//                    useSMS.set(errTimes >= 3);
//
//                    if(useSMS.get() && smsFiled.get().isEmpty()){
//                        ToastUtil.showToast("请输入验证码");
//                        return;
//                    }
//
//                }
//
//                @Override
//                public String initCacheKey() {
//                    return null;//如果该页面涉及隐私，则不传cacheKey，就不会产生缓存数据
//                }
//
//            });

        }
    };


    public OnClickAdapter.onClickCommand smsClick = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            new RxOHCUtils(LoginActivity.this).executeApi(BaseApplication.apiService.getSms(), new BaseSubscriber(LoginActivity.this) {
                @Override
                public void success(Object data) {
                    new CountDownTimer(SMS * 1000, 1000){

                        @Override
                        public void onTick(long millisUntilFinished) {
                            smsCountDown.set(millisUntilFinished/1000);
                        }

                        @Override
                        public void onFinish() {
                            smsCountDown.set(SMS);
                        }
                    }.start();
                }

                @Override
                public String initCacheKey() {
                    return null;//如果该页面涉及隐私，则不传cacheKey，就不会产生缓存数据
                }

                @Override
                public void onError(Throwable e) {
                    new CountDownTimer(SMS * 1000, 1000){

                        @Override
                        public void onTick(long millisUntilFinished) {
                            smsCountDown.set(millisUntilFinished/1000);
                        }

                        @Override
                        public void onFinish() {
                            smsCountDown.set(SMS);
                        }
                    }.start();
                }
            });
        }
    };
}
