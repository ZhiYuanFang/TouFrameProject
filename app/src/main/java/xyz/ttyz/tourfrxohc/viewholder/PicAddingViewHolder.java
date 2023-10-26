package xyz.ttyz.tourfrxohc.viewholder;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.adapter.utils.viewholder.BaseNormalViewHolder;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ViewholderPicAddingBinding;

/**
 * @author 投投
 * @date 2023/10/26
 * @email 343315792@qq.com
 */
public class PicAddingViewHolder extends BaseNormalViewHolder<String, ViewholderPicAddingBinding> {
    ClickDelegate clickDelegate;
    int curPos;
    public PicAddingViewHolder(Context context, @Nullable ClickDelegate clickDelegate, ViewGroup parent) {
        super(context, R.layout.viewholder_pic_adding, parent);
        this.clickDelegate = clickDelegate;
    }

    @Override
    protected void initVariable(ViewholderPicAddingBinding mBinding) {
        mBinding.setContext(this);
    }

    @Override
    @Deprecated
    public void bindData(String data) {

        bindData(data, 0);
    }
    public void bindData(String data, int pos) {

        this.curPos = pos;
        mBinding.setImageUrl(data);
    }

    public OnClickAdapter.onClickCommand clickLook = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            clickDelegate.look(curPos);
        }
    };
    public OnClickAdapter.onClickCommand clickDelete = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            clickDelegate.delete(curPos);
        }
    };

    public interface ClickDelegate {
        void delete(int pos);
        void look(int pos);
    }
}
