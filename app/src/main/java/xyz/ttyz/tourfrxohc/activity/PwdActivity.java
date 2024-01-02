package xyz.ttyz.tourfrxohc.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.dilusense.customkeyboard.KeyboardUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.mylibrary.method.RetrofitUtils;
import xyz.ttyz.mylibrary.method.RfRxOHCBaseModule;
import xyz.ttyz.mylibrary.method.RxOHCUtils;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.vm.ToolBarViewModel;
import xyz.ttyz.tourfrxohc.BaseApplication;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityPwdBinding;
import xyz.ttyz.tourfrxohc.http.BaseSubscriber;
import xyz.ttyz.tourfrxohc.models.CarModel;
import xyz.ttyz.tourfrxohc.utils.PwdUtils;
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
        mBinding.vcv.setOnCodeFinishListener(new VerificationCodeView.OnCodeFinishListener() {
            @Override
            public void onTextChange(View view, String content) {
            }

            @Override
            public void onComplete(View view, String content) {
            }

            @Override
            public void onFocusChanged(EditText editText) {
                if(editText != null)
                    keyboardIdentity.attachTo(editText);

            }
        });

        KeyboardUtils.bindEditTextEvent(keyboardIdentity,(EditText) mBinding.vcv.getChildAt(0));
        keyboardIdentity.setOnOkClick(new KeyboardNext.OnOkClick() {
            @Override
            public void onOkClick() {
                onClickConfirm.click();
            }
        });
        keyboardIdentity.setOnDeleteNoneClick(new KeyboardNext.OnDeleteNoneClick() {
            @Override
            public void onDeleteNoneClick(EditText editText) {
                //focus 上一个
                int pos = mBinding.vcv.indexOfChild(editText);
                if(pos > 0){
                    keyboardIdentity.attachTo((EditText) mBinding.vcv.getChildAt(pos - 1));
                }
            }
        });
    }

    @Override
    protected void initServer() {

    }

    public OnClickAdapter.onClickCommand onClickConfirm = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
           if(mBinding.vcv.getResult().length() == 4){
               switch (type){
                   case PUT:{
                       Map map = new HashMap();
                       map.put("keyInterBoxCode", mBinding.vcv.getResult());
                       map.put("keyCabinetId", PwdUtils.getWareHouseCode());
                       new RxOHCUtils<>(ActivityManager.getInstance()).executeApi(BaseApplication.apiService.getInterBoxCarInfo(RetrofitUtils.getNormalBody(map)), new BaseSubscriber<CarModel>(PwdActivity.this) {
                           @Override
                           public void success(CarModel data) {

                               PutDetailActivity.show(data, mBinding.vcv.getResult());
                               mBinding.vcv.setEmpty();

                           }
                       });
                       break;}
                   case GET:{
                       Map map = new HashMap();
                       map.put("keyCabinetType", 2);
                       map.put("keyOutBoxCheckCode", mBinding.vcv.getResult());
                       map.put("keyCabinetId", PwdUtils.getWareHouseCode());
                       new RxOHCUtils<>(ActivityManager.getInstance()).executeApi(BaseApplication.apiService.canOutBoxKeyList(RetrofitUtils.getNormalBody(map)), new BaseSubscriber<List<CarModel>>(PwdActivity.this) {
                           @Override
                           public void success(List<CarModel> data) {
                               // 成功才进入
                               GetListActivity.show(mBinding.vcv.getResult());
                               mBinding.vcv.setEmpty();
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
