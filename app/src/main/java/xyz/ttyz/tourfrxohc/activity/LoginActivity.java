package xyz.ttyz.tourfrxohc.activity;

import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityLoginBinding;
import xyz.ttyz.tourfrxohc.models.UserModel;
import xyz.ttyz.tourfrxohc.utils.UserUtils;

public class LoginActivity extends BaseActivity<ActivityLoginBinding>{
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

    }

    @Override
    protected void initServer() {

    }

    public OnClickAdapter.onClickCommand clickLogin = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            // TODO: 2021/3/31 请求接口 获取用户信息
            UserModel userModel = new UserModel();
            userModel.setId(1);
            UserUtils.login(userModel);
        }
    };
}
