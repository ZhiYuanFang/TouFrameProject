package xyz.ttyz.tourfrxohc.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.view.ViewGroup;

import androidx.databinding.ObservableField;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;

import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.mylibrary.method.BaseModule;
import xyz.ttyz.mylibrary.method.HttpDefaultUtils;
import xyz.ttyz.mylibrary.method.RetrofitUtils;
import xyz.ttyz.mylibrary.method.RxOHCUtils;
import xyz.ttyz.toubasemvvm.adapter.BaseGridAdapter;
import xyz.ttyz.toubasemvvm.adapter.utils.BaseRecyclerAdapter;
import xyz.ttyz.toubasemvvm.vm.ToolBarViewModel;
import xyz.ttyz.tourfrxohc.BaseApplication;
import xyz.ttyz.tourfrxohc.DefaultUtils;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityGetDetailBinding;
import xyz.ttyz.tourfrxohc.databinding.ActivityGetListBinding;
import xyz.ttyz.tourfrxohc.databinding.ActivityGetSuccessBinding;
import xyz.ttyz.tourfrxohc.http.BaseSubscriber;
import xyz.ttyz.tourfrxohc.models.CarModel;
import xyz.ttyz.tourfrxohc.utils.LockUtil;
import xyz.ttyz.tourfrxohc.utils.PwdUtils;
import xyz.ttyz.tourfrxohc.utils.TimeUtils;
import xyz.ttyz.tourfrxohc.utils.ToastUtils;
import xyz.ttyz.tourfrxohc.viewholder.CarSelectViewHolder;

/**
 * @author 投投
 * @date 2023/12/26
 * @email 343315792@qq.com
 */
