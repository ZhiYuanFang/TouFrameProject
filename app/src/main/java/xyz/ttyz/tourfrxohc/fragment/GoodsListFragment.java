package xyz.ttyz.tourfrxohc.fragment;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.Map;

import io.reactivex.Observable;
import xyz.ttyz.toubasemvvm.adapter.utils.BaseEmptyAdapterParent;
import xyz.ttyz.toubasemvvm.adapter.utils.BaseRecyclerAdapter;
import xyz.ttyz.tourfrxohc.BaseApplication;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.FragmentGoodsListBinding;
import xyz.ttyz.tourfrxohc.models.GoodsModel;
import xyz.ttyz.tourfrxohc.viewholder.GoodsDetailViewHolder;

/**
 * @author 投投
 * @date 2023/10/18
 * @email 343315792@qq.com
 */
public class GoodsListFragment extends BaseInViewPagerFragment<FragmentGoodsListBinding> {
    int type;//表示 在库、已出库、待盘点、已盘点

    public final static int NowIn = 1;
    public final static int NowOut = 12;
    public final static int NowWait = 13;
    public final static int NowPant = 14;



    public GoodsListFragment(int type) {
       this.type = type;
    }

    BaseEmptyAdapterParent adapterParent;
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
    protected int initLayoutId() {
        return R.layout.fragment_goods_list;
    }

    @Override
    protected String[] initPermission() {
        return new String[0];
    }

    @Override
    protected void initData() {
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
        mBinding.setAdapter(adapterParent);
    }

    @Override
    protected Observable initApiService() {
        return BaseApplication.apiService.goodsList();
    }

    @Override
    protected Map<String, Object> initLoadMoreParam() {
        return null;
    }

    @Override
    protected BaseEmptyAdapterParent initLoadPageInfoAdapter() {
        return null;
    }

    @Override
    protected void dealLoadMoreSuccess(Object data) {

    }

    @Override
    protected void initServer() {

    }

    @Override
    public LifecycleTransformer bindUntilEvent(Object event) {
        return null;
    }
}
