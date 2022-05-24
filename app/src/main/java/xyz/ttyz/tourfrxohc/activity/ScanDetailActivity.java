package xyz.ttyz.tourfrxohc.activity;

import android.content.Intent;

import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.ui.BaseTouActivity;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityScanDetailBinding;

public class ScanDetailActivity extends BaseTouActivity<ActivityScanDetailBinding> {
    public static void show(){
        Intent intent = new Intent(ActivityManager.getInstance(), ScanDetailActivity.class);
        ActivityManager.getInstance().startActivity(intent);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_scan_detail;
    }

    @Override
    protected String[] initPermission() {
        return new String[0];
    }

    @Override
    protected void initData() {
        mBinding.setContext(this);
    }

    @Override
    protected void initServer() {

    }

    public OnClickAdapter.onClickCommand clickContinueScan = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            TicketScanActivity.show();
            finish();
        }
    };
}
