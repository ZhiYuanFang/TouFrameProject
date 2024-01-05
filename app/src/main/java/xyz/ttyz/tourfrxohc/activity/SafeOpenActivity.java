package xyz.ttyz.tourfrxohc.activity;

import android.content.Intent;
import android.widget.EditText;

import androidx.databinding.ObservableField;

import com.dilusense.customkeyboard.KeyboardUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;

import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.vm.ToolBarViewModel;
import xyz.ttyz.tourfrxohc.DefaultUtils;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivitySafeOpenBinding;
import xyz.ttyz.tourfrxohc.models.CarModel;
import xyz.ttyz.tourfrxohc.utils.LockUtil;
import xyz.ttyz.tourfrxohc.utils.PwdUtils;
import xyz.ttyz.tourfrxohc.utils.ToastUtils;
import xyz.ttyz.tourfrxohc.weight.KeyboardNext;

/**
 * @author 投投
 * @date 2023/12/26
 * @email 343315792@qq.com
 * 密码取用
 */
public class SafeOpenActivity extends BaseActivity<ActivitySafeOpenBinding>{

    public static void show(int type){
        Intent intent = new Intent(ActivityManager.getInstance(), SafeOpenActivity.class);
        intent.putExtra("type", type);
        ActivityManager.getInstance().startActivity(intent);
    }

    int type;
    public ObservableField<String> pwdFiled = new ObservableField<>("");
    ToolBarViewModel toolBarViewModel;
    @Override
    protected int initLayoutId() {
        return R.layout.activity_safe_open;
    }

    @Override
    protected String[] initPermission() {
        return new String[0];
    }

    @Override
    protected void initData() {
        type = getIntent().getIntExtra("type", PwdActivity.PUT);
        mBinding.setContext(this);
        toolBarViewModel = new ToolBarViewModel.Builder().build();
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
            switch (type){
                case PwdActivity.PUT:
                    //不存在
                    break;
                case PwdActivity.GET:
                    //打开所有门
                    //通过pwdFiled获取对应车得到门
                    int doorNumber = PwdUtils.getDoor(pwdFiled.get());
                    if(doorNumber > 0){
                        ToastUtils.showSuccess("密码正确");
                        LockUtil.getInstance(new LockUtil.LockDelegate() {
                            @Override
                            public void callBackOpen(int keyNumber) {
                                //重置锁存在状态
                                DefaultUtils.resetDoorWithKey(doorNumber, false);
                                //将密码门关系删除
                                PwdUtils.removeDoor(doorNumber);
                                GetSuccessActivity.show(GetSuccessActivity.PwdComing);
                                finish();
                            }

                            @Override
                            public void callBackState(boolean[] readArr) {

                            }
                        }).openKey(doorNumber);
                    } else {
                        ToastUtils.showError("密码不正确");
                    }
                    break;
                default:
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LockUtil.clearCallBack();
    }
}
