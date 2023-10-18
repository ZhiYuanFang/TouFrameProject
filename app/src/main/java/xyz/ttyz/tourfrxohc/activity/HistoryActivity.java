package xyz.ttyz.tourfrxohc.activity;

import android.content.Intent;

import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.toubasemvvm.vm.ToolBarViewModel;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityHistoryBinding;

/**
 * @author 投投
 * @date 2023/10/18
 * @email 343315792@qq.com
 */
public class HistoryActivity extends BaseActivity<ActivityHistoryBinding> {
    public static void show(){
        Intent intent = new Intent(ActivityManager.getInstance(), HistoryActivity.class);
        ActivityManager.getInstance().startActivity(intent);
    }
    ToolBarViewModel toolBarViewModel;
    @Override
    protected int initLayoutId() {
        return R.layout.activity_history;
    }

    @Override
    protected String[] initPermission() {
        return new String[0];
    }

    @Override
    protected void initData() {
        mBinding.setContext(this);
        toolBarViewModel = new ToolBarViewModel.Builder()
                .title("记录")
                .build();
        mBinding.setToolBarViewModel(toolBarViewModel);
    }

    @Override
    protected void initServer() {

    }
}
