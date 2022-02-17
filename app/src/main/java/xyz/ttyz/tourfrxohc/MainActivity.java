package xyz.ttyz.tourfrxohc;

import android.Manifest;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.toutou.myapplication.Native;

import java.util.HashMap;
import java.util.Map;

import xyz.ttyz.mylibrary.method.RetrofitUtils;
import xyz.ttyz.mylibrary.method.RxOHCUtils;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.adapter.utils.BaseEmptyAdapterParent;
import xyz.ttyz.toubasemvvm.adapter.utils.BaseRecyclerAdapter;
import xyz.ttyz.toubasemvvm.utils.DialogUtils;
import xyz.ttyz.toubasemvvm.utils.ToastUtil;
import xyz.ttyz.toubasemvvm.vm.ToolBarViewModel;
import xyz.ttyz.tourfrxohc.activity.BaseActivity;
import xyz.ttyz.tourfrxohc.databinding.ActivityMainBinding;
import xyz.ttyz.tourfrxohc.fragment.MainFragment;
import xyz.ttyz.tourfrxohc.http.BaseSubscriber;
import xyz.ttyz.tourfrxohc.jni.JNITools;
import xyz.ttyz.tourfrxohc.models.Hardware;
import xyz.ttyz.tourfrxohc.models.MainModel;
import xyz.ttyz.tourfrxohc.models.ResorceModel;
import xyz.ttyz.tourfrxohc.models.Software;
import xyz.ttyz.tourfrxohc.models.UserModel;
import xyz.ttyz.tourfrxohc.viewholder.ResorceViewHolder;

public class MainActivity extends BaseActivity<ActivityMainBinding> {
    @Override
    protected int initLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected String[] initPermission() {
        return new String[]{
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};
    }

    //recycler适配器
    BaseEmptyAdapterParent historyAdapter;
    //标题栏
    ToolBarViewModel toolBarViewModel;
    @Override
    protected void initData() {
        mPm = getApplicationContext().getPackageManager();
        ComponentName defaultName = getComponentName();
        ComponentName double11 = new ComponentName(getBaseContext(), "xyz.ttyz.tourfrxohc.test11");
        ComponentName double12 = new ComponentName(getBaseContext(), "xyz.ttyz.tourfrxohc.test12");
        disableComponent(defaultName);
        enableComponent(double11);
        disableComponent(double12);


        hardware = new Hardware();
        software = new Software();
        mBinding.setContext(this);
        toolBarViewModel = new ToolBarViewModel.Builder()
                .rightTxt("图片")
                .rightClick(new OnClickAdapter.onClickCommand() {
                    @Override
                    public void click() {
                        DialogUtils.showDialog("点击返回按钮可以关闭图片", new DialogUtils.DialogButtonModule("显示图片", new DialogUtils.DialogClickDelegate() {
                            @Override
                            public void click(DialogUtils.DialogButtonModule dialogButtonModule) {
//                                MainFragment mainFragment = new MainFragment();
//                                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                                fragmentTransaction.add(R.id.container, mainFragment);
//                                fragmentTransaction.commitAllowingStateLoss();
//                                fragmentTransaction.addToBackStack("");



                                ToastUtil.showToast(Native.stringFromJNI(MainActivity.this));
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
    }

    @Override
    protected void initServer() {
        //同时调动两个接口，会先执行完第一个接口，再执行第二个接口，所以，虽然后一个接口需要第一个接口返回的token，但不需要在第一个接口的回调中再去执行第二个接口。
        //这是因为RfRxOHC中，做了接口顺序执行的保护
        //接口还是异步请求，但给它们做了一个执行队列。
        login();
        loadHistory();
    }

    private void login(){
        //登录
        Map map = new HashMap();
        map.put("mobile", "17758116193");
        map.put("code", "339999");
        map.put("hardware", hardware);
        map.put("software", software);
        new RxOHCUtils<>(this).executeApi(BaseApplication.apiService.login(RetrofitUtils.getNormalBody(map)), new BaseSubscriber<UserModel>(this) {
            @Override
            public void success(UserModel data) {
                if (data != null) {
                    toolBarViewModel.title.set(data.getNickname() + " 的浏览历史");
                    DefaultUtils.token = data.getAccessToken();
                }
            }

            @Override
            public String initCacheKey() {
                return "getNormalBody";//如果该页面涉及隐私，则不传cacheKey，就不会产生缓存数据
            }

        });
    }

    private void loadHistory(){
        //获取历史
        Map<String, Object> map = new HashMap<>();
        map.put("hardware", hardware);
        map.put("software", software);
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

    private PackageManager mPm;
    private void enableComponent(ComponentName componentName) {

        mPm.setComponentEnabledSetting(componentName,

                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,

                PackageManager.DONT_KILL_APP);

    }

    private void disableComponent(ComponentName componentName) {

        mPm.setComponentEnabledSetting(componentName,

                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,

                PackageManager.DONT_KILL_APP);

    }

    //region 测试接口需要，不用管
    public Hardware hardware;
    public Software software;
    //endregion
}


