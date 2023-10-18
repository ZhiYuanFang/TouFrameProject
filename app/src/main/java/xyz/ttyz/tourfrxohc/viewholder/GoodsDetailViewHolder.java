package xyz.ttyz.tourfrxohc.viewholder;

import android.content.Context;
import android.view.ViewGroup;

import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.adapter.utils.viewholder.BaseNormalViewHolder;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.activity.GoodsDetailOperatorActivity;
import xyz.ttyz.tourfrxohc.databinding.ViewholderGoodsDetailBinding;
import xyz.ttyz.tourfrxohc.models.GoodsModel;

/**
 * @author 投投
 * @date 2023/10/18
 * @email 343315792@qq.com
 */
public class GoodsDetailViewHolder extends BaseNormalViewHolder<GoodsModel, ViewholderGoodsDetailBinding> {
    public GoodsDetailViewHolder(Context context,ViewGroup parent) {
        super(context, R.layout.viewholder_goods_detail, parent);
    }

    @Override
    protected void initVariable(ViewholderGoodsDetailBinding mBinding) {
        mBinding.setContext(this);
    }

    @Override
    public void bindData(GoodsModel data) {
        mBinding.setGoodsModel(data);
    }

    public OnClickAdapter.onClickCommand clickToDetail = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            //去详情
            GoodsDetailOperatorActivity.show(mBinding.getGoodsModel().getId());
        }
    };
}
