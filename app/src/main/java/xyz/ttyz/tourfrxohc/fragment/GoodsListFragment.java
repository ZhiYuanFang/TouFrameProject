package xyz.ttyz.tourfrxohc.fragment;

import static xyz.ttyz.tourfrxohc.utils.Constans.*;

import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import xyz.ttyz.mylibrary.method.RecordsModule;
import xyz.ttyz.mylibrary.method.RetrofitUtils;
import xyz.ttyz.toubasemvvm.adapter.utils.BaseEmptyAdapterParent;
import xyz.ttyz.toubasemvvm.adapter.utils.BaseRecyclerAdapter;
import xyz.ttyz.tourfrxohc.BaseApplication;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.FragmentGoodsListBinding;
import xyz.ttyz.tourfrxohc.models.GoodsModel;
import xyz.ttyz.tourfrxohc.models.LocationModel;
import xyz.ttyz.tourfrxohc.viewholder.GoodsDetailViewHolder;

/**
 * @author 投投
 * @date 2023/10/18
 * @email 343315792@qq.com
 */
public class GoodsListFragment extends BaseInViewPagerFragment<FragmentGoodsListBinding,List<GoodsModel>, RecordsModule<List<GoodsModel>>> {
    int type;//表示 在库、已出库、待盘点、已盘点


    LocationModel locationModel = new LocationModel();
    String searchStr = "";

    public GoodsListFragment(int type) {
       this.type = type;
    }

    BaseEmptyAdapterParent adapterParent;

    //设置分区地址
    public void reSetLocationModel(LocationModel locationModel) {
        this.locationModel = locationModel;
        // 已经加载的情况下，刷新
        if(isUIVisible && isViewCreated){
            loadPageInfo(true);
        }
    }
    //设置模糊查询字段
    public void reSetSearchStr(String searchStr) {
        this.searchStr = searchStr;
        // 已经加载的情况下，刷新
        if(isUIVisible && isViewCreated){
            loadPageInfo(true);
        }
    }
    @Override
    public String initTitle() {
        switch (type){
            case NowIn:
                return "当前在库";
            case NowOut:
                return "已出库";
            case NowWait:
                return "待盘点";
            case NowPant:
                return "已盘点";
            default:return "未知";
        }
    }

    @Override
    protected void initVariable(FragmentGoodsListBinding mBinding) {
        mBinding.setContext(this);
        mBinding.setAdapter(initLoadPageInfoAdapter());
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_goods_list;
    }

    @Override
    protected String[] initPermission() {
        return new String[0];
    }

    @Nullable
    @Override
    protected Observable initApiService(Map map) {
        return BaseApplication.apiService.goodsList(RetrofitUtils.getNormalBody(map));
    }

    @Override
    protected Map<String, Object> initLoadMoreParam() {
        Map map = new HashMap();
        map.put("status", type);
        map.put("fuzzyGoodsActualNo", searchStr);
        map.put("warehouseAreaId", locationModel.warehouseAreaId);
        map.put("warehouseId", locationModel.warehouseId);
        return map;
    }

    @Override
    protected BaseEmptyAdapterParent initLoadPageInfoAdapter() {
        adapterParent = new BaseEmptyAdapterParent(getContext(), new BaseRecyclerAdapter.NormalAdapterDelegate() {
            @Override
            public int getItemViewType(int position) {
                return 0;
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new GoodsDetailViewHolder(getContext(), parent);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

                ((GoodsDetailViewHolder)holder).bindData((GoodsModel) adapterParent.getItem(position));
            }
        });

        return adapterParent;
    }

    @Override
    protected void dealLoadMoreSuccess(List<GoodsModel> data) {
        adapterParent.addAll(data);
    }
}
