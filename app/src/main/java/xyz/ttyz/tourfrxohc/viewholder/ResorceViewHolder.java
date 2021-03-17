package xyz.ttyz.tourfrxohc.viewholder;

import android.content.Context;
import android.view.ViewGroup;

import xyz.ttyz.toubasemvvm.adapter.utils.viewholder.BaseNormalViewHolder;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ViewholderResorceBinding;
import xyz.ttyz.tourfrxohc.models.ResorceModel;

public class ResorceViewHolder extends BaseNormalViewHolder<ResorceModel, ViewholderResorceBinding> {

    public ResorceViewHolder(Context context, ViewGroup parent) {
        super(context, R.layout.viewholder_resorce, parent);
    }

    @Override
    protected void initVariable(ViewholderResorceBinding mBinding) {

    }

    @Override
    public void bindData(ResorceModel data) {
        mBinding.setResorceModule(data);
    }
}
