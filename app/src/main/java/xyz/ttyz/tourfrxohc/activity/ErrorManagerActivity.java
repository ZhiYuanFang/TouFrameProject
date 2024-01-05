package xyz.ttyz.tourfrxohc.activity;

import android.content.Intent;
import android.util.Log;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;

import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.mylibrary.method.RetrofitUtils;
import xyz.ttyz.mylibrary.method.RxOHCUtils;
import xyz.ttyz.toubasemvvm.adapter.utils.BaseEmptyAdapterParent;
import xyz.ttyz.toubasemvvm.adapter.utils.BaseRecyclerAdapter;
import xyz.ttyz.toubasemvvm.utils.DialogUtils;
import xyz.ttyz.toubasemvvm.vm.ToolBarViewModel;
import xyz.ttyz.tourfrxohc.BaseApplication;
import xyz.ttyz.tourfrxohc.DefaultUtils;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityErrorManagerBinding;
import xyz.ttyz.tourfrxohc.http.BaseSubscriber;
import xyz.ttyz.tourfrxohc.models.CarModel;
import xyz.ttyz.tourfrxohc.utils.LockUtil;
import xyz.ttyz.tourfrxohc.utils.PwdUtils;
import xyz.ttyz.tourfrxohc.viewholder.ErrorItemViewHolder;

/**
 * @author 投投
 * @date 2024/1/3
 * @email 343315792@qq.com
 */
public class ErrorManagerActivity extends BaseActivity<ActivityErrorManagerBinding>{
    public static void show(){
        ActivityManager.getInstance().startActivity(new Intent(ActivityManager.getInstance(), ErrorManagerActivity.class));
    }

    private static final String TAG = "ErrorManagerActivity";
    ToolBarViewModel toolBarViewModel;
    BaseEmptyAdapterParent adapterParent;
    @Override
    protected int initLayoutId() {
        return R.layout.activity_error_manager;
    }

    @Override
    protected String[] initPermission() {
        return new String[0];
    }

    @Override
    protected void initData() {
        mBinding.setContext(this);
        toolBarViewModel = new ToolBarViewModel.Builder()
                .title("故障柜管理")
                .build();
        mBinding.setToolBarViewModel(toolBarViewModel);
        adapterParent = new BaseEmptyAdapterParent(this, new BaseRecyclerAdapter.NormalAdapterDelegate() {
            @Override
            public int getItemViewType(int position) {
                return 0;
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ErrorItemViewHolder(ErrorManagerActivity.this, parent, new ErrorItemViewHolder.ErrorItemDelegate() {
                    @Override
                    public void normalItem(CarModel carModel) {
                        DialogUtils.showDialog("确定将柜门" + carModel.getDoorNumber() + "号设置为正常柜？", new DialogUtils.DialogButtonModule("确认", new DialogUtils.DialogClickDelegate() {
                            @Override
                            public void click(DialogUtils.DialogButtonModule dialogButtonModule) {
                                DefaultUtils.removeErrorDoor(carModel.getDoorNumber());
                                adapterParent.remove(carModel);
                            }
                        }));
                    }
                });
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((ErrorItemViewHolder)holder).bindData((CarModel) adapterParent.getItem(position));
            }
        });
        mBinding.setAdapter(adapterParent);
    }

    @Override
    protected void initServer() {
        adapterParent.clear();
        for (String door : DefaultUtils.getErrorKeyList()) {
            CarModel carModel = new CarModel();
            try {
                carModel.setDoorNumber(Integer.parseInt(door));
                carModel.carUnique = door;
                adapterParent.add(carModel);
            } catch (NumberFormatException e) {
                throw new RuntimeException(e);
            }
        }

        if(adapterParent.getData().size() > 0){
            //初始化状态
            LockUtil.getInstance(new LockUtil.LockDelegate() {
                @Override
                public void callBackOpen(int keyNumber) {

                }

                @Override
                public void callBackState(boolean[] readArr) {
                    for (CarModel carModel : (List<CarModel>)adapterParent.getData()) {
                        carModel.setOpen(readArr[carModel.getDoorNumber() - 1]);
                    }
                }
            }).readAllKeyState();
        }
    }
}
