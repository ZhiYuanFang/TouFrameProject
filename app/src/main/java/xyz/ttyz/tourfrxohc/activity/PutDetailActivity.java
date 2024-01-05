package xyz.ttyz.tourfrxohc.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import java.util.HashMap;
import java.util.Map;

import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.mylibrary.method.BaseModule;
import xyz.ttyz.mylibrary.method.RetrofitUtils;
import xyz.ttyz.mylibrary.method.RxOHCUtils;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.utils.DialogUtils;
import xyz.ttyz.toubasemvvm.vm.BaseViewModle;
import xyz.ttyz.tourfrxohc.BaseApplication;
import xyz.ttyz.tourfrxohc.DefaultUtils;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityPutDetailBinding;
import xyz.ttyz.tourfrxohc.http.BaseSubscriber;
import xyz.ttyz.tourfrxohc.models.CarModel;
import xyz.ttyz.tourfrxohc.utils.LockUtil;
import xyz.ttyz.tourfrxohc.utils.PwdUtils;
import xyz.ttyz.tourfrxohc.utils.TimeUtils;
import xyz.ttyz.tourfrxohc.utils.ToastUtils;

/**
 * @author 投投
 * @date 2023/12/26
 * @email 343315792@qq.com
 */
public class PutDetailActivity extends BaseActivity<ActivityPutDetailBinding>{
    private static final String TAG = "PutDetailActivity";
    public static void show(CarModel carModel, String keyInterBoxCode){
        Intent intent = new Intent(ActivityManager.getInstance(), PutDetailActivity.class);
        intent.putExtra("carModel", carModel);
        intent.putExtra("keyInterBoxCode", keyInterBoxCode);

        ActivityManager.getInstance().startActivity(intent);
    }
    String keyInterBoxCode;
    public ObservableInt openDoorFiled = new ObservableInt(0);

    @Override
    protected int initLayoutId() {
        return R.layout.activity_put_detail;
    }

    @Override
    protected String[] initPermission() {
        return new String[0];
    }

    String boxPassword;

    public BaseViewModle retryModel = new BaseViewModle();
    public BaseViewModle reportModel = new BaseViewModle();
    @Override
    protected void initData() {
        mBinding.setContext(this);

        keyInterBoxCode = getIntent().getStringExtra("keyInterBoxCode");
        mBinding.setCarModel((CarModel) getIntent().getSerializableExtra("carModel"));

        retryModel.onClickCommand.set(retryClick);
        reportModel.onClickCommand.set(errorReportClick);


        int doorNumber = DefaultUtils.getDoorAddress(true, mBinding.getCarModel());
        Log.i(TAG, "initData: 存入钥匙 -> " + doorNumber);
        if(doorNumber < 1){
            ToastUtils.showError("当前柜已满");
            finish();
            return;
        }
        openDoorFiled.set(doorNumber);

        boxPassword = PwdUtils.getDoorPwd(doorNumber);


        // 门想开
        Map map = new HashMap();
        map.put("applyInterBoxDate", TimeUtils.getServerNeedDate());
        map.put("boxNum", openDoorFiled.get());
        map.put("boxPassword", boxPassword);
        map.put("keyCabinetId", PwdUtils.getWareHouseCode());
        map.put("keyCabinetType", 2);
        map.put("keyInterBoxCode", keyInterBoxCode);
        new RxOHCUtils<>(ActivityManager.getInstance()).executeApi(BaseApplication.apiService.beforeInterBoxKey(RetrofitUtils.getNormalBody(map)), new BaseSubscriber<Object>(PutDetailActivity.this) {
            @Override
            public void success(Object data) {
                //后端点了出柜之后，腾出了这个柜子，才能开柜
                retryClick.click();
            }

            @Override
            protected void fail(BaseModule<Object> baseModule) {
                //接口不让出这个柜
                DialogUtils.showSingleDialog("当前密码已失效，请重新申请密码开柜", new DialogUtils.DialogButtonModule("确定", new DialogUtils.DialogClickDelegate() {
                    @Override
                    public void click(DialogUtils.DialogButtonModule dialogButtonModule) {
                        finish();
                    }
                }));
            }
        });
    }

    @Override
    protected void initServer() {

    }

    public OnClickAdapter.onClickCommand retryClick = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            final boolean[] isOpen = new boolean[1];

            LockUtil.getInstance(new LockUtil.LockDelegate() {
                @Override
                public void callBackOpen(int keyNumber) {
                    isOpen[0] = true;
                }

                @Override
                public void callBackState(boolean[] readArr) {
                    if(isOpen[0]){
                        if(!readArr[openDoorFiled.get() - 1]){
                            // 从开到关,表示存放成功【将钥匙放入柜门并关闭】
                            PutSuccessActivity.show(openDoorFiled.get(), keyInterBoxCode, boxPassword);
                            finish();
                        }
                    }
                }
            }).openKey(openDoorFiled.get());



        }
    };
    public OnClickAdapter.onClickCommand errorReportClick = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            //故障
            DefaultUtils.setErrorDoor(openDoorFiled.get());
            //回退状态
            Map map = new HashMap();
            map.put("keyCabinetId", PwdUtils.getWareHouseCode());
            map.put("keyId", mBinding.getCarModel().keyId);
            new RxOHCUtils<>(ActivityManager.getInstance()).executeApi(BaseApplication.apiService.cancelBeforeInterBoxKey(RetrofitUtils.getNormalBody(map)), new BaseSubscriber<Object>(PutDetailActivity.this) {
                @Override
                public void success(Object data) {
                    //故障之后，回到输入密码页
                    finish();
                }
            });

        }
    };
}
