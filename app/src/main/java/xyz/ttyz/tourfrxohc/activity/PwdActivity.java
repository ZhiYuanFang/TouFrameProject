package xyz.ttyz.tourfrxohc.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.databinding.ObservableField;

import com.dilusense.customkeyboard.KeyboardUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.mylibrary.method.RetrofitUtils;
import xyz.ttyz.mylibrary.method.RfRxOHCBaseModule;
import xyz.ttyz.mylibrary.method.RxOHCUtils;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.utils.VersionUtils;
import xyz.ttyz.toubasemvvm.vm.ToolBarViewModel;
import xyz.ttyz.tourfrxohc.BaseApplication;
import xyz.ttyz.tourfrxohc.DefaultUtils;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityPwdBinding;
import xyz.ttyz.tourfrxohc.http.BaseSubscriber;
import xyz.ttyz.tourfrxohc.models.CarModel;
import xyz.ttyz.tourfrxohc.utils.LockUtil;
import xyz.ttyz.tourfrxohc.utils.PwdUtils;
import xyz.ttyz.tourfrxohc.utils.TimeUtils;
import xyz.ttyz.tourfrxohc.utils.ToastUtils;
import xyz.ttyz.tourfrxohc.weight.KeyboardNext;
import xyz.ttyz.tourfrxohc.weight.VerificationCodeView;

/**
 * @author 投投
 * @date 2023/12/26
 * @email 343315792@qq.com
 */
public class PwdActivity extends BaseActivity<ActivityPwdBinding>{

    public static final int PUT = 1;
    public static final int GET = 2;
    private static final String TAG = "PwdActivity";

    public int type;// 取还是存
    public ObservableField<String> pwdFiled = new ObservableField<>("");
    public static void show(int type){
        Intent intent = new Intent(ActivityManager.getInstance(), PwdActivity.class);
        intent.putExtra("type", type);
        ActivityManager.getInstance().startActivity(intent);
    }

    ToolBarViewModel toolBarViewModel;
    @Override
    protected int initLayoutId() {
        return R.layout.activity_pwd;
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
        type = getIntent().getIntExtra("type", PUT);
        KeyboardNext keyboardIdentity = new KeyboardNext();
        KeyboardUtils.bindEditTextEvent(keyboardIdentity, mBinding.etPwd);
        keyboardIdentity.setOnOkClick(new KeyboardNext.OnOkClick() {
            @Override
            public void onOkClick() {
                onClickConfirm.click();
            }
        });
    }

    @Override
    protected void initServer() {
        //检查更新
        VersionUtils.check(this);
        //同步故障柜
        Map map = new HashMap();
        map.put("syncDate", TimeUtils.getServerNeedDate());
        map.put("keyCabinetId", PwdUtils.getWareHouseCode());
        map.put("boxNum", DefaultUtils.getErrorKeyList());
        new RxOHCUtils<>(ActivityManager.getInstance()).executeApi(BaseApplication.apiService.synFaultCabinet(RetrofitUtils.getNormalBody(map)), new BaseSubscriber<Object>(PwdActivity.this) {
            @Override
            public void success(Object data) {

            }
        });
    }

    public OnClickAdapter.onClickCommand onClickConfirm = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
           if(pwdFiled.get().length() == 4){
               switch (type){
                   case PUT:{
                       Map map = new HashMap();
                       map.put("keyInterBoxCode", pwdFiled.get());
                       map.put("keyCabinetId", PwdUtils.getWareHouseCode());
                       new RxOHCUtils<>(ActivityManager.getInstance()).executeApi(BaseApplication.apiService.getInterBoxCarInfo(RetrofitUtils.getNormalBody(map)), new BaseSubscriber<CarModel>(PwdActivity.this) {
                           @Override
                           public void success(CarModel data) {

                               PutDetailActivity.show(data, pwdFiled.get());

                           }
                       });
                       break;}
                   case GET:{
                       Map map = new HashMap();
                       map.put("keyCabinetType", 2);
                       map.put("keyOutBoxCheckCode", pwdFiled.get());
                       map.put("keyCabinetId", PwdUtils.getWareHouseCode());
                       new RxOHCUtils<>(ActivityManager.getInstance()).executeApi(BaseApplication.apiService.canOutBoxKeyList(RetrofitUtils.getNormalBody(map)), new BaseSubscriber<List<CarModel>>(PwdActivity.this) {
                           @Override
                           public void success(List<CarModel> data) {
                               // 成功才进入
                               GetListActivity.show(pwdFiled.get());
                               pwdFiled.set("");
                           }
                       });
                       break;}
                   default:
               }
           } else {
               ToastUtils.showError("请输入4位开柜密码");
           }
        }
    };

    public OnClickAdapter.onClickCommand onClickSafeOpen = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            SafeOpenActivity.show(type);
        }
    };
}
