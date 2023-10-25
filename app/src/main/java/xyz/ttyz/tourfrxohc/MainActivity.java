package xyz.ttyz.tourfrxohc;

import android.Manifest;
import android.content.Intent;
import android.view.ViewGroup;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import xyz.ttyz.mylibrary.method.BaseModule;
import xyz.ttyz.mylibrary.method.RetrofitUtils;
import xyz.ttyz.mylibrary.method.RxOHCUtils;
import xyz.ttyz.mylibrary.protect.SharedPreferenceUtil;
import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.adapter.utils.BaseEmptyAdapterParent;
import xyz.ttyz.toubasemvvm.adapter.utils.BaseRecyclerAdapter;
import xyz.ttyz.toubasemvvm.event.NetEvent;
import xyz.ttyz.toubasemvvm.utils.DialogUtils;
import xyz.ttyz.toubasemvvm.vm.ToolBarViewModel;
import xyz.ttyz.tourfrxohc.activity.BaseActivity;
import xyz.ttyz.tourfrxohc.activity.ComingActivity;
import xyz.ttyz.tourfrxohc.activity.LoginActivity;
import xyz.ttyz.tourfrxohc.activity.PandingActivity;
import xyz.ttyz.tourfrxohc.activity.WebActivity;
import xyz.ttyz.tourfrxohc.databinding.ActivityMainBinding;
import xyz.ttyz.tourfrxohc.dialog.LocationDialog;
import xyz.ttyz.tourfrxohc.event.LocationSelectEvent;
import xyz.ttyz.tourfrxohc.http.BaseSubscriber;
import xyz.ttyz.tourfrxohc.models.Hardware;
import xyz.ttyz.tourfrxohc.models.LocationModel;
import xyz.ttyz.tourfrxohc.models.MainModel;
import xyz.ttyz.tourfrxohc.models.ResorceModel;
import xyz.ttyz.tourfrxohc.models.Software;
import xyz.ttyz.tourfrxohc.models.StatisticsModel;
import xyz.ttyz.tourfrxohc.models.UserModel;
import xyz.ttyz.tourfrxohc.utils.Constans;
import xyz.ttyz.tourfrxohc.viewholder.ResorceViewHolder;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    public static void goMain () {
        Intent intent = new Intent(ActivityManager.getInstance(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityManager.getInstance().startActivity(intent);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void locationChange(LocationSelectEvent locationSelectEvent){
        locationModelObservableField.set(DefaultUtils.getLocalLocationModel());
    }

    public ObservableField<LocationModel> locationModelObservableField = new ObservableField<>(new LocationModel());
    @Override
    protected int initLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected String[] initPermission() {
        return new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};
    }

    //recycler适配器
    BaseEmptyAdapterParent historyAdapter;
    //标题栏
    ToolBarViewModel toolBarViewModel;
    @Override
    protected void initData() {
        mBinding.setContext(this);
        toolBarViewModel = new ToolBarViewModel.Builder()
                .title(getString(R.string.app_name))
                .backClick(null)
                .build();
        mBinding.setToolBarViewModel(toolBarViewModel);
        historyAdapter = new BaseEmptyAdapterParent(this, new BaseRecyclerAdapter.NormalAdapterDelegate() {
            @Override
            public int getItemViewType(int position) {
                return 0;
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ResorceViewHolder(MainActivity.this, parent);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((ResorceViewHolder)holder).bindData((ResorceModel) historyAdapter.getItem(position));
            }
        });
        mBinding.setAdapter(historyAdapter);
        locationModelObservableField.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                //地址发生改变，获取最新数据
                refreshStatistics();
            }
        });
        locationModelObservableField.set(DefaultUtils.getLocalLocationModel());

        mBinding.setStatisticsModel(new StatisticsModel());

        if(DefaultUtils.getCookie() == null){
            LoginActivity.toLogin();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshStatistics();
    }

    @Override
    protected void initServer() {

    }

    private void refreshStatistics(){
        if(locationModelObservableField.get().warehouseAreaId < 0) return;
        //首屏的在库货品统计信息
        Map map = new HashMap();
        map.put("warehouseAreaId", locationModelObservableField.get().warehouseAreaId);
        map.put("warehouseId", locationModelObservableField.get().warehouseId);
        new RxOHCUtils<>(this).executeApi(BaseApplication.apiService.stockedStatistics(RetrofitUtils.getNormalBody(map)), new BaseSubscriber<StatisticsModel>(this) {
            @Override
            public void success(StatisticsModel data) {
                mBinding.setStatisticsModel(data);
            }
        });
    }

    public OnClickAdapter.onClickCommand clickLocation = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            LocationDialog.showDialog(new LocationDialog.CallBackDelegate() {
                @Override
                public void select(LocationModel locationModel) {
                    locationModelObservableField.set(locationModel);
                    DefaultUtils.setLocationModel(locationModel);
                }
            });
        }
    };

    public OnClickAdapter.onClickCommand clickLogout = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            new RxOHCUtils<>(MainActivity.this).executeApi(BaseApplication.apiService.logout(), new BaseSubscriber<Boolean>(MainActivity.this) {
                @Override
                public void success(Boolean data) {
                    LoginActivity.toLogin();
                }

                @Override
                protected void fail(BaseModule<Boolean> baseModule) {
                    if(baseModule.getCode() == 302){
                        LoginActivity.toLogin();
                    } else super.fail(baseModule);
                }
            });
        }
    };

    public OnClickAdapter.onClickCommand clickComing = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            // 入库
            ComingActivity.show(Constans.NowIn);
        }
    };

    public OnClickAdapter.onClickCommand clickFilter = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            // 盘库
            PandingActivity.show();
        }
    };

    public OnClickAdapter.onClickCommand clickOut = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            // 出库
            ComingActivity.show(Constans.NowOut);
        }
    };
}


