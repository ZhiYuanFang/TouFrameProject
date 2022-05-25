package xyz.ttyz.tourfrxohc.activity;

import android.content.Intent;

import androidx.databinding.ObservableField;

import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.ui.BaseTouActivity;
import xyz.ttyz.tourfrxohc.DefaultUtils;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityLoginBinding;

public class LoginActivity extends BaseTouActivity<ActivityLoginBinding> {
    public static void show(){
        // TODO: 2022/5/23 清理账户信息
        DefaultUtils.token = null;
        Intent intent = new Intent(ActivityManager.getInstance(), LoginActivity.class);
        ActivityManager.getInstance().startActivity(intent);
        ActivityManager.popOtherActivity(LoginActivity.class);
    }

    public ObservableField<String> accountFiled = new ObservableField<>("");
    public ObservableField<String> password = new ObservableField<>("");


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
            DefaultUtils.token = "123";
            TicketScanActivity.show();
        }
    };
}
