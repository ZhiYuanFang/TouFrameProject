package xyz.ttyz.tourfrxohc.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.databinding.ObservableBoolean;

import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.vm.ToolBarViewModel;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityScanBinding;
import xyz.ttyz.tourfrxohc.dialog.LocationDialog;
import xyz.ttyz.tourfrxohc.models.GoodsModel;
import xyz.ttyz.tourfrxohc.models.LocationModel;

/**
 * @author 投投
 * @date 2023/10/16
 * @email 343315792@qq.com
 */
public class ScanActivity extends BaseActivity<ActivityScanBinding> {
    public static void show() {
        Intent intent = new Intent(ActivityManager.getInstance(), ScanActivity.class);
        ActivityManager.getInstance().startActivity(intent);
    }
    private static final String SCANACTION = "com.android.server.scannerservice.broadcast";
    ToolBarViewModel toolBarViewModel;

    BroadcastReceiver scanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SCANACTION)) {
                String code = intent.getStringExtra("scannerdata");
                System.out.println("扫描结果：" + code);
                // TODO: 2023/10/16 转变成model
                GoodsDetailActivity.show(new GoodsModel(), false);
                finish();
            }
        }
    };

    @Override
    protected int initLayoutId() {
        return R.layout.activity_scan;
    }

    @Override
    protected String[] initPermission() {
        return new String[0];
    }

    @Override
    protected void onResume() {
        super.onResume();//注册广播接收器
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SCANACTION);
        intentFilter.setPriority(Integer.MAX_VALUE);
        registerReceiver(scanReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(scanReceiver);
    }

    @Override
    protected void initData() {
        mBinding.setContext(this);
        toolBarViewModel = new ToolBarViewModel.Builder()
                .title("请扫描")
                .build();
        mBinding.setToolBarViewModel(toolBarViewModel);

    }

    @Override
    protected void initServer() {

    }

}
