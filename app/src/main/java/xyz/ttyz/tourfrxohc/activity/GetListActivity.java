package xyz.ttyz.tourfrxohc.activity;

import android.content.Intent;
import android.view.ViewGroup;

import androidx.databinding.ObservableBoolean;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.toubasemvvm.adapter.BaseGridAdapter;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.adapter.utils.BaseEmptyAdapterParent;
import xyz.ttyz.toubasemvvm.adapter.utils.BaseRecyclerAdapter;
import xyz.ttyz.toubasemvvm.vm.ToolBarViewModel;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityGetListBinding;
import xyz.ttyz.tourfrxohc.models.CarModel;
import xyz.ttyz.tourfrxohc.utils.ToastUtils;
import xyz.ttyz.tourfrxohc.viewholder.CarSelectViewHolder;

/**
 * @author 投投
 * @date 2023/12/26
 * @email 343315792@qq.com
 */
public class GetListActivity extends BaseActivity<ActivityGetListBinding>{
    public static void show(CarModel carModel){
        Intent intent = new Intent(ActivityManager.getInstance(), GetListActivity.class);
        intent.putExtra("carModel", carModel);
        ActivityManager.getInstance().startActivity(intent);
    }
    ToolBarViewModel toolBarViewModel;
    BaseGridAdapter adapterParent;
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
    protected void onResume() {
        super.onResume();
        initServer();
    }

    @Override
    protected void initServer() {
        adapterParent.clear();
        for(int i = 0 ; i < 5; i ++){
            adapterParent.add(new CarModel());
        }
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
                GetDetailActivity.show(selectModelList);
        }
    };
}
