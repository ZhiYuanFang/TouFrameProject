package xyz.ttyz.tourfrxohc.activity;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT;

import android.content.Intent;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableField;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

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

import static xyz.ttyz.tourfrxohc.utils.Constans.*;
/**
 * @author 投投
 * @date 2023/10/16
 * @email 343315792@qq.com
 */
public class ComingActivity extends BaseActivity<ActivityComingBinding>{

    public static void show(@Nullable LocationModel locationModel, int type){
        Intent intent = new Intent(ActivityManager.getInstance(), ComingActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("locationModel", locationModel);
        ActivityManager.getInstance().startActivity(intent);
    }
    ToolBarViewModel toolBarViewModel;
    LocationModel locationModel;
    public int type;
    public ObservableField<String> inputCodeFiled =  new ObservableField<>("");//输入的货品码

    List<GoodsListFragment> fragmentList = new ArrayList<GoodsListFragment>(){};
    GoodsListFragment inFragment = new GoodsListFragment(NowIn);
    GoodsListFragment outFragment = new GoodsListFragment(NowOut);

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
        type = getIntent().getIntExtra("type", 0);
        mBinding.setContext(this);
        toolBarViewModel = new ToolBarViewModel.Builder()
                .title(locationModel.selectOne + " - " + locationModel.selectTwo)
                .titleClick(new OnClickAdapter.onClickCommand() {
                    @Override
                    public void click() {
                        LocationDialog.showDialog(new LocationDialog.CallBackDelegate() {
                            @Override
                            public void select(LocationModel locationModel) {

                                inFragment.reSetLocationModel(locationModel);
                                outFragment.reSetLocationModel(locationModel);

                            }
                        });
                    }
                })
                .build();
        mBinding.setToolBarViewModel(toolBarViewModel);

        fragmentList.add(inFragment);
        fragmentList.add(outFragment);
        mBinding.vpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), BEHAVIOR_SET_USER_VISIBLE_HINT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return fragmentList.get(position).initTitle();
            }
        });
        mBinding.recyclerTabLayout.setupWithViewPager(mBinding.vpager);
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
