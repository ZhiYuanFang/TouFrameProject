package xyz.ttyz.tourfrxohc.activity;

import android.content.Intent;

import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.ui.BaseTouActivity;
import xyz.ttyz.toubasemvvm.vm.ToolBarViewModel;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityAccountInfoBinding;

public class AccountInfoActivity extends BaseTouActivity<ActivityAccountInfoBinding> {
    public static void show(){
        Intent intent = new Intent(ActivityManager.getInstance(), AccountInfoActivity.class);
        ActivityManager.getInstance().startActivity(intent);
    }

    ToolBarViewModel toolBarViewModel;
    @Override
    protected int initLayoutId() {
        return R.layout.activity_account_info;
    }

    @Override
    protected String[] initPermission() {
        return new String[0];
    }

    @Override
    protected void initData() {
        toolBarViewModel = new ToolBarViewModel.Builder().title("账户信息").build();
        mBinding.setToolBarViewModel(toolBarViewModel);
        mBinding.setContext(this);
    }

    @Override
    protected void initServer() {

    }
    public OnClickAdapter.onClickCommand reLoginClick = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            LoginActivity.show();
        }
    };
}
