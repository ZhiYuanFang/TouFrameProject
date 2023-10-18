package xyz.ttyz.tourfrxohc.activity;

import android.content.Intent;
import android.location.Location;

import androidx.annotation.Nullable;
import androidx.databinding.ObservableField;

import java.util.ArrayList;
import java.util.List;

import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.vm.ToolBarViewModel;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityComingBinding;
import xyz.ttyz.tourfrxohc.dialog.LocationDialog;
import xyz.ttyz.tourfrxohc.fragment.BaseInViewPagerFragment;
import xyz.ttyz.tourfrxohc.fragment.GoodsListFragment;
import xyz.ttyz.tourfrxohc.models.LocationModel;

/**
 * @author 投投
 * @date 2023/10/16
 * @email 343315792@qq.com
 */
public class ComingActivity extends BaseActivity<ActivityComingBinding>{
    public static int Type_in = 0;
    public static int Type_Out = 0;
    public static void show(@Nullable LocationModel locationModel, int type){
        Intent intent = new Intent(ActivityManager.getInstance(), ComingActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("locationModel", locationModel);
        ActivityManager.getInstance().startActivity(intent);
    }
    ToolBarViewModel toolBarViewModel;
    LocationModel locationModel;
    public int type;
    public ObservableField<String> inputCodeFiled =  new ObservableField<>("");

    List<BaseInViewPagerFragment> fragmentList = new ArrayList<BaseInViewPagerFragment>(){{
        add(new GoodsListFragment());
        add(new GoodsListFragment());
    }};
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
        type = getIntent().getIntExtra("type", Type_in);
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
