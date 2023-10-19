package xyz.ttyz.tourfrxohc.activity;

import android.content.Intent;

import org.greenrobot.eventbus.EventBus;

import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.vm.ToolBarViewModel;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityComsucessBinding;
import xyz.ttyz.tourfrxohc.dialog.LocationDialog;
import xyz.ttyz.tourfrxohc.event.GoodsOperatorEvent;
import xyz.ttyz.tourfrxohc.models.LocationModel;

/**
 * @author 投投
 * @date 2023/10/16
 * @email 343315792@qq.com
 */
public class ComSuccessActivity extends BaseActivity<ActivityComsucessBinding>{
    ToolBarViewModel toolBarViewModel;
    public static void show(){
        Intent intent = new Intent(ActivityManager.getInstance(), ComSuccessActivity.class);
        ActivityManager.getInstance().startActivity(intent);
    }
    @Override
    protected int initLayoutId() {
        return R.layout.activity_comsucess;
    }

    @Override
    protected String[] initPermission() {
        return new String[0];
    }

    @Override
    protected void initData() {
        mBinding.setContext(this);
        toolBarViewModel = new ToolBarViewModel.Builder()
                .title("入库成功")
                .build();
        mBinding.setToolBarViewModel(toolBarViewModel);

    }

    @Override
    protected void initServer() {

    }

    public OnClickAdapter.onClickCommand clickContinue = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            ScanActivity.show();
            EventBus.getDefault().post(new GoodsOperatorEvent());
            finish();
        }
    };
}
