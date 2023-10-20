package xyz.ttyz.tourfrxohc.activity;

import android.content.Intent;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.mylibrary.method.RecordsModule;
import xyz.ttyz.toubasemvvm.adapter.utils.BaseEmptyAdapterParent;
import xyz.ttyz.toubasemvvm.adapter.utils.BaseRecyclerAdapter;
import xyz.ttyz.tourfrxohc.DefaultUtils;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityPantDetailListBinding;
import xyz.ttyz.tourfrxohc.models.GoodsModel;
import xyz.ttyz.tourfrxohc.viewholder.GoodsDetailViewHolder;

/**
 * @author 投投
 * @date 2023/10/20
 * @email 343315792@qq.com
 */
public class PantDetailListActivity extends BaseContainLoadMoreActivity<ActivityPantDetailListBinding, List<GoodsModel>, RecordsModule<List<GoodsModel>>>{
    public static void show(RecordsModule recordsModule){
        Intent intent = new Intent(ActivityManager.getInstance(), PantDetailListActivity.class);
        intent.putExtra("id", 0);
        ActivityManager.getInstance().startActivity(intent);
    }
    long id;
    BaseEmptyAdapterParent adapterParent;
    @Override
    protected void initData() {
        id = getIntent().getLongExtra("id", 0);
        super.initData();
    }

    @Override
    protected Observable<RecordsModule<List<GoodsModel>>> initApiService(Map map) {
        return null;
    }

    @Override
    protected Map<String, Object> initLoadMoreParam() {
        Map map = new HashMap();
        map.put("id", id);
        map.put("warehouseAreaId", DefaultUtils.getLocalLocationModel().warehouseAreaId);
        map.put("warehouseId", DefaultUtils.getLocalLocationModel().warehouseId);
        return map;
    }

    @Override
    protected BaseEmptyAdapterParent initLoadPageInfoAdapter() {
        mBinding.setContext(this);
        adapterParent = new BaseEmptyAdapterParent(this, new BaseRecyclerAdapter.NormalAdapterDelegate() {
            @Override
            public int getItemViewType(int position) {
                return 0;
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new GoodsDetailViewHolder(PantDetailListActivity.this, parent);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((GoodsDetailViewHolder)holder).bindData((GoodsModel) adapterParent.getItem(position), true);
            }
        });
        mBinding.setAdapter(adapterParent);
        return adapterParent;
    }

    @Override
    protected void dealLoadMoreSuccess(List<GoodsModel> data) {
        adapterParent.addAll(data);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_pant_detail_list;
    }

    @Override
    protected String[] initPermission() {
        return new String[0];
    }

    @Override
    protected void initServer() {

    }
}
