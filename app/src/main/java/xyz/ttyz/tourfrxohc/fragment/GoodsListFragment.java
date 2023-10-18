package xyz.ttyz.tourfrxohc.fragment;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import xyz.ttyz.toubasemvvm.adapter.utils.BaseEmptyAdapterParent;
import xyz.ttyz.toubasemvvm.adapter.utils.BaseRecyclerAdapter;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.FragmentGoodsListBinding;

/**
 * @author 投投
 * @date 2023/10/18
 * @email 343315792@qq.com
 */
public class GoodsListFragment extends BaseInViewPagerFragment<FragmentGoodsListBinding> {
    boolean isComming = false;

    public GoodsListFragment(boolean isComming) {
        this.isComming = isComming;
    }

    BaseEmptyAdapterParent adapterParent;
    @Override
    public String initTitle() {
        return isComming ? "当前在库" : "已出库";
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
                return null;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            }
        });
        mBinding.setAdapter(adapterParent);
    }

    @Override
    protected void initServer() {

    }
}
