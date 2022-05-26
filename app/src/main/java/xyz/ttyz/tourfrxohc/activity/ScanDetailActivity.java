package xyz.ttyz.tourfrxohc.activity;

import android.Manifest;
import android.content.Intent;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.ui.BaseTouActivity;
import xyz.ttyz.toubasemvvm.utils.DialogUtils;
import xyz.ttyz.toubasemvvm.utils.ToastUtil;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.Utils;
import xyz.ttyz.tourfrxohc.databinding.ActivityScanDetailBinding;

import static xyz.ttyz.tourfrxohc.Utils.SCAN_IDCARD_REQUEST;

public class ScanDetailActivity extends BaseTouActivity<ActivityScanDetailBinding> {
    public static void show(String data) {
        Intent intent = new Intent(ActivityManager.getInstance(), ScanDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("data", data);
        ActivityManager.getInstance().startActivity(intent);
        ActivityManager.popOtherActivity(ScanDetailActivity.class);
    }

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
        try {
            JSONObject json = new JSONObject(data);
            /* 扫的是身份证*/
            DialogUtils.showSingleDialog("身份证", data);
        } catch (JSONException e) {
            /* 扫的是二维码*/
            DialogUtils.showSingleDialog("二维码", data);
        }
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
}
