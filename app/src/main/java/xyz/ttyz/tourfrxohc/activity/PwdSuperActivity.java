package xyz.ttyz.tourfrxohc.activity;

import android.content.Intent;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.dilusense.customkeyboard.KeyboardUtils;
import com.trello.rxlifecycle2.LifecycleTransformer;

import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.vm.ToolBarViewModel;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityPwdBinding;
import xyz.ttyz.tourfrxohc.databinding.ActivityPwdSuperBinding;
import xyz.ttyz.tourfrxohc.models.CarModel;
import xyz.ttyz.tourfrxohc.utils.EncrUtil;
import xyz.ttyz.tourfrxohc.utils.LockUtil;
import xyz.ttyz.tourfrxohc.utils.PwdUtils;
import xyz.ttyz.tourfrxohc.utils.ToastUtils;
import xyz.ttyz.tourfrxohc.weight.KeyboardNext;

/**
 * @author 投投
 * @date 2023/12/26
 * @email 343315792@qq.com
 * 应急开箱
 */
public class PwdSuperActivity extends BaseActivity<ActivityPwdSuperBinding>{
    public static void show(){
        ActivityManager.getInstance().startActivity(new Intent(ActivityManager.getInstance(), PwdSuperActivity.class));
    }
    ToolBarViewModel toolBarViewModel;

    public ObservableField<String> pwdFiled = new ObservableField<>("");
    @Override
    protected int initLayoutId() {
        return R.layout.activity_pwd_super;
    }

    @Override
    protected String[] initPermission() {
        return new String[0];
    }

    @Override
    protected void initData() {
        mBinding.setContext(this);
        toolBarViewModel = new ToolBarViewModel.Builder()
                .build();
        mBinding.setToolBarViewModel(toolBarViewModel);
        KeyboardNext keyboardIdentity = new KeyboardNext();
        KeyboardUtils.bindEditTextEvent(keyboardIdentity,(EditText) mBinding.etPwd);
        keyboardIdentity.setOnOkClick(new KeyboardNext.OnOkClick() {
            @Override
            public void onOkClick() {
                onClickConfirm.click();
            }
        });
    }

    @Override
    protected void initServer() {

    }

    public OnClickAdapter.onClickCommand onClickConfirm = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            if(pwdFiled.get().isEmpty()){
                ToastUtils.showError("请输入密码");
                return;
            }

            if(!pwdFiled.get().equals(PwdUtils.getSuperPwd())){
                ToastUtils.showError("密码错误");
                return;
            }

            ResetSuperPwdActivity.show(true);
            finish();
        }
    };
}
