package xyz.ttyz.tourfrxohc.activity;

import android.content.Intent;

import androidx.databinding.ObservableField;

import org.greenrobot.eventbus.EventBus;

import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.mylibrary.method.RxOHCUtils;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.utils.ToastUtil;
import xyz.ttyz.tourfrxohc.BaseApplication;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityRegisterBinding;
import xyz.ttyz.tourfrxohc.event.UserChangeEvent;
import xyz.ttyz.tourfrxohc.http.BaseSubscriber;
import xyz.ttyz.tourfrxohc.models.UserModel;

public class RegisterActivity extends BaseActivity<ActivityRegisterBinding> {
    public static void show(){
        Intent intent = new Intent(ActivityManager.getInstance(), RegisterActivity.class);
        ActivityManager.getInstance().startActivity(intent);
    }
    public ObservableField<String> phoneFiled = new ObservableField<>("");
    public ObservableField<String> pwdFiled = new ObservableField<>("");
    public ObservableField<String> confirmPwdFiled = new ObservableField<>("");
    public ObservableField<String> nickNameFiled = new ObservableField<>("");


    @Override
    protected int initLayoutId() {
        return R.layout.activity_register;
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

    public OnClickAdapter.onClickCommand clickRegister = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            new RxOHCUtils<>(RegisterActivity.this).executeApi(BaseApplication.apiService.register(phoneFiled.get(),pwdFiled.get(), nickNameFiled.get()), new BaseSubscriber(RegisterActivity.this) {
                @Override
                public void success(Object data) {
                    ToastUtil.showToast("注册成功");
                    RegisterActivity.this.finish();
                    UserModel cur = new UserModel();
                    cur.setPhone(phoneFiled.get());
                    EventBus.getDefault().post(new UserChangeEvent(cur));
                }

                @Override
                public String initCacheKey() {
                    return null;
                }
            });
        }
    };
}
