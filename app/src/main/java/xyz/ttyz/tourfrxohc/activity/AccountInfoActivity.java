package xyz.ttyz.tourfrxohc.activity;

import android.content.Intent;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import xyz.ttyz.mylibrary.method.RxOHCUtils;
import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.ui.BaseTouActivity;
import xyz.ttyz.toubasemvvm.vm.ToolBarViewModel;
import xyz.ttyz.tourfrxohc.BaseApplication;
import xyz.ttyz.tourfrxohc.DefaultUtils;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityAccountInfoBinding;
import xyz.ttyz.tourfrxohc.http.BaseSubscriber;
import xyz.ttyz.tourfrxohc.models.UserModel;

public class AccountInfoActivity extends BaseActivity<ActivityAccountInfoBinding> {
    public static void show(){
        Intent intent = new Intent(ActivityManager.getInstance(), AccountInfoActivity.class);
        ActivityManager.getInstance().startActivity(intent);
    }

    public ObservableBoolean isEditDoorFiled = new ObservableBoolean(false);
    public ObservableField<String> doorIDFiled = new ObservableField<String>(DefaultUtils.getDoorID());

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
        mBinding.setUserModel(DefaultUtils.getUser());
    }

    @Override
    protected void initServer() {

    }
    public OnClickAdapter.onClickCommand reLoginClick = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            new RxOHCUtils(AccountInfoActivity.this).executeApi(BaseApplication.apiService.loginOut(DefaultUtils.getUser().getId()), new BaseSubscriber<UserModel>(AccountInfoActivity.this) {
                @Override
                public void success(UserModel data) {
                    System.out.println("登出成功");
                    DefaultUtils.clearCache();
                    LoginActivity.show();
                }

                @Override
                public String initCacheKey() {
                    return null;//如果该页面涉及隐私，则不传cacheKey，就不会产生缓存数据
                }

            });
        }
    };

    public OnClickAdapter.onClickCommand dealDoor = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            isEditDoorFiled.set(!isEditDoorFiled.get());
            if(!isEditDoorFiled.get()){
                DefaultUtils.setDoorID(doorIDFiled.get());
            }
        }
    };
}
