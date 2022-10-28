package xyz.ttyz.tourfrxohc.activity;

import android.Manifest;
import android.content.Intent;

import com.google.gson.Gson;
import com.nfc.NFCardReaderByRx;
import com.nfc.ReadCallBack;
import com.nfc.UserInfo;

import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.vm.ToolBarViewModel;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.Utils;
import xyz.ttyz.tourfrxohc.databinding.ActivityCardBinding;
import xyz.ttyz.tourfrxohc.models.UserModel;

public class CardActivity extends BaseActivity<ActivityCardBinding>{

    public static void show() {
        Intent intent = new Intent(ActivityManager.getInstance(), CardActivity.class);
        ActivityManager.getInstance().startActivity(intent);
    }
    ToolBarViewModel toolBarViewModel;
    @Override
    protected int initLayoutId() {
        return R.layout.activity_card;
    }

    @Override
    protected String[] initPermission() {
        return new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    @Override
    protected void initData() {
        toolBarViewModel = new ToolBarViewModel.Builder().title("身份证识别").build();
        mBinding.setToolBarViewModel(toolBarViewModel);
        mBinding.setContext(this);
        NFCardReaderByRx.getInstance().init(this, "", "", false);
    }

    @Override
    protected void initServer() {

    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        NFCardReaderByRx.getInstance().readCardCore(intent, new ReadCallBack() {
            @Override
            public void processBack(String s) {
                System.out.println(s);
            }

            @Override
            public void errorBack(String s) {
                System.out.println(s);

            }

            @Override
            public void successRead(UserInfo userInfo) {
                String str = new Gson().toJson(userInfo);
                System.out.println("successRead=====> "+ str);
                UserModel userModel = new UserModel();
                userModel.setCardNumber("");
                ScanDetailActivity.show(new Gson().toJson(userModel), 2);
            }

            @Override
            public void headMsg(String s) {
                System.out.println(s);
            }
        });
    }


    public OnClickAdapter.onClickCommand clickContinueScanIDCard = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            IDCardScanActivity.show();
        }
    };
    @Override
    protected void onPause() {
        super.onPause();
        NFCardReaderByRx.getInstance().onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        NFCardReaderByRx.getInstance().onResume();
    }

}
