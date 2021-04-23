package xyz.ttyz.tourfrxohc.viewholder;

import android.content.Context;
import android.view.ViewGroup;

import androidx.databinding.ViewDataBinding;

import xyz.ttyz.toubasemvvm.adapter.utils.viewholder.BaseNormalViewHolder;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ViewholderMachingPersonItemBinding;
import xyz.ttyz.tourfrxohc.models.UserModel;

public class MatchItemViewHolder extends BaseNormalViewHolder<UserModel, ViewholderMachingPersonItemBinding> {

    public MatchItemViewHolder(Context context, ViewGroup parent) {
        super(context, R.layout.viewholder_maching_person_item, parent);
    }

    @Override
    protected void initVariable(ViewholderMachingPersonItemBinding mBinding) {

    }

    @Override
    public void bindData(UserModel data) {
        mBinding.setUserModel(data);
    }
}
