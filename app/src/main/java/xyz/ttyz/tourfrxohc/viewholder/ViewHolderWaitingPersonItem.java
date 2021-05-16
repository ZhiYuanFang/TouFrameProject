package xyz.ttyz.tourfrxohc.viewholder;

import android.content.Context;
import android.view.ViewGroup;

import xyz.ttyz.toubasemvvm.adapter.utils.viewholder.BaseNormalViewHolder;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ViewholderWaitingPersonItemBinding;
import xyz.ttyz.tourfrxohc.models.UserModel;

public class ViewHolderWaitingPersonItem extends BaseNormalViewHolder<UserModel, ViewholderWaitingPersonItemBinding> {
    public ViewHolderWaitingPersonItem(Context context,  ViewGroup parent) {
        super(context, R.layout.viewholder_waiting_person_item, parent);
    }

    @Override
    protected void initVariable(ViewholderWaitingPersonItemBinding mBinding) {
        mBinding.setContext(this);
    }

    @Override
    public void bindData(UserModel data) {
        mBinding.setUser(data);
    }
}
