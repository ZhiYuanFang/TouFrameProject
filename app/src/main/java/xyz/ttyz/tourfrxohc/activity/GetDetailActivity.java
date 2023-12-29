package xyz.ttyz.tourfrxohc.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.view.ViewGroup;

import androidx.databinding.ObservableField;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.locks.Lock;

import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.toubasemvvm.adapter.BaseGridAdapter;
import xyz.ttyz.toubasemvvm.adapter.utils.BaseRecyclerAdapter;
import xyz.ttyz.toubasemvvm.vm.ToolBarViewModel;
import xyz.ttyz.tourfrxohc.DefaultUtils;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityGetDetailBinding;
import xyz.ttyz.tourfrxohc.databinding.ActivityGetListBinding;
import xyz.ttyz.tourfrxohc.databinding.ActivityGetSuccessBinding;
import xyz.ttyz.tourfrxohc.models.CarModel;
import xyz.ttyz.tourfrxohc.utils.LockUtil;
import xyz.ttyz.tourfrxohc.utils.ToastUtils;
import xyz.ttyz.tourfrxohc.viewholder.CarSelectViewHolder;

/**
 * @author 投投
 * @date 2023/12/26
 * @email 343315792@qq.com
 */
public class GetDetailActivity extends BaseActivity<ActivityGetDetailBinding>{
    private static List<CarModel> selectList;
    public  static void show(List<CarModel> list){
        selectList = list;
        Intent intent = new Intent(ActivityManager.getInstance(), GetDetailActivity.class);
        ActivityManager.getInstance().startActivity(intent);
    }
    ToolBarViewModel toolBarViewModel;
    BaseGridAdapter adapterParent;

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
            //判断某一个门是否观赏
            for (CarModel carModel: selectList) {
                if(readArr[carModel.getDoorNumber() - 1]){
                    //有一个是开着的，就返回false
                    isAllClose = false;
                } else {
                    DefaultUtils.resetDoorWithKey(carModel.getDoorNumber(), false);
                    //todo 当前是关上了,请求接口告知 这个门空了
                }
            }

            // 判断打开的所有门已关上
            if(isAllClose){
                GetSuccessActivity.show(GetSuccessActivity.ListComing);
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
            }
        }
    }

    @Override
    protected void initData() {
        mBinding.setContext(this);
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
                return new CarSelectViewHolder(GetDetailActivity.this, parent, false, null);
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
        for (CarModel carModel : selectList) {
            adapterParent.add(carModel);
            int doorAddress = DefaultUtils.getDoorAddress(false, carModel);
            if(doorAddress > 0){
                LockUtil.getInstance(lockDelegate).openKey(doorAddress);
            } else {
                // 不应该出现
                ToastUtils.showError("当前柜没有对应存放");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LockUtil.clearCallBack();
    }
}
