package xyz.ttyz.toubasemvvm.adapter;

import android.graphics.Rect;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.GridLayoutAnimationController;
import android.view.animation.LayoutAnimationController;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.util.DensityUtil;

import xyz.ttyz.toubasemvvm.R;
import xyz.ttyz.toubasemvvm.adapter.utils.BaseEmptyAdapterParent;

import static android.widget.NumberPicker.OnScrollListener.SCROLL_STATE_IDLE;

public class RecyclerAdapter {
    @BindingAdapter(value = {"adapter", "isNotLinearLayoutManager", "isHorizontal", "layoutManager"}, requireAll = false)
    public static void d(RecyclerView recyclerView, RecyclerView.Adapter adapter, boolean isNotLinearLayoutManager, boolean isHorizontal, RecyclerView.LayoutManager layoutManager) {
        if (!isNotLinearLayoutManager) {
            LinearLayoutManager manager = new LinearLayoutManager(recyclerView.getContext());
            if (isHorizontal) {
                manager.setOrientation(RecyclerView.HORIZONTAL);
            }
            recyclerView.setLayoutManager(manager);
        } else {
            recyclerView.setLayoutManager(layoutManager);
        }
        if (layoutManager instanceof GridLayoutManager) {
            //默认给每一个列表添加动画展示效果
            GridLayoutAnimationController gridLayoutAnimationController = new GridLayoutAnimationController(AnimationUtils.loadAnimation(recyclerView.getContext(), R.anim.item_anim));
            gridLayoutAnimationController.setOrder(LayoutAnimationController.ORDER_REVERSE);
            gridLayoutAnimationController.setDelay(0.15f);
            recyclerView.setLayoutAnimation(gridLayoutAnimationController);
        } else {
            //默认给每一个列表添加动画展示效果
            LayoutAnimationController layoutAnimationController = new LayoutAnimationController(AnimationUtils.loadAnimation(recyclerView.getContext(), R.anim.item_anim));
            layoutAnimationController.setOrder(LayoutAnimationController.ORDER_NORMAL);
            layoutAnimationController.setDelay(0.15f);
            recyclerView.setLayoutAnimation(layoutAnimationController);
        }
        if (adapter != null) {
            recyclerView.setAdapter(adapter);

        }
    }

    @BindingAdapter(value = {"itemDecorations"}, requireAll = false)
    public static void i(RecyclerView recyclerView, RecyclerView.ItemDecoration... itemDecorations) {
        if (itemDecorations == null || itemDecorations.length == 0) return;
        for (RecyclerView.ItemDecoration decoration : itemDecorations) {
            recyclerView.addItemDecoration(decoration);
        }
    }

    @BindingAdapter(value = {"itemDecoration"}, requireAll = false)
    public static void itemDecoration(RecyclerView recyclerView, RecyclerView.ItemDecoration itemDecoration) {
        i(recyclerView, itemDecoration);
    }

    /**
     * @param horizonDis      两者之间的横向距离
     * @param verticalDis     两者之间的纵向距离
     * @param notSpecialOther 是否边缘处放大距离
     * @param noEdge          是否不要边缘处间距
     */
    @BindingAdapter(value = {"horizonDis", "verticalDis", "notSpecialOther", "noEdge"}, requireAll = false)
    public static void i(RecyclerView recyclerView,final int horizonDis, final int verticalDis, final boolean notSpecialOther,final  boolean noEdge) {
        RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                if (parent.getAdapter() != null) {
                    BaseEmptyAdapterParent adapterParent = (BaseEmptyAdapterParent) parent.getAdapter();
                    int pos = parent.getChildAdapterPosition(view);
                    outRect.top = DensityUtil.dp2px(verticalDis / 2);
                    outRect.bottom = DensityUtil.dp2px(verticalDis / 2);
                    outRect.left = DensityUtil.dp2px(horizonDis / 2);
                    outRect.right = DensityUtil.dp2px(horizonDis / 2);
                    if (!notSpecialOther) {
                        if (pos == 0) {
                            outRect.top = DensityUtil.dp2px(verticalDis);
                            outRect.left = DensityUtil.dp2px(horizonDis);
                        } else if (pos == adapterParent.getItemCount() - 1) {
                            outRect.bottom = DensityUtil.dp2px(verticalDis);
                            outRect.right = DensityUtil.dp2px(horizonDis);
                        }
                    }

                    if (noEdge) {
                        if (pos == 0 || pos == adapterParent.getItemCount() - 1) {
                            outRect.top = 0;
                            outRect.left = 0;
                        }
                    }
                }
            }
        };
        i(recyclerView, itemDecoration);
    }

    @BindingAdapter("smoothScrollToPos")
    public static void smoothScrollToPos(RecyclerView mRecyclerView, int position) {
        // 第一个可见位置
        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
        // 最后一个可见位置
        int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));
        if (firstItem < 0 || lastItem < 0) return;
        mRecyclerView.setTag(true);//正在定向滚动
        if (position <= firstItem) {
            // 第一种可能:跳转位置在第一个可见位置之前
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position < lastItem) {
            // 第二种可能:跳转位置在第一个可见位置之后
            int movePosition = position - firstItem;
            if (movePosition < mRecyclerView.getChildCount()) {
                int top = mRecyclerView.getChildAt(movePosition).getTop();
                mRecyclerView.smoothScrollBy(0, top);
            }
        } else {
            addScrollListener(mRecyclerView, smoothScrollListener);
            // 第三种可能:跳转位置在最后可见项之后
            mRecyclerView.smoothScrollToPosition(position);
            mShouldScroll = true;
            toPosition = position;
        }
    }

    private static boolean mShouldScroll;
    private static int toPosition;
    private static RecyclerView.OnScrollListener smoothScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == SCROLL_STATE_IDLE) {
                recyclerView.setTag(false);//停止定向滚动
                if (mShouldScroll) {
                    smoothScrollToPos(recyclerView, toPosition);
                    mShouldScroll = false;
                }
            }

        }
    };

    @BindingAdapter("scrollListener")
    public static void addScrollListener(RecyclerView recyclerView, RecyclerView.OnScrollListener scrollListener) {
        recyclerView.removeOnScrollListener(scrollListener);
        recyclerView.addOnScrollListener(scrollListener);
    }
}
