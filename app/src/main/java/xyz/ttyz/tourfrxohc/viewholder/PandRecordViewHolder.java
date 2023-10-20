package xyz.ttyz.tourfrxohc.viewholder;

import android.content.Context;
import android.view.ViewGroup;

import xyz.ttyz.toubasemvvm.adapter.utils.viewholder.BaseNormalViewHolder;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ViewholderPandRecordBinding;
import xyz.ttyz.tourfrxohc.models.PantRecordModel;

/**
 * @author 投投
 * @date 2023/10/20
 * @email 343315792@qq.com
 */
public class PandRecordViewHolder extends BaseNormalViewHolder<PantRecordModel, ViewholderPandRecordBinding> {
    public PandRecordViewHolder(Context context, ViewGroup parent) {
        super(context, R.layout.viewholder_pand_record, parent);
    }

    @Override
    protected void initVariable(ViewholderPandRecordBinding mBinding) {

    }

    @Override
    public void bindData(PantRecordModel data) {
        mBinding.setPantRecordModule(data);
    }
}
