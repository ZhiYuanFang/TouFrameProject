package xyz.ttyz.tourfrxohc.activity;

import android.content.Intent;
import android.view.ViewGroup;

import androidx.databinding.ObservableBoolean;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.mylibrary.method.RetrofitUtils;
import xyz.ttyz.mylibrary.method.RxOHCUtils;
import xyz.ttyz.toubasemvvm.adapter.BaseGridAdapter;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.adapter.utils.BaseEmptyAdapterParent;
import xyz.ttyz.toubasemvvm.adapter.utils.BaseRecyclerAdapter;
import xyz.ttyz.toubasemvvm.vm.ToolBarViewModel;
import xyz.ttyz.tourfrxohc.BaseApplication;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityGetListBinding;
import xyz.ttyz.tourfrxohc.http.BaseSubscriber;
import xyz.ttyz.tourfrxohc.models.CarModel;
import xyz.ttyz.tourfrxohc.utils.PwdUtils;
import xyz.ttyz.tourfrxohc.utils.ToastUtils;
import xyz.ttyz.tourfrxohc.viewholder.CarSelectViewHolder;

/**
 * @author 投投
 * @date 2023/12/26
 * @email 343315792@qq.com
 */
public class GetListActivity extends BaseActivity<ActivityGetListBinding>{
    public static void show(String keyOutBoxCheckCode){
        Intent intent = new Intent(ActivityManager.getInstance(), GetListActivity.class);
        intent.putExtra("keyOutBoxCheckCode", keyOutBoxCheckCode);
        ActivityManager.getInstance().startActivity(intent);
    }
    ToolBarViewModel toolBarViewModel;
    BaseGridAdapter adapterParent;
    String keyOutBoxCheckCode;
    @Override
    protected int initLayoutId() {
        return R.layout.activity_get_list;
    }

    @Override
    protected String[] initPermission() {
        return new String[0];
    }

    @Override
    protected void initData() {
        mBinding.setContext(this);
        keyOutBoxCheckCode = getIntent().getStringExtra("keyOutBoxCheckCode");
        toolBarViewModel = new ToolBarViewModel.Builder()
                .title("取用列表")
                .build();
        mBinding.setToolBarViewModel(toolBarViewModel);
        adapterParent = new BaseGridAdapter(this,2, new BaseRecyclerAdapter.NormalAdapterDelegate() {
            @Override
            public int getItemViewType(int position) {
                return 0;
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new CarSelectViewHolder(GetListActivity.this, parent, new CarSelectViewHolder.CarSelectDelegate() {
                    @Override
                    public void selectItem(CarModel carModel) {
                        carModel.setSelect(!carModel.isSelect());
                        if(carModel.isSelect()){
                            // 判断其它是否也选中了
                            boolean allSelect = true;
                            for (CarModel cur: (List<CarModel>)adapterParent.getData()) {
                                if(!cur.isSelect()){
                                    allSelect = false;
                                    break;
                                }
                            }
                            isSelectAllFiled.set(allSelect);
                        } else {
                            isSelectAllFiled.set(false);
                        }
                    }

                    @Override
                    public void setErrorDoor(CarModel carModel) {
                        // 列表没有
                    }

                    @Override
                    public void retry(CarModel carModel) {
                        // 列表没有
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
    protected void init() {
        initData();
        //不要请求接口
    }

    @Override
    protected void onResume() {
        super.onResume();
        initServer();
    }

    @Override
    protected void initServer() {
        Map map = new HashMap();
        map.put("keyCabinetType", 2);
        map.put("keyOutBoxCheckCode", keyOutBoxCheckCode);
        map.put("keyCabinetId", PwdUtils.getWareHouseCode());
        new RxOHCUtils<>(ActivityManager.getInstance()).executeApi(BaseApplication.apiService.canOutBoxKeyList(RetrofitUtils.getNormalBody(map)), new BaseSubscriber<List<CarModel>>(GetListActivity.this) {
            @Override
            public void success(List<CarModel> data) {
                if(data == null || data.isEmpty()){
                    finish();
                    ToastUtils.showError("已无待取用列表");
                    return;
                }
                adapterParent.setList(data);
            }
        });
    }

    public ObservableBoolean isSelectAllFiled = new ObservableBoolean(false);
    public OnClickAdapter.onClickCommand onClickSelectAll = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            for (CarModel carModel: (List<CarModel>)adapterParent.getData()) {
                carModel.setSelect(!isSelectAllFiled.get());
            }
            isSelectAllFiled.set(!isSelectAllFiled.get());
        }
    };

    public OnClickAdapter.onClickCommand onClickConfirm = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            List<CarModel> selectModelList = new ArrayList<>();
            int address = 0;
            for (CarModel carModel: (List<CarModel>)adapterParent.getData()) {
                if(carModel.isSelect()){
                    carModel.setDoorNumber(++address);
                    selectModelList.add(carModel);
                }
            }
            if(selectModelList.isEmpty()){
                ToastUtils.showError("请选择");
            } else
                GetDetailActivity.show(selectModelList, keyOutBoxCheckCode);
        }
    };
}
