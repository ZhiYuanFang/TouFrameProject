package xyz.ttyz.tourfrxohc.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import xyz.ttyz.toubasemvvm.adapter.utils.BaseEmptyAdapterParent;
import xyz.ttyz.toubasemvvm.adapter.utils.BaseRecyclerAdapter;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.LayoutNumberPopViewBinding;
import xyz.ttyz.tourfrxohc.viewholder.PopNumberItemViewHolder;

@SuppressLint("ViewConstructor")
public class NumberSelectView extends RelativeLayout {
    PopDelegate delegate;
    BaseEmptyAdapterParent adapterParent;
    public NumberSelectView(Context context, PopDelegate delegate) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_number_pop_view, this, false);
        addView(view);
        LayoutNumberPopViewBinding mBinding = DataBindingUtil.bind(view);
        if(mBinding == null) return;
        mBinding.setContext(this);
        this.delegate = delegate;

        adapterParent = new BaseEmptyAdapterParent(context, new BaseRecyclerAdapter.NormalAdapterDelegate() {
            @Override
            public int getItemViewType(int position) {
                return 0;
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new PopNumberItemViewHolder(context, parent, new PopNumberItemViewHolder.ItemClsDelegate() {
                    @Override
                    public void selectItem(int number) {
                        if(delegate != null){
                            delegate.selectItem(number);
                        }
                    }
                });
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((PopNumberItemViewHolder)holder).bindData((int) adapterParent.getItem(position), position);
            }
        });
        mBinding.setAdapter(adapterParent);

        /**
         * 4 - 9 人局
         * */
        for(int i = 4; i < 10; i ++){
            adapterParent.add(i);
        }
    }

    public interface PopDelegate{
        void selectItem(int number);
    }
}
