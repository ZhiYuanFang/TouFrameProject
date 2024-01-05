package xyz.ttyz.tourfrxohc.viewholder;

import android.content.Context;
import android.view.ViewGroup;

import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.adapter.utils.viewholder.BaseNormalViewHolder;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ViewholderErrorItemBinding;
import xyz.ttyz.tourfrxohc.models.CarModel;
import xyz.ttyz.tourfrxohc.utils.LockUtil;

/**
 * @author 投投
 * @date 2023/12/26
 * @email 343315792@qq.com
 * 故障柜管理item
 */
public class ErrorItemViewHolder extends BaseNormalViewHolder<CarModel, ViewholderErrorItemBinding> {
    ErrorItemDelegate delegate;
    public ErrorItemViewHolder(Context context, ViewGroup parent, ErrorItemDelegate delegate) {
        super(context, R.layout.viewholder_error_item, parent);
        this.delegate = delegate;

    }

    @Override
    protected void initVariable(ViewholderErrorItemBinding mBinding) {
        mBinding.setContext(this);
    }

    @Override
    public void bindData(CarModel data) {
        mBinding.setCarModel(data);
    }

    public OnClickAdapter.onClickCommand onClickVerify = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            LockUtil.getInstance(new LockUtil.LockDelegate() {
                @Override
                public void callBackOpen(int keyNumber) {
                    mBinding.getCarModel().setOpen(true);
                }

                @Override
                public void callBackState(boolean[] readArr) {
                    mBinding.getCarModel().setOpen(readArr[mBinding.getCarModel().getDoorNumber() - 1]);
                }
            }).openKey(mBinding.getCarModel().getDoorNumber());
        }
    };

    public OnClickAdapter.onClickCommand onClickNormal = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            if(delegate != null){
                delegate.normalItem(mBinding.getCarModel());
            }
        }
    };



    public interface ErrorItemDelegate{
        void normalItem(CarModel carModel);
    }

}
