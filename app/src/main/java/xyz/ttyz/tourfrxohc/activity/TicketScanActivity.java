package xyz.ttyz.tourfrxohc.activity;

import android.Manifest;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.king.zxing.CameraScan;

import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.Utils;
import xyz.ttyz.tourfrxohc.databinding.ActivityTicketScanBinding;

public class TicketScanActivity extends BaseActivity<ActivityTicketScanBinding> {
    public static void show() {
        Intent intent = new Intent(ActivityManager.getInstance(), TicketScanActivity.class);
        ActivityManager.getInstance().startActivity(intent);
        ActivityManager.popOtherActivity(TicketScanActivity.class);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_ticket_scan;
    }

    @Override
    protected String[] initPermission() {
        return new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initData() {
        mBinding.setContext(this);
    }

    @Override
    protected void initServer() {

    }

    public OnClickAdapter.onClickCommand scanERCode = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            Utils.scanERCode();
        }
    };

    public OnClickAdapter.onClickCommand scanIDCard = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            Utils.scanIDCard();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Utils.SCAN_ERCODE_REQUEST && resultCode == RESULT_OK) {
            String scanResult = data.getStringExtra(CameraScan.SCAN_RESULT);
            ScanDetailActivity.show(scanResult, 1);
            finish();
        }
    }
}
