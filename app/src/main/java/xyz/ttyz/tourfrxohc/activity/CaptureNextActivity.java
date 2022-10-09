package xyz.ttyz.tourfrxohc.activity;

import androidx.databinding.DataBindingUtil;

import com.king.zxing.CaptureActivity;

import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.toubasemvvm.vm.ToolBarViewModel;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityCaptureNextBinding;

public class CaptureNextActivity extends CaptureActivity {
    ToolBarViewModel toolBarViewModel;
    ActivityCaptureNextBinding mBinding;
    @Override
    public boolean isContentView(int layoutId) {
        return false;
    }

    @Override
    public void initUI() {
        ActivityManager.popActivity(this);
        mBinding = DataBindingUtil.setContentView(this, getLayoutId());
        super.initUI();
        toolBarViewModel = new ToolBarViewModel.Builder().title("扫描二维码").build();
        mBinding.setToolBarViewModel(toolBarViewModel);
        mBinding.setContext(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_capture_next;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.exitActivity(this);
    }
}
