package xyz.ttyz.tourfrxohc.activity;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT;
import static xyz.ttyz.tourfrxohc.utils.Constans.NowIn;
import static xyz.ttyz.tourfrxohc.utils.Constans.NowOut;
import static xyz.ttyz.tourfrxohc.utils.Constans.NowPant;
import static xyz.ttyz.tourfrxohc.utils.Constans.NowWait;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.seuic.uhf.EPC;
import com.seuic.uhf.UHFService;

import java.util.ArrayList;
import java.util.List;

import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.adapter.utils.BaseEmptyAdapterParent;
import xyz.ttyz.toubasemvvm.adapter.utils.BaseRecyclerAdapter;
import xyz.ttyz.toubasemvvm.utils.ToastUtil;
import xyz.ttyz.toubasemvvm.vm.ToolBarViewModel;
import xyz.ttyz.tourfrxohc.DefaultUtils;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityPandingBinding;
import xyz.ttyz.tourfrxohc.dialog.LocationDialog;
import xyz.ttyz.tourfrxohc.fragment.GoodsListFragment;
import xyz.ttyz.tourfrxohc.models.LocationModel;
import xyz.ttyz.tourfrxohc.viewholder.PandingViewHolder;

/**
 * @author 投投
 * @date 2023/10/18
 * @email 343315792@qq.com
 * 盘库射频
 */
public class PandingActivity extends BaseActivity<ActivityPandingBinding> {

    public static void show() {

        if (DefaultUtils.getLocalLocationModel().warehouseAreaId < 0){
            ToastUtil.showToast("请先选择仓库区域");
            return;
        }
        Intent intent = new Intent(ActivityManager.getInstance(), PandingActivity.class);
        ActivityManager.getInstance().startActivity(intent);
    }

    UHFService uhfService;
    ToolBarViewModel toolBarViewModel;
    BaseEmptyAdapterParent pandingEndAdapter;
    List<GoodsListFragment> fragmentList;
    GoodsListFragment pantFragment = new GoodsListFragment(NowPant);
    GoodsListFragment waitFragment = new GoodsListFragment(NowWait);

    //连续寻卡回调
    private List<EPC> epcList = new ArrayList<>();

    public ObservableBoolean isPandingFiled = new ObservableBoolean(false);

    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected int initLayoutId() {
        return R.layout.activity_panding;
    }

    @Override
    protected String[] initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return new String[]{Manifest.permission.BLUETOOTH_CONNECT};
        } else {
            return new String[]{Manifest.permission.BLUETOOTH};
        }
    }


    @Override
    protected void initData() {

        mBinding.setContext(this);
        toolBarViewModel = new ToolBarViewModel.Builder()
                .title(DefaultUtils.getLocalLocationModel().selectOne + "-" + DefaultUtils.getLocalLocationModel().selectTwo)
                .titleClick(new OnClickAdapter.onClickCommand() {
                    @Override
                    public void click() {
                        LocationDialog.showDialog(new LocationDialog.CallBackDelegate() {
                            @Override
                            public void select(LocationModel model) {
                                DefaultUtils.setLocationModel(model);
                                toolBarViewModel.title.set(DefaultUtils.getLocalLocationModel().selectOne + "-" + DefaultUtils.getLocalLocationModel().selectTwo);
                            }
                        });
                    }
                })
                .rightTxt("记录")
                .rightClick(new OnClickAdapter.onClickCommand() {
                    @Override
                    public void click() {
                        HistoryActivity.show();
                    }
                })
                .build();
        mBinding.setToolBarViewModel(toolBarViewModel);
        uhfService = UHFService.getInstance(this);

        pandingEndAdapter = new BaseEmptyAdapterParent(this, new BaseRecyclerAdapter.NormalAdapterDelegate() {
            @Override
            public int getItemViewType(int position) {
                return 0;
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new PandingViewHolder(PandingActivity.this, parent);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((PandingViewHolder) holder).bindData(epcList.get(position));
            }
        });
        mBinding.setPandingEndAdapter(pandingEndAdapter);


        isPandingFiled.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if(isPandingFiled.get()){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(isPandingFiled.get()){
                                epcList = uhfService.getTagIDs();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        pandingEndAdapter.setList(epcList);
                                    }
                                });
                                handler.postDelayed(this, 600);
                            }
                        }
                    });
                }
            }
        });
        fragmentList = new ArrayList<>();
        fragmentList.add(waitFragment);
        fragmentList.add(pantFragment);
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

        fragmentRefresh();
    }

    private void fragmentRefresh(){
        waitFragment.reSetLocationModel(DefaultUtils.getLocalLocationModel(),"");
        pantFragment.reSetLocationModel(DefaultUtils.getLocalLocationModel(),"");
    }

    @Override
    protected void initServer() {

    }

    // region 寻卡
    private void openUfh() {
        uhfService.open();
        isPandingFiled.set(uhfService.inventoryStart());
    }

    private void closeUhf() {
        isPandingFiled.set(!uhfService.inventoryStop());
        uhfService.close();
        ToastUtil.showToast("已结束盘点");
    }



    //endregion
    public OnClickAdapter.onClickCommand clickPand = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            if (isPandingFiled.get()) {
                closeUhf();
                // TODO: 2023/10/18 epcList 提交服务
                for (EPC epc : epcList) {
                    System.out.println("提交服务-寻卡地址: " + epc.getId());
                }
                fragmentRefresh();
            } else {
                openUfh();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeUhf();
    }
}
