package xyz.ttyz.toubasemvvm.adapter.utils.viewholder;

import android.content.Context;
import android.view.ViewGroup;

import xyz.ttyz.toubasemvvm.R;
import xyz.ttyz.toubasemvvm.databinding.LayoutEmptyBinding;


public class EmptyViewHolder extends BaseNormalViewHolder<String, LayoutEmptyBinding> {

    public EmptyViewHolder(Context context, ViewGroup parent) {
        super(context, R.layout.layout_empty, parent);
    }

    @Override
    protected void initVariable(LayoutEmptyBinding mBinding) {

    }

    @Override
    public void bindData(String data) {
        mBinding.setInfo(data);
    }
}
