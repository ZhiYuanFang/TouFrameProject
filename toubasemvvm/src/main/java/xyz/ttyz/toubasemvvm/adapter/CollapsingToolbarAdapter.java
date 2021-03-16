package xyz.ttyz.toubasemvvm.adapter;

import androidx.databinding.BindingAdapter;

import com.google.android.material.appbar.CollapsingToolbarLayout;

public class CollapsingToolbarAdapter {
    @BindingAdapter(value = {"collapTitleColor", "expandTitleColor"}, requireAll = true)
    public static void coa(CollapsingToolbarLayout collapsingToolbarLayout,int collapTitleColor,int expandTitleColor){
        collapsingToolbarLayout.setExpandedTitleColor(expandTitleColor);
        collapsingToolbarLayout.setCollapsedTitleTextColor(collapTitleColor);
    }

    @BindingAdapter(value = {"showTitle"})
    public static void t(CollapsingToolbarLayout collapsingToolbarLayout,boolean showTitle){
        collapsingToolbarLayout.setTitleEnabled(showTitle);
    }

}
