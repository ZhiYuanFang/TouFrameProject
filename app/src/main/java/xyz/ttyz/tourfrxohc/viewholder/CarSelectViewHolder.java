package xyz.ttyz.tourfrxohc.viewholder;

import android.content.Context;
import android.view.ViewGroup;

import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.adapter.utils.viewholder.BaseNormalViewHolder;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ViewholderCarItemBinding;
import xyz.ttyz.tourfrxohc.models.CarModel;

/**
 * @author 投投
 * @date 2023/12/26
 * @email 343315792@qq.com
 */
public class CarSelectViewHolder extends BaseNormalViewHolder<CarModel, ViewholderCarItemBinding> {
    public boolean showSelect;
    public int doorClose = R.mipmap.close;
    public int doorOpen = R.mipmap.door;
    CarSelectDelegate carSelectDelegate;
    public CarSelectViewHolder(Context context, ViewGroup parent,CarSelectDelegate carSelectDelegate) {
        this(context, parent, true, carSelectDelegate);
    }
    public CarSelectViewHolder(Context context, ViewGroup parent, boolean showSelect, CarSelectDelegate carSelectDelegate) {
        super(context, R.layout.viewholder_car_item, parent);
        this.showSelect = showSelect;
        this.carSelectDelegate = carSelectDelegate;
    }

    @Override
    protected void initVariable(ViewholderCarItemBinding mBinding) {

        mBinding.setContext(this);
    }

    @Override
    public void bindData(CarModel data) {
        mBinding.setCarModel(data);
    }

    public OnClickAdapter.onClickCommand onClickItem = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            if(carSelectDelegate != null){
                carSelectDelegate.selectItem(mBinding.getCarModel());
            }
        }
    };

    public interface CarSelectDelegate{
        void selectItem(CarModel carModel);
    }

}