public class GetDetailActivity extends BaseActivity<ActivityGetDetailBinding>{
    private static List<CarModel> selectList;
    public  static void show(List<CarModel> list, String keyOutBoxCheckCode){
        selectList = list;
        Intent intent = new Intent(ActivityManager.getInstance(), GetDetailActivity.class);
        intent.putExtra("keyOutBoxCheckCode", keyOutBoxCheckCode);
        ActivityManager.getInstance().startActivity(intent);
    }
    ToolBarViewModel toolBarViewModel;
    BaseGridAdapter adapterParent;
    String keyOutBoxCheckCode;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_get_detail;
    }

    @Override
    protected String[] initPermission() {
        return new String[0];
    }

    LockUtil.LockDelegate lockDelegate = new LockUtil.LockDelegate() {
        @Override
        public void callBackOpen(int keyNumber) {
            setCurOpen(keyNumber);
        }

        @Override
        public void callBackState(boolean[] readArr) {
            boolean isAllClose = true;
            //判断某一个门是否关上
            for (CarModel carModel: selectList) {
                boolean isFirstCloseBack = carModel.isOpen();
                carModel.setOpen(readArr[carModel.getDoorNumber() - 1]);
                if(carModel.isOpen()){
                    //有一个是开着的
                    //,并且不是故障柜，就返回false
                    if(!carModel.isErrorDoor){
                        isAllClose = false;
                    }
                } else {
                    // 当前是关上了,请求接口告知
                    //第一次检查到关柜成功
                    if(isFirstCloseBack){
                        Map map = new HashMap();
                        map.put("boxNum", carModel.getDoorNumber());
                        map.put("keyCabinetId", PwdUtils.getWareHouseCode());
                        map.put("keyCabinetType", 2);
                        map.put("keyId", carModel.keyId);
                        map.put("takeOutBoxKeyDate", TimeUtils.getServerNeedDate());
                        map.put("keyOutBoxCheckCode", keyOutBoxCheckCode);
                        new RxOHCUtils<>(ActivityManager.getInstance()).executeApi(BaseApplication.apiService.afterTakeOutBoxKey(RetrofitUtils.getNormalBody(map)), new BaseSubscriber<Object>(null) {//不要跟随取消接口
                            @Override
                            public void success(Object data) {

                            }

                            @Override
                            protected void fail(BaseModule<Object> baseModule) {
                                //重复开门会造成错误提示，不提示即可
                            }
                        });
                    }
                }
            }

            // 判断打开的所有门已关上
            if(isAllClose){
                GetSuccessActivity.show(GetSuccessActivity.ListComing, keyOutBoxCheckCode);
                finish();
            }


        }
    };

    // 设置当前状态已开
    private void setCurOpen(int doorNumber){
        // 根据keyNumber推断是哪个model
        for (CarModel carModel: selectList) {
            if(carModel.getDoorNumber() == doorNumber){
                carModel.setOpen(true);
                //当前门已开
                DefaultUtils.resetDoorWithKey(carModel.getDoorNumber(), false);
                //将密码门关系删除
                PwdUtils.removeDoor(carModel.getDoorNumber());
            }
        }


    }

    @Override
    protected void initData() {
        mBinding.setContext(this);
        keyOutBoxCheckCode = getIntent().getStringExtra("keyOutBoxCheckCode");
        toolBarViewModel = new ToolBarViewModel.Builder()
                .backClick(null)
                .title("确定取钥匙的车辆")
                .build();
        mBinding.setToolBarViewModel(toolBarViewModel);
        adapterParent = new BaseGridAdapter(this,2, new BaseRecyclerAdapter.NormalAdapterDelegate() {
            @Override
            public int getItemViewType(int position) {
                return 0;
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new CarSelectViewHolder(GetDetailActivity.this, parent, false, new CarSelectViewHolder.CarSelectDelegate() {
                    @Override
                    public void selectItem(CarModel carModel) {
                        // 详情没有
                    }

                    @Override
                    public void setErrorDoor(CarModel carModel) {
                        DefaultUtils.setErrorDoor(carModel.getDoorNumber());
                        ((CarModel)adapterParent.getItem(adapterParent.getData().indexOf(carModel))).isErrorDoor = true;
                        ToastUtils.showSuccess("设置故障柜成功");
                        //状态回退
                        Map map = new HashMap();
                        map.put("keyCabinetId", PwdUtils.getWareHouseCode());
                        map.put("keyId", carModel.keyId);
                        new RxOHCUtils<>(ActivityManager.getInstance()).executeApi(BaseApplication.apiService.cancelBeforeOutBoxKey(RetrofitUtils.getNormalBody(map)), new BaseSubscriber<Object>(GetDetailActivity.this) {
                            @Override
                            public void success(Object data) {

                            }
                        });
                    }

                    @Override
                    public void retry(CarModel carModel) {
                        LockUtil.getInstance(lockDelegate).openKey(carModel.getDoorNumber());
                    }
                });
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((CarSelectViewHolder)holder).bindData((CarModel) adapterParent.getItem(position));
            }
        }){
            @Override
            protected int initDis() {
                return 9;
            }
        };
        mBinding.setAdapter(adapterParent);


    }

    @Override
    protected void initServer() {
        List<Object> mapList = new ArrayList<>();

        for (CarModel carModel : selectList) {
            adapterParent.add(carModel);
            int doorAddress = DefaultUtils.getDoorAddress(false, carModel);
            if(doorAddress > 0){
                LockUtil.getInstance(lockDelegate).openKey(doorAddress);
            } else {
                // 不应该出现
                ToastUtils.showError("当前柜没有对应存放");
            }


            //开柜前状态告知
            Map map = new HashMap();
            map.put("boxNum", doorAddress);
            map.put("keyCabinetId", PwdUtils.getWareHouseCode());
            map.put("keyCabinetType", 2);
            map.put("applyTakeOutBoxKeyDate", TimeUtils.getServerNeedDate());
            map.put("keyOutBoxCheckCode", keyOutBoxCheckCode);
            map.put("keyId", carModel.keyId);
            map.put("staffId", carModel.staffId);

            mapList.add(map);
        }

        //开柜前状态告知
        new RxOHCUtils<>(ActivityManager.getInstance()).executeApi(BaseApplication.apiService.applyTakeOutBoxKey(RetrofitUtils.getNormalBody(mapList)), new BaseSubscriber<Object>(GetDetailActivity.this) {
            @Override
            public void success(Object data) {

            }
        });
    }
}
