package xyz.ttyz.tourfrxohc.viewholder;

import android.content.Context;
import android.view.ViewGroup;

import com.seuic.uhf.EPC;

import xyz.ttyz.toubasemvvm.adapter.utils.viewholder.BaseNormalViewHolder;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ViewholderPandingItemBinding;
import xyz.ttyz.tourfrxohc.databinding.ViewholderResorceBinding;
import xyz.ttyz.tourfrxohc.models.ResorceModel;

/**
 * @author 投投
 * @date 2023/10/18
 * @email 343315792@qq.com
 */
public class PandingViewHolder extends BaseNormalViewHolder<EPC, ViewholderPandingItemBinding> {

    public PandingViewHolder(Context context, ViewGroup parent) {
        super(context, R.layout.viewholder_panding_item, parent);
    }

    @Override
    protected void initVariable(ViewholderPandingItemBinding mBinding) {

    }

    @Override
    public void bindData(EPC data) {
        mBinding.setEpc(data);
    }
}
