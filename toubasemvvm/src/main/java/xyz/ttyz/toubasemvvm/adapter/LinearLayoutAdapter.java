package xyz.ttyz.toubasemvvm.adapter;

import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import androidx.databinding.BindingAdapter;

public class LinearLayoutAdapter {
    @BindingAdapter("isVertical")
    public static void isv(LinearLayout linearLayout, boolean isVertical){
        linearLayout.setOrientation(isVertical ? LinearLayout.VERTICAL : LinearLayout.HORIZONTAL);
    }
    @BindingAdapter("centerHorizontal")
    public static void s(LinearLayout linearLayout, boolean centerHorizontal){
        linearLayout.setGravity(centerHorizontal ? Gravity.CENTER_HORIZONTAL : Gravity.CENTER_VERTICAL);
    }

    @BindingAdapter("itemWeight")
    public static void w(View view, float itemWeight){
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.weight = itemWeight;
        view.setLayoutParams(params);
    }
}
