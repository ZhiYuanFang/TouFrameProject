package xyz.ttyz.tourfrxohc.activity;

import android.Manifest;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.google.gson.Gson;
import com.king.zxing.CameraScan;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xyz.ttyz.mylibrary.encryption_decryption.EncryptionUtil;
import xyz.ttyz.mylibrary.method.BaseModule;
import xyz.ttyz.mylibrary.method.RxOHCUtils;
import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.ui.BaseTouActivity;
import xyz.ttyz.toubasemvvm.utils.DialogUtils;
import xyz.ttyz.toubasemvvm.utils.ToastUtil;
import xyz.ttyz.toubasemvvm.utils.TouUtils;
import xyz.ttyz.tourfrxohc.BaseApplication;
import xyz.ttyz.tourfrxohc.DefaultUtils;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.Utils;
import xyz.ttyz.tourfrxohc.databinding.ActivityScanDetailBinding;
import xyz.ttyz.tourfrxohc.http.BaseSubscriber;
import xyz.ttyz.tourfrxohc.models.UserModel;

import static xyz.ttyz.tourfrxohc.Utils.SCAN_IDCARD_REQUEST;

public class ScanDetailActivity extends BaseActivity<ActivityScanDetailBinding> {
    int type;//1:条码 2:身份证 3:IC 卡
    public static void show(String data, int type) {
        Intent intent = new Intent(ActivityManager.getInstance(), ScanDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("data", data);
        intent.putExtra("type", type);
        ActivityManager.getInstance().startActivity(intent);
        ActivityManager.popOtherActivity(ScanDetailActivity.class);
    }

    public ObservableBoolean searchSuccessFiled = new ObservableBoolean(false);
    public ObservableField<String> tipMessageFiled = new ObservableField<>("");
    public ObservableField<String> scanTypeFiled = new ObservableField<>("");
    public UserModel userModel;//用户信息

    @Override
    protected int initLayoutId() {
        return R.layout.activity_scan_detail;
    }

    @Override
    protected String[] initPermission() {
        return new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
    }

    @Override
    protected void initData() {
        mBinding.setContext(this);
    }

    @Override
    protected void initServer() {
        int type = getIntent().getIntExtra("type", 0);
        String data = getIntent().getStringExtra("data");
        System.out.println("扫码结果：" + data);
        switch (type){
            case 1:
                dealERCode(data);
                break;
            case 2:
                /*根据扫码得到的数据类型，区分扫码类型*/
                try {
                    JSONObject json = new JSONObject(data);
                    /* 扫的是身份证*/
                    scanTypeFiled.set("身份证");
                    userModel = new Gson().fromJson(data, UserModel.class);
                    userModel.setCardNumber(userModel.getCardNumber());
                    userModel.setCheckType(2);
                    searchTicket();
                } catch (JSONException e) {
                    /* 扫的是二维码*/
                    e.printStackTrace();
                    ToastUtil.showToast(e.getMessage());
                }
                break;
            case 3:
                // 市名卡
                break;
            default:
        }

        mBinding.setUserModel(userModel);
    }

    //处理二维码/条形码返回结果
    private void dealERCode(String data){
        scanTypeFiled.set("二维码/条形码");
        userModel = new UserModel();
        userModel.setType("测试票");
//            userModel.setName("张三");
        userModel.setName(data);
        userModel.setCheckType(1);
        userModel.setCardNumber(dealIDCard("330121311012091293"));

        searchTicket();
    }

    //查票
    private void searchTicket(){
        new RxOHCUtils(ScanDetailActivity.this).executeApi(BaseApplication.apiService.searchTicket(userModel.getCardNumber(), userModel.getCheckType(), userModel.getName(), "1",false,DefaultUtils.getUser().getId()), new BaseSubscriber<UserModel>(ScanDetailActivity.this) {
            @Override
            public void success(UserModel data) {

            }

            @Override
            public void onRfRxNext(BaseModule<UserModel> baseModule) {
                super.onRfRxNext(baseModule);
                searchSuccessFiled.set(baseModule.isSuccess());
                if(baseModule.isSuccess()){
                    System.out.println("查票成功");
                    ticketID = "";
                    communicationId="";
                } else {
                    tipMessageFiled.set(baseModule.getMsg());
                }
            }

            @Override
            public String initCacheKey() {
                return null;//如果该页面涉及隐私，则不传cacheKey，就不会产生缓存数据
            }

        });
    }

    String ticketID;//检票接口调用得到检票类型识别号
    String communicationId;//检票接口调用得到通信 ID
    //检票
    public OnClickAdapter.onClickCommand clickUseTicket = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            new RxOHCUtils(ScanDetailActivity.this).executeApi(BaseApplication.apiService.checkTicket(userModel.getCardNumber(), userModel.getCheckType(), "1",false,
                    ticketID,communicationId,
                    DefaultUtils.getUser().getId()), new BaseSubscriber<UserModel>(ScanDetailActivity.this) {
                @Override
                public void success(UserModel data) {
                    // TODO: 2022/9/19 等接口
                    System.out.println("检票成功");
                }

                @Override
                protected void fail(BaseModule<UserModel> baseModule) {
                    super.fail(baseModule);
                    ToastUtil.showToast("检票失败："+baseModule.getMsg());
                }

                @Override
                public String initCacheKey() {
                    return null;//如果该页面涉及隐私，则不传cacheKey，就不会产生缓存数据
                }

            });
        }
    };
    public OnClickAdapter.onClickCommand clickContinueScanERCode = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            Utils.scanERCode();
        }
    };

    public OnClickAdapter.onClickCommand clickContinueScanIDCard = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            Utils.scanIDCard();
        }
    };

    public String dealIDCard(String idCardNum){
        if(idCardNum != null && idCardNum.length() > 6){
            Pattern credentialsPattern = Pattern.compile("(\\d{3})\\d*([0-9a-zA-Z]{4})");
            Matcher credentialsMatch = credentialsPattern.matcher(idCardNum);
            idCardNum = credentialsMatch.replaceAll("$1****$2");
            return idCardNum;
        } else return idCardNum;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Utils.SCAN_ERCODE_REQUEST && resultCode == RESULT_OK) {
            String scanResult = data.getStringExtra(CameraScan.SCAN_RESULT);
            dealERCode(scanResult);
        }
    }
}
