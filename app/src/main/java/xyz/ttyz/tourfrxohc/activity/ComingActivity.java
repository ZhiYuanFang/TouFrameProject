package xyz.ttyz.tourfrxohc.activity;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT;

import android.content.Intent;
import android.location.Location;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import xyz.ttyz.mylibrary.method.RetrofitUtils;
import xyz.ttyz.mylibrary.method.RxOHCUtils;
import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.toubasemvvm.adapter.EditAdapter;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.event.NetEvent;
import xyz.ttyz.toubasemvvm.utils.ToastUtil;
import xyz.ttyz.toubasemvvm.vm.ToolBarViewModel;
import xyz.ttyz.tourfrxohc.BaseApplication;
import xyz.ttyz.tourfrxohc.DefaultUtils;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityComingBinding;
import xyz.ttyz.tourfrxohc.dialog.LocationDialog;
import xyz.ttyz.tourfrxohc.event.GoodsOperatorEvent;
import xyz.ttyz.tourfrxohc.fragment.BaseInViewPagerFragment;
import xyz.ttyz.tourfrxohc.fragment.GoodsListFragment;
import xyz.ttyz.tourfrxohc.http.BaseSubscriber;
import xyz.ttyz.tourfrxohc.models.LocationModel;

import static xyz.ttyz.tourfrxohc.utils.Constans.*;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author 投投
 * @date 2023/10/16
 * @email 343315792@qq.com
 * 货品列表
 */
public class ComingActivity extends BaseActivity<ActivityComingBinding>{

    public static void show( int type){
        if (DefaultUtils.getLocalLocationModel().warehouseAreaId < 0){
            ToastUtil.showToast("请先选择仓库区域");
            return;
        }
        Intent intent = new Intent(ActivityManager.getInstance(), ComingActivity.class);
        intent.putExtra("type", type);
        ActivityManager.getInstance().startActivity(intent);
    }

    ToolBarViewModel toolBarViewModel;
    public int type;
    public ObservableBoolean searchShowFiled = new ObservableBoolean(false);
    public ObservableField<String> inputCodeFiled =  new ObservableField<>("");//输入的货品码

    List<GoodsListFragment> fragmentList;
    GoodsListFragment inFragment = new GoodsListFragment(NowIn, false);
    GoodsListFragment outFragment = new GoodsListFragment(NowOut, false);
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void hasOperator(GoodsOperatorEvent goodsOperatorEvent){
        fragmentRefresh();
    }

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
        type = getIntent().getIntExtra("type", 0);
        mBinding.setContext(this);
        toolBarViewModel = new ToolBarViewModel.Builder()
                .title(DefaultUtils.getLocalLocationModel().selectOne + " - " + DefaultUtils.getLocalLocationModel().selectTwo)
                .titleClick(new OnClickAdapter.onClickCommand() {
                    @Override
                    public void click() {
                        LocationDialog.showDialog(new LocationDialog.CallBackDelegate() {
                            @Override
                            public void select(LocationModel locationModel) {
                                DefaultUtils.setLocationModel(locationModel);
                                toolBarViewModel.title.set(DefaultUtils.getLocalLocationModel().selectOne + " - " + DefaultUtils.getLocalLocationModel().selectTwo);
                                fragmentRefresh();
                            }
                        });
                    }
                })
                .shareDraResID(R.drawable.search)
                .shareClick(new OnClickAdapter.onClickCommand() {
                    @Override
                    public void click() {
                        searchShowFiled.set(!searchShowFiled.get());
                    }
                })
                .build();
        mBinding.setToolBarViewModel(toolBarViewModel);

        fragmentList = new ArrayList<>();
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
//        if(type == NowOut){
//            mBinding.vpager.setCurrentItem(1);
//        }

        fragmentRefresh();
    }

    private void fragmentRefresh(){
        inFragment.reSetLocationModel(DefaultUtils.getLocalLocationModel(),inputCodeFiled.get());
        outFragment.reSetLocationModel(DefaultUtils.getLocalLocationModel(),inputCodeFiled.get());
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

    public EditAdapter.EditDelegate editSearch = new EditAdapter.EditDelegate() {
        @Override
        public void send(String str) {
            hideInput(getCurrentFocus());
            fragmentRefresh();
        }
    };
}
