package xyz.ttyz.toubasemvvm.adapter;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import androidx.databinding.BindingAdapter;


import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.util.List;

public class ViewAdapter {
    @BindingAdapter(value = {"maxHeight", "maxWidth", "minHeight", "minWidth"}, requireAll = false)
    public static void viewLimitParam(final View view, int maxHeight, final int maxWidth, final int minHeight, final int minWidth) {
        float dp = view.getContext().getResources().getDisplayMetrics().density;
        final int finalMaxHeight = (int) (dp * maxHeight);
        final int finalMaxWidth = (int) (dp * maxWidth);
        final int finalMinHeight = (int) (dp * minHeight);
        final int finalMinWidth = (int) (dp * minWidth);

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewGroup.LayoutParams params = view.getLayoutParams();
                int height = view.getHeight();
                int width = view.getWidth();
                if (finalMaxHeight > 0 && height > finalMaxHeight) {
                    params.height = finalMaxHeight;
                    view.invalidate();
                }
                if (finalMaxWidth > 0 && width > finalMaxWidth) {
                    params.width = finalMaxWidth;
                    view.invalidate();
                }
                if (finalMinHeight > 0 && height < finalMinHeight) {
                    params.height = finalMinHeight;
                    view.invalidate();
                }
                if (finalMinWidth > 0 && width < finalMinWidth) {
                    params.width = finalMinWidth;
                    view.invalidate();
                }
            }
        });
    }

    @BindingAdapter("visible")
    public static void v(View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }


    @BindingAdapter("animVisible")
    public static void av(final View view, final boolean visible) {
        if (visible) {
            Animation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                    1.0f, Animation.RELATIVE_TO_SELF, -0.0f);
            mShowAction.setDuration(300);
            mShowAction.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                        v(view, true);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    v(view, visible);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            view.startAnimation(mShowAction);
        } else {
            Animation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                    0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                    1.0f);
            mHiddenAction.setDuration(300);
            mHiddenAction.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
//                    v(view, true);//要隐藏 则当前布局就是显示状态， 不要去改。 涉及到 本身处在隐藏调用此方法会出现体验不适
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    v(view, visible);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            view.startAnimation(mHiddenAction);
        }
    }

    @BindingAdapter("visibleForList")
    public static void f(View view, List list) {
        view.setVisibility(list != null && !list.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("isSelect")
    public static void qw(View view, boolean isSelect) {
        view.setSelected(isSelect);
    }

    @BindingAdapter("touchAble")
    public static void t(View view, final boolean touchaAble) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return !touchaAble;
            }
        });
    }

    @BindingAdapter("layoutHeight")
    public static void h(View view, int height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = height;
        view.setLayoutParams(params);
        view.invalidate();
    }

    @BindingAdapter("layoutWidth")
    public static void w(View view, int width) {
        if(width > 0){
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.width = width;
            view.setLayoutParams(params);
            view.invalidate();
        }
    }

    @BindingAdapter("touchListener")
    public static void t(View view, View.OnTouchListener touchListener) {
        view.setOnTouchListener(touchListener);
    }

    @BindingAdapter("marginStart")
    public static void s(View view, float marginStart) {
        if (!(view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) return;
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.setMarginStart(DensityUtil.dp2px(marginStart));
        view.setLayoutParams(layoutParams);
    }
    @BindingAdapter("marginEnd")
    public static void e(View view, float marginEnd) {
        if (!(view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) return;
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.setMarginEnd(DensityUtil.dp2px(marginEnd));
        view.setLayoutParams(layoutParams);
    }
    @BindingAdapter("marginBottom")
    public static void b(View view, float marginBottom) {
        if (!(view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) return;
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin, layoutParams.rightMargin, DensityUtil.dp2px(marginBottom));
        view.setLayoutParams(layoutParams);
    }

    @BindingAdapter("marginTop")
    public static void w(View view, float marginTop) {
        if (!(view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) return;
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.setMargins(layoutParams.leftMargin, DensityUtil.dp2px(marginTop), layoutParams.rightMargin, layoutParams.bottomMargin);
        view.setLayoutParams(layoutParams);
    }

    @BindingAdapter("layoutMargin")
    public static void m(View view, int layoutMargin) {
        layoutMargin = DensityUtil.dp2px(layoutMargin);
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            layoutParams.setMargins(layoutMargin, layoutMargin, layoutMargin, layoutMargin);
        }
    }

    @BindingAdapter("layoutPadding")
    public static void p(View view, int layoutPadding) {
        layoutPadding = DensityUtil.dp2px(layoutPadding);
        view.setPadding(layoutPadding, layoutPadding, layoutPadding, layoutPadding);
    }

    @BindingAdapter("bacColor")
    public static void bc(View view, int bacColor) {
        view.setBackgroundColor(bacColor);
    }


}

