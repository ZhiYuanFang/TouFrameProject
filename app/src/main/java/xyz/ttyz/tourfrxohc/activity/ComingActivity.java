package xyz.ttyz.tourfrxohc.activity;

import android.content.Intent;
import android.location.Location;

import androidx.annotation.Nullable;
import androidx.databinding.ObservableField;

import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.vm.ToolBarViewModel;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityComingBinding;
import xyz.ttyz.tourfrxohc.dialog.LocationDialog;
import xyz.ttyz.tourfrxohc.models.LocationModel;

/**
 * @author 投投
 * @date 2023/10/16
 * @email 343315792@qq.com
 */
public class ComingActivity extends BaseActivity<ActivityComingBinding>{
    public static void show(@Nullable LocationModel locationModel){
        Intent intent = new Intent(ActivityManager.getInstance(), ComingActivity.class);
        intent.putExtra("locationModel", locationModel);
        ActivityManager.getInstance().startActivity(intent);
    }
    ToolBarViewModel toolBarViewModel;
    LocationModel locationModel;
    public ObservableField<String> inputCodeFiled =  new ObservableField<>("");
    @Override
    protected int initLayoutId() {
        return R.layout.activity_coming;
    }

    @Override
    protected String[] initPermission() {
        return new String[0];
    }

    @Override
    protected void initData() {
        locationModel = (LocationModel) getIntent().getSerializableExtra("locationModel");
        mBinding.setContext(this);
        toolBarViewModel = new ToolBarViewModel.Builder()
                .title(locationModel.selectOne + " - " + locationModel.selectTwo)
                .titleClick(new OnClickAdapter.onClickCommand() {
                    @Override
                    public void click() {
                        LocationDialog.showDialog(new LocationDialog.CallBackDelegate() {
                            @Override
                            public void select(LocationModel locationModel) {

                                initServer();
                            }
                        });
                    }
                })
                .build();
        mBinding.setToolBarViewModel(toolBarViewModel);
    }

    @Override
    protected void initServer() {

    }

    public OnClickAdapter.onClickCommand clickScan = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            ScanActivity.show();
        }
    };
}
