package xyz.ttyz.tourfrxohc.fragment;

import android.content.Context;
import android.view.ViewGroup;


import xyz.ttyz.toubasemvvm.adapter.utils.viewholder.BaseNormalViewHolder;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ViewholderSelectStrBinding;
import xyz.ttyz.tourfrxohc.models.SelectItemModel;

public class SelectStrViewHolder extends BaseNormalViewHolder<SelectItemModel, ViewholderSelectStrBinding> {
    private SelectItemDelegate selectItemDelegate;
    public SelectStrViewHolder(Context context, ViewGroup parent, SelectItemDelegate selectItemDelegate) {
        super(context, R.layout.viewholder_select_str, parent);
        this.selectItemDelegate = selectItemDelegate;
    }

    @Override
    protected void initVariable(ViewholderSelectStrBinding mBinding) {
        mBinding.setContext(this);
    }

    @Override
    public void bindData(SelectItemModel data) {
        mBinding.setSelectItemModel(data);

    }

    public void selectItem(SelectItemModel selectItemModel){
        if(selectItemDelegate != null){
            selectItemDelegate.selectItem(selectItemModel);
        }
    }

    public interface SelectItemDelegate{
        void selectItem(SelectItemModel selectItemModel);
    }
}
