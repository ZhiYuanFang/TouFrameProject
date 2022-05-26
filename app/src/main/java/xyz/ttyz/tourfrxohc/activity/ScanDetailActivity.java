package xyz.ttyz.tourfrxohc.activity;

import android.Manifest;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.ui.BaseTouActivity;
import xyz.ttyz.toubasemvvm.utils.DialogUtils;
import xyz.ttyz.toubasemvvm.utils.ToastUtil;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.Utils;
import xyz.ttyz.tourfrxohc.databinding.ActivityScanDetailBinding;
import xyz.ttyz.tourfrxohc.models.UserModel;

import static xyz.ttyz.tourfrxohc.Utils.SCAN_IDCARD_REQUEST;

public class ScanDetailActivity extends BaseTouActivity<ActivityScanDetailBinding> {
    public static void show(String data) {
        Intent intent = new Intent(ActivityManager.getInstance(), ScanDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("data", data);
        ActivityManager.getInstance().startActivity(intent);
        ActivityManager.popOtherActivity(ScanDetailActivity.class);
    }

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
        String data = getIntent().getStringExtra("data");
        System.out.println("扫码结果：" + data);
        /*根据扫码得到的数据类型，区分扫码类型*/
        try {
            JSONObject json = new JSONObject(data);
            /* 扫的是身份证*/
            scanTypeFiled.set("身份证");
            userModel = new Gson().fromJson(data, UserModel.class);
            userModel.setCardNumber(dealIDCard(userModel.getCardNumber()));
        } catch (JSONException e) {
            /* 扫的是二维码*/
            scanTypeFiled.set("二维码");
            userModel = new UserModel();
            userModel.setType("测试票");
            userModel.setName("张三");
            userModel.setCardNumber(dealIDCard("330121311012091293"));
        }
        mBinding.setUserModel(userModel);

    }


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

    private String dealIDCard(String idCardNum){
        if(idCardNum != null && idCardNum.length() > 6){
            Pattern credentialsPattern = Pattern.compile("(\\d{3})\\d*([0-9a-zA-Z]{4})");
            Matcher credentialsMatch = credentialsPattern.matcher(idCardNum);
            idCardNum = credentialsMatch.replaceAll("$1****$2");
            return idCardNum;
        } else return idCardNum;
    }
}
