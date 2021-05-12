package xyz.ttyz.tourfrxohc.viewholder;

import android.content.Context;
import android.view.ViewGroup;

import androidx.databinding.ObservableInt;


import xyz.ttyz.toubasemvvm.adapter.utils.viewholder.BaseNormalViewHolder;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.LayoutNumberPopItemBinding;

public class PopNumberItemViewHolder extends BaseNormalViewHolder<Integer, LayoutNumberPopItemBinding> {
    public PopNumberItemViewHolder(Context context, ViewGroup parent, ItemClsDelegate itemClsDelegate) {
        super(context, R.layout.layout_number_pop_item, parent);
        this.delegate = itemClsDelegate;
    }

    public ObservableInt posFiled = new ObservableInt(0);
    @Override
    protected void initVariable(LayoutNumberPopItemBinding mBinding) {
        mBinding.setContext(this);
    }

    @Deprecated
    @Override
    public void bindData(Integer data) {
        bindData(data, 0);
    }

    public void bindData(Integer data, int pos) {
        mBinding.setNumber(data);
        posFiled.set(pos);
    }
    public ItemClsDelegate delegate;
    public interface ItemClsDelegate{
        void selectItem(int number);
    }
}
