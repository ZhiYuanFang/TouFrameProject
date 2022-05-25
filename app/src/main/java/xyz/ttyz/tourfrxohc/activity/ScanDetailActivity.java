package xyz.ttyz.tourfrxohc.activity;

import android.content.Intent;

import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.ui.BaseTouActivity;
import xyz.ttyz.toubasemvvm.utils.ToastUtil;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.Utils;
import xyz.ttyz.tourfrxohc.databinding.ActivityScanDetailBinding;

public class ScanDetailActivity extends BaseTouActivity<ActivityScanDetailBinding> {
    public static void show(String data){
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
        return new String[0];
    }

    @Override
    protected void initData() {
        mBinding.setContext(this);
    }

    @Override
    protected void initServer() {
        String data = getIntent().getStringExtra("data");
        System.out.println("扫码结果：" + data);
        ToastUtil.showToast(data);
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
            ToastUtil.showToast("身份证");
        }
    };
}
