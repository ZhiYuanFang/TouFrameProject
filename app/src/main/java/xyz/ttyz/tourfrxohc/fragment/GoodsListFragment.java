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
public class GoodsListFragment extends BaseInViewPagerFragment<FragmentGoodsListBinding, List<GoodsModel>, RecordsModule<List<GoodsModel>>> {
    int type;//表示 在库、已出库
    boolean isPant;


    LocationModel locationModel = new LocationModel();
    String searchStr = "";

    public GoodsListFragment(int type, boolean isPant) {
        this.type = type;
        this.isPant = isPant;
    }

    BaseEmptyAdapterParent adapterParent;

    //设置分区地址模糊查询字段
    public void reSetLocationModel(LocationModel locationModel, String searchStr) {
        this.locationModel = locationModel;
        // 已经加载的情况下，刷新
        this.searchStr = searchStr;
        // 已经加载的情况下，刷新
        if (adapterParent != null) {
            loadPageInfo(true);
        }
    }

    @Override
    public String initTitle() {
        if(isPant){
            switch (type) {
                case NowWait:
                    return "待盘点";
                case NowPant:
                    return "已盘点";
                default:
                    return "未知";
            }
        }
        switch (type) {
            case NowIn:
                return "当前在库";
            case NowOut:
                return "已出库";
            default:
                return "未知";
        }
    }

    @Override
    protected void initVariable(FragmentGoodsListBinding mBinding) {
        mBinding.setContext(this);
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

                ((GoodsDetailViewHolder) holder).bindData((GoodsModel) adapterParent.getItem(position), isPant);
            }
        });
        mBinding.setAdapter(adapterParent);
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
        if (!isPant) {
            return BaseApplication.apiService.goodsList(RetrofitUtils.getNormalBody(map));
        } else {
            return BaseApplication.apiService.pageCurrentDetails(RetrofitUtils.getNormalBody(map));
        }
    }

    @Override
    protected Map<String, Object> initLoadMoreParam() {
        Map map = new HashMap();
        if(isPant){
            map.put("detailStatus", type);
        } else {
            map.put("status",  type);
        }
        map.put("fuzzyGoodsActualNo", searchStr);
        map.put("warehouseAreaId", locationModel.warehouseAreaId);
        map.put("warehouseId", locationModel.warehouseId);
        return map;
    }

    @Override
    protected BaseEmptyAdapterParent initLoadPageInfoAdapter() {


        return adapterParent;
    }

    @Override
    protected void dealLoadMoreSuccess(List<GoodsModel> data) {
        adapterParent.addAll(data);
    }
}
