package xyz.ttyz.tourfrxohc;

import android.Manifest;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.Map;

import xyz.ttyz.mylibrary.method.BaseTouSubscriber;
import xyz.ttyz.mylibrary.method.RetrofitUtils;
import xyz.ttyz.mylibrary.method.RxOHCUtils;
import xyz.ttyz.tou_example.init.ApplicationUtils;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.adapter.utils.BaseEmptyAdapterParent;
import xyz.ttyz.toubasemvvm.adapter.utils.BaseRecyclerAdapter;
import xyz.ttyz.toubasemvvm.vm.ToolBarViewModel;
import xyz.ttyz.tourfrxohc.activity.BaseActivity;
import xyz.ttyz.tourfrxohc.databinding.ActivityMainBinding;
import xyz.ttyz.tourfrxohc.fragment.MainFragment;
import xyz.ttyz.tourfrxohc.http.BaseSubscriber;
import xyz.ttyz.tourfrxohc.models.MainModel;
import xyz.ttyz.tourfrxohc.models.ResorceModel;
import xyz.ttyz.tourfrxohc.models.UserModel;
import xyz.ttyz.tourfrxohc.viewholder.ResorceViewHolder;


public class MainActivity extends BaseActivity<ActivityMainBinding> {

    @Override
    protected int initLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected String[] initPermission() {
        return new String[]{Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};
    }

    BaseEmptyAdapterParent historyAdapter;
    ToolBarViewModel toolBarViewModel;
    @Override
    protected void initData() {
        mBinding.setContext(this);
        toolBarViewModel = new ToolBarViewModel.Builder()
                .title("首页")
                .shareClick(new OnClickAdapter.onClickCommand() {
                    @Override
                    public void click() {
                        MainFragment mainFragment = new MainFragment();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.add(R.id.container, mainFragment);
                        fragmentTransaction.commitAllowingStateLoss();
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

    }

    @Override
    protected void initServer() {
        //登录
        Map map = new HashMap();
        map.put("mobild", "17758116193");
        map.put("code", "339999");
        Map hardMap = new HashMap();
        hardMap.put("type", 0);
        hardMap.put("uuid", "1234567890987654321");
        hardMap.put("model", "web");
        hardMap.put("system", "chrome");
        map.put("hardware", hardMap);
        Map softMap = new HashMap();
        softMap.put("type", 1003);
        softMap.put("build", 100);
        map.put("software", softMap);
        new RxOHCUtils<>(this).executeApi(BaseApplication.apiService.login(RetrofitUtils.getNormalBody(map)), new BaseTouSubscriber<UserModel>(this) {
            @Override
            public void success(UserModel data) {
                if (data != null) {
                    mBinding.setUserModule(data);
                    DefaultUtils.token = data.getAccessToken();
                    loadHistory();
                }
            }

            @Override
            public String initCacheKey() {
                return null;//如果该页面涉及隐私，则不传cacheKey，就不会产生缓存数据
            }

        });
    }

    private void loadHistory(){
        //获取历史
        new RxOHCUtils<>(this).executeApi(BaseApplication.apiService.getHistory(), new BaseSubscriber<MainModel>(this, loadEnd) {
            @Override
            public void success(MainModel data) {
                if (data != null) {
                    historyAdapter.setList(data.getReadHistory());
                }
            }

            @Override
            public String initCacheKey() {
                return null;//如果该页面涉及隐私，则不传cacheKey，就不会产生缓存数据
            }

        });
    }
}


