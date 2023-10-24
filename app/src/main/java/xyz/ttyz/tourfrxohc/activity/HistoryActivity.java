package xyz.ttyz.tourfrxohc.activity;

import static xyz.ttyz.tourfrxohc.utils.Constans.NowPant;

import android.content.Intent;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import xyz.ttyz.mylibrary.method.BaseModule;
import xyz.ttyz.mylibrary.method.RecordsModule;
import xyz.ttyz.mylibrary.method.RetrofitUtils;
import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.toubasemvvm.adapter.utils.BaseEmptyAdapterParent;
import xyz.ttyz.toubasemvvm.adapter.utils.BaseRecyclerAdapter;
import xyz.ttyz.toubasemvvm.vm.ToolBarViewModel;
import xyz.ttyz.tourfrxohc.BaseApplication;
import xyz.ttyz.tourfrxohc.DefaultUtils;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityHistoryBinding;
import xyz.ttyz.tourfrxohc.models.PantRecordModel;
import xyz.ttyz.tourfrxohc.viewholder.PandRecordViewHolder;

/**
 * @author 投投
 * @date 2023/10/18
 * @email 343315792@qq.com
 */
public class HistoryActivity extends BaseContainLoadMoreActivity<ActivityHistoryBinding, List<PantRecordModel>, RecordsModule<List<PantRecordModel>>> {
    public static void show(){
        Intent intent = new Intent(ActivityManager.getInstance(), HistoryActivity.class);
        ActivityManager.getInstance().startActivity(intent);
    }
    ToolBarViewModel toolBarViewModel;
    BaseEmptyAdapterParent adapter;
    @Override
    protected int initLayoutId() {
        return R.layout.activity_history;
    }

    @Override
    protected String[] initPermission() {
        return new String[0];
    }

    @Override
    protected Observable<BaseModule<RecordsModule<List<PantRecordModel>>>> initApiService(Map map) {
        return BaseApplication.apiService.pageDone(RetrofitUtils.getNormalBody(map));
    }

    @Override
    protected Map<String, Object> initLoadMoreParam() {
        Map map = new HashMap();
        map.put("warehouseAreaId", DefaultUtils.getLocalLocationModel().warehouseAreaId);
        map.put("warehouseId", DefaultUtils.getLocalLocationModel().warehouseId);
        return map;
    }

    @Override
    protected void initBinding() {
        mBinding.setContext(this);
        toolBarViewModel = new ToolBarViewModel.Builder()
                .title("记录")
                .build();
        mBinding.setToolBarViewModel(toolBarViewModel);
        adapter = new BaseEmptyAdapterParent(this, new BaseRecyclerAdapter.NormalAdapterDelegate() {
            @Override
            public int getItemViewType(int position) {
                return 0;
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new PandRecordViewHolder(HistoryActivity.this, parent);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((PandRecordViewHolder)holder).bindData((PantRecordModel) adapter.getItem(position));
            }
        });
        mBinding.setAdapter(adapter);
    }

    @Override
    protected BaseEmptyAdapterParent initLoadPageInfoAdapter() {
        return adapter;
    }

    @Override
    protected void dealLoadMoreSuccess(List<PantRecordModel> data) {
        adapter.addAll(data);
    }

    @Override
    protected void initServer() {

    }
}
