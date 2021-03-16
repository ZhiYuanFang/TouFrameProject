package xyz.ttyz.toubasemvvm.adapter.utils.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseNormalViewHolder<T, CB extends ViewDataBinding> extends RecyclerView.ViewHolder {
    protected Context context;
    protected CB mBinding;
    private BaseNormalViewHolder(Context context, View view) {
        super(view);
        this.context = context;
        mBinding = DataBindingUtil.bind(itemView);
        initVariable(mBinding);
    }

    public BaseNormalViewHolder(Context context, @LayoutRes int layoutResID, ViewGroup parent){
        this(context, LayoutInflater.from(context).inflate(layoutResID, parent, false));
    }

    protected abstract void initVariable(CB mBinding);

    public abstract void bindData(T data);

    public interface VHDelegate{};
}
