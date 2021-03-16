package xyz.ttyz.toubasemvvm.adapter.utils;

import android.content.Context;
import android.view.ViewGroup;

import androidx.databinding.Bindable;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import xyz.ttyz.toubasemvvm.BR;
import xyz.ttyz.toubasemvvm.R;
import xyz.ttyz.toubasemvvm.adapter.utils.viewholder.BaseNormalViewHolder;
import xyz.ttyz.toubasemvvm.adapter.utils.viewholder.EmptyViewHolder;


/**
 * Created by toutou on 2018/7/19.
 * 自动处理空数据适配器
 */

public class BaseEmptyAdapterParent extends BaseRecyclerAdapter{
    protected Context c;
    protected boolean noMore;//尾部
    protected BaseStaggeredDelegate baseStaggeredDelegate;
    boolean showEndTips;
    public BaseEmptyAdapterParent(Context context, NormalAdapterDelegate normalAdapterDelegate) {
        super(normalAdapterDelegate);
        this.c = context;
        this.showEndTips = true;
    }
    public BaseEmptyAdapterParent(Context context, boolean showEndTips, NormalAdapterDelegate normalAdapterDelegate) {
        super(normalAdapterDelegate);
        this.c = context;
        this.showEndTips = showEndTips;
    }

    @Override
    public int getItemViewType(int position) {
        if (list == null || list.size() == 0) {
            return -100;
        } else if (noMore && isFooterItem(position)) {
            return -200;
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public int getItemCount() {
        if (list == null || list.size() == 0) {
            return 1;
        } else if (noMore && showEndTips) {
            return list.size() + 1;
        } else
            return super.getItemCount();
    }

    protected String initEmptyInfo(){return "暂无数据";}

    protected BaseNormalViewHolder initEmptyViewHolder(ViewGroup parent){return null;}

    protected BaseNormalViewHolder initFooterViewHolder(ViewGroup parent){return null;}

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == -100) {
            if(null == initEmptyViewHolder(parent)){
                return new EmptyViewHolder(c, parent);
            } else {
                return initEmptyViewHolder(parent);
            }
        } else if (viewType == -200 ) {
            if(null == initFooterViewHolder(parent)){
                return new BaseNormalViewHolder(c, R.layout.view_nomore_foot, parent) {

                    @Override
                    protected void initVariable(ViewDataBinding mBinding) {

                    }

                    @Override
                    public void bindData(Object data) {

                    }
                };
            } else {
                return initFooterViewHolder(parent);
            }
        } else
            return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (baseStaggeredDelegate != null) {
            baseStaggeredDelegate.handleLayoutIfStaggeredGridLayout(holder, position);
        }
        switch (getItemViewType(position)){
            case -100:
                if(holder instanceof EmptyViewHolder){
                    ((EmptyViewHolder)holder).bindData(initEmptyInfo());
                }
                break;
            case -200:
                break;
            default:
                super.onBindViewHolder(holder, position);
        }
    }

    public void setNoMore(boolean noMore) {
        this.noMore = noMore;
        notifyChange(BR.noMore);
    }

    @Bindable
    public boolean isNoMore() {
        return noMore;
    }

    public interface BaseStaggeredDelegate {
        void handleLayoutIfStaggeredGridLayout(RecyclerView.ViewHolder holder, int position);
    }



}

