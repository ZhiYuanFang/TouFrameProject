package xyz.ttyz.toubasemvvm.adapter;

import androidx.databinding.BindingAdapter;

import xyz.ttyz.toubasemvvm.weight.TouchFrameLayout;


public class TouchAdapter {
    @BindingAdapter("canInterceptTouch")
    public static void d(TouchFrameLayout touchFrameLayout, boolean canInterceptTouch){
        touchFrameLayout.setCanInterceptTouch(canInterceptTouch);
    }
}
