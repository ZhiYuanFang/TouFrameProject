package xyz.ttyz.toubasemvvm.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import xyz.ttyz.toubasemvvm.adapter.utils.BaseEmptyAdapterParent;
import xyz.ttyz.toubasemvvm.utils.DensityUtil;

public class BaseGridAdapter extends BaseEmptyAdapterParent {
    public GridLayoutManager gridLayoutManager;
    public RecyclerView.ItemDecoration itemDecoration;

    public BaseGridAdapter(Context context, NormalAdapterDelegate normalAdapterDelegate) {
        this(context, 3, normalAdapterDelegate);
    }

    public BaseGridAdapter(Context context, int spanCount, NormalAdapterDelegate normalAdapterDelegate) {
        super(context, normalAdapterDelegate);
        gridLayoutManager = new WrapGridLayoutManager(context, spanCount, GridLayoutManager.VERTICAL, false);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(getData().isEmpty() || isFooterItem(position) || isFullSize(position)){
                    return gridLayoutManager.getSpanCount();
                } else return 1;
            }
        });
        itemDecoration = initItemDecoration();
    }

    protected boolean isFullSize(int position){
        return false;
    }

    public RecyclerView.ItemDecoration initItemDecoration() {
        return new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int dis = initDis();
                outRect.top = DensityUtil.dp2px(dis);
                outRect.bottom = DensityUtil.dp2px(dis);
                outRect.left = DensityUtil.dp2px(dis);
                outRect.right = DensityUtil.dp2px(dis);
            }
        };
    }

    protected int initDis(){
        return 1;
    }

}
