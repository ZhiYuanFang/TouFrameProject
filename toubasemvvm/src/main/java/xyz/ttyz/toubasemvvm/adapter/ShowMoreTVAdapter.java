package xyz.ttyz.toubasemvvm.adapter;

import androidx.databinding.BindingAdapter;

import xyz.ttyz.toubasemvvm.weight.ShowMoreTextView;


public class ShowMoreTVAdapter {
    @BindingAdapter("showMoreDelegate")
    public static void d(ShowMoreTextView showMoreTextView, ShowMoreTextView.ShowMoreDelegate delegate){
        showMoreTextView.setDelegate(delegate);
    }
}
