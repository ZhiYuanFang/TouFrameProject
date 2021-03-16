package xyz.ttyz.toubasemvvm.adapter;

import androidx.core.widget.NestedScrollView;
import androidx.databinding.BindingAdapter;

public class NeastScrollAdapter {
    @BindingAdapter("onScrollChangeListener")
    public static void d(NestedScrollView nestedScrollView, NestedScrollView.OnScrollChangeListener onScrollChangeListener){
        nestedScrollView.setOnScrollChangeListener(onScrollChangeListener);
    }
}
