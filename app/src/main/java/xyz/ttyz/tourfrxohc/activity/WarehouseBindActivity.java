package xyz.ttyz.tourfrxohc.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.dilusense.customkeyboard.KeyboardUtils;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.HashMap;
import java.util.Map;

import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.mylibrary.method.RetrofitUtils;
import xyz.ttyz.mylibrary.method.RfRxOHCBaseModule;
import xyz.ttyz.mylibrary.method.RxOHCUtils;
import xyz.ttyz.mylibrary.protect.SharedPreferenceUtil;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.tourfrxohc.BaseApplication;
import xyz.ttyz.tourfrxohc.DefaultUtils;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityWarehouseBindBinding;
import xyz.ttyz.tourfrxohc.http.BaseSubscriber;
import xyz.ttyz.tourfrxohc.utils.Constans;
import xyz.ttyz.tourfrxohc.utils.EncrUtil;
import xyz.ttyz.tourfrxohc.utils.PwdUtils;
import xyz.ttyz.tourfrxohc.utils.ToastUtils;
import xyz.ttyz.tourfrxohc.weight.KeyboardNext;

/**
 * @author 投投
 * @date 2023/12/26
 * @email 343315792@qq.com
 * 仓库绑定
 */
public class WarehouseBindActivity extends BaseActivity<ActivityWarehouseBindBinding> {
    public static void show(){
        Intent intent = new Intent(ActivityManager.getInstance(), WarehouseBindActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        ActivityManager.getInstance().startActivity(intent);
    }

    public ObservableField<String> pwdFiled = new ObservableField<>("");

    @Override
    protected int initLayoutId() {
        return R.layout.activity_warehouse_bind;
    }

    @Override
    protected String[] initPermission() {
        return new String[0];
    }

    @Override
    protected void initData() {

        mBinding.setContext(this);
        KeyboardNext keyboardIdentity = new KeyboardNext();
        KeyboardUtils.bindEditTextEvent(keyboardIdentity,(EditText) mBinding.etPwd);
        keyboardIdentity.setOnOkClick(new KeyboardNext.OnOkClick() {
            @Override
            public void onOkClick() {
                onClickConfirm.click();
            }
        });

        View view = findViewById(R.id.lv_top).findViewById(R.id.iv_tolauncher);
        view.setVisibility(View.VISIBLE);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DefaultUtils.toLauncher();
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
                ToastUtils.showError("请输入设备号");
                return;
            }
            String superPwd = EncrUtil.encrypt(PwdUtils.generateRandomPwd());
            Map map = new HashMap();
            map.put("emergencyPassword", superPwd);
            map.put("keyCabinetId", pwdFiled.get());
            new RxOHCUtils<>(ActivityManager.getInstance()).executeApi(BaseApplication.apiService.verifyCabinetInit(RetrofitUtils.getNormalBody(map)), new BaseSubscriber<Object>(WarehouseBindActivity.this) {
                @Override
                public void success(Object data) {
                    PwdUtils.setSuperPwd(superPwd);
                    PwdUtils.setWareHouseCode(pwdFiled.get());
                    //请求接口绑定成功， 跳转到pannel页
                    ToastUtils.showSuccess("绑定成功");
                    PannelActivity.show();
                    finish();
                }
            });
        }
    };
}
