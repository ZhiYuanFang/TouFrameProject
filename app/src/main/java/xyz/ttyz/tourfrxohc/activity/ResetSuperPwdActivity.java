package xyz.ttyz.tourfrxohc.activity;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.HashMap;
import java.util.Map;

import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.mylibrary.method.RetrofitUtils;
import xyz.ttyz.mylibrary.method.RxOHCUtils;
import xyz.ttyz.mylibrary.protect.SharedPreferenceUtil;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.tourfrxohc.BaseApplication;
import xyz.ttyz.tourfrxohc.DefaultUtils;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityResetSuperPwdBinding;
import xyz.ttyz.tourfrxohc.http.BaseSubscriber;
import xyz.ttyz.tourfrxohc.utils.Constans;
import xyz.ttyz.tourfrxohc.utils.EncrUtil;
import xyz.ttyz.tourfrxohc.utils.LockUtil;
import xyz.ttyz.tourfrxohc.utils.PwdUtils;
import xyz.ttyz.tourfrxohc.utils.ToastUtils;

/**
 * @author 投投
 * @date 2023/12/26
 * @email 343315792@qq.com
 * 重制应急开箱密码
 */
public class ResetSuperPwdActivity extends BaseActivity<ActivityResetSuperPwdBinding>{
    public static void show(boolean autoOpen){
        Intent intent = new Intent(ActivityManager.getInstance(), ResetSuperPwdActivity.class);
        intent.putExtra("autoOpen", autoOpen);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        ActivityManager.getInstance().startActivity(intent);
    }

    boolean autoOpen;
    @Override
    protected int initLayoutId() {
        return R.layout.activity_reset_super_pwd;
    }

    @Override
    protected String[] initPermission() {
        return new String[0];
    }

    @Override
    protected void initData() {
        autoOpen = getIntent().getBooleanExtra("autoOpen", false);
        mBinding.setContext(this);
        if(autoOpen){
            //发送全部开箱指令
            PwdUtils.clearSuperPwd();
            //将所有钥匙柜清空
            DefaultUtils.resetAllDoorWithNoneKey();
            LockUtil.getInstance(new LockUtil.LockDelegate() {
                @Override
                public void callBackOpen(int keyNumber) {

                }

                @Override
                public void callBackState(boolean[] readArr) {

                }
            }).openAllKey();
        }
    }

    @Override
    protected void initServer() {

    }

    public OnClickAdapter.onClickCommand onClickReset = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            String superPwd = EncrUtil.encrypt(PwdUtils.generateRandomPwd());
            Map map = new HashMap();
            map.put("emergencyPassword", superPwd);
            map.put("keyCabinetId", PwdUtils.getWareHouseCode());
            new RxOHCUtils<>(ActivityManager.getInstance()).executeApi(BaseApplication.apiService.initEmergencyPassword(RetrofitUtils.getNormalBody(map)), new BaseSubscriber<Object>(ResetSuperPwdActivity.this) {
                @Override
                public void success(Object data) {
                    PwdUtils.setSuperPwd(superPwd);
                    //请求接口绑定成功， 跳转到pannel页
                    ToastUtils.showSuccess("重置成功");
                    PannelActivity.show();
                    finish();
                }
            });
        }
    };
}
