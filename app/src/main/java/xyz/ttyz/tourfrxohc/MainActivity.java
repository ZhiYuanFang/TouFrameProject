package xyz.ttyz.tourfrxohc;

import android.Manifest;
import android.content.Intent;
import android.view.ViewGroup;

import androidx.databinding.ObservableField;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import xyz.ttyz.mylibrary.method.RetrofitUtils;
import xyz.ttyz.mylibrary.method.RxOHCUtils;
import xyz.ttyz.mylibrary.protect.SharedPreferenceUtil;
import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.adapter.utils.BaseEmptyAdapterParent;
import xyz.ttyz.toubasemvvm.adapter.utils.BaseRecyclerAdapter;
import xyz.ttyz.toubasemvvm.utils.DialogUtils;
import xyz.ttyz.toubasemvvm.vm.ToolBarViewModel;
import xyz.ttyz.tourfrxohc.activity.BaseActivity;
import xyz.ttyz.tourfrxohc.activity.ComingActivity;
import xyz.ttyz.tourfrxohc.activity.LoginActivity;
import xyz.ttyz.tourfrxohc.databinding.ActivityMainBinding;
import xyz.ttyz.tourfrxohc.dialog.LocationDialog;
import xyz.ttyz.tourfrxohc.fragment.MainFragment;
import xyz.ttyz.tourfrxohc.http.BaseSubscriber;
import xyz.ttyz.tourfrxohc.models.Hardware;
import xyz.ttyz.tourfrxohc.models.LocationModel;
import xyz.ttyz.tourfrxohc.models.MainModel;
import xyz.ttyz.tourfrxohc.models.ResorceModel;
import xyz.ttyz.tourfrxohc.models.Software;
import xyz.ttyz.tourfrxohc.models.UserModel;
import xyz.ttyz.tourfrxohc.viewholder.ResorceViewHolder;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    public static void goMain () {
        Intent intent = new Intent(ActivityManager.getInstance(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityManager.getInstance().startActivity(intent);
    }

    public ObservableField<String> currentNumber = new ObservableField<String>("");

    public ObservableField<String> realNumber = new ObservableField<String>("");
    public ObservableField<LocationModel> locationModelObservableField = new ObservableField<>(new LocationModel(-1, "请选择", "店铺"));
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
                .rightClick(new OnClickAdapter.onClickCommand() {
                    @Override
                    public void click() {
                        DialogUtils.showDialog("点击返回按钮可以关闭图片", new DialogUtils.DialogButtonModule("显示图片", new DialogUtils.DialogClickDelegate() {
                            @Override
                            public void click(DialogUtils.DialogButtonModule dialogButtonModule) {
                                MainFragment mainFragment = new MainFragment();
                                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.add(R.id.container, mainFragment);
                                fragmentTransaction.commitAllowingStateLoss();
                                fragmentTransaction.addToBackStack("");
                            }
                        }));
                    }
                })
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
        LocationModel local = new Gson().fromJson(SharedPreferenceUtil.getShareString(ActivityManager.getInstance(), "location"), LocationModel.class);
        if(local != null){
            locationModelObservableField.set(local);
        }
    }

    @Override
    protected void initServer() {
        //同时调动两个接口，会先执行完第一个接口，再执行第二个接口，所以，虽然后一个接口需要第一个接口返回的token，但不需要在第一个接口的回调中再去执行第二个接口。
        //这是因为RfRxOHC中，做了接口顺序执行的保护
        //接口还是异步请求，但给它们做了一个执行队列。
    }

    private void loadHistory(){
        //获取历史
        Map<String, Object> map = new HashMap<>();
        new RxOHCUtils<>(this).executeApi(BaseApplication.apiService.getHistory(map), new BaseSubscriber<MainModel>(this, loadEnd) {
            @Override
            public void success(MainModel data) {
                if (data != null) {
                    historyAdapter.setList(data.getReadHistory());
                }
            }

            @Override
            public String initCacheKey() {
                return "getHistory";//如果该页面涉及隐私，则不传cacheKey，就不会产生缓存数据
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
                    SharedPreferenceUtil.setShareString(ActivityManager.getInstance(), "location", new Gson().toJson(locationModel));
                }
            });
        }
    };

    public OnClickAdapter.onClickCommand clickLogout = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            new RxOHCUtils<>(MainActivity.this).executeApi(BaseApplication.apiService.logout(), new BaseSubscriber<MainModel>(MainActivity.this) {
                @Override
                public void success(MainModel data) {
                    DefaultUtils.removeCookie();
                    LoginActivity.toLogin();
                }
            });
        }
    };

    public OnClickAdapter.onClickCommand clickComing = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            // 入库
            ComingActivity.show(locationModelObservableField.get());
        }
    };

    public OnClickAdapter.onClickCommand clickFilter = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            // TODO: 2023/10/12 盘库
        }
    };

    public OnClickAdapter.onClickCommand clickOut = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            // TODO: 2023/10/12 出库
        }
    };
}


