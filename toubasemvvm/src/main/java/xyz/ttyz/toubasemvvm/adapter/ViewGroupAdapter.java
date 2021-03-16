package xyz.ttyz.toubasemvvm.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.databinding.BindingAdapter;

import xyz.ttyz.toubasemvvm.R;

public class ViewGroupAdapter {
    @BindingAdapter(value = {"clearForeground","childViews"}, requireAll = false)
    public static void vgv(ViewGroup viewGroup,boolean clearForeground, View... childView){
        viewGroup.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(viewGroup.getContext(), R.anim.loadlayoutanimation));
        if(clearForeground){
            viewGroup.removeAllViews();
        }

        if(childView != null && childView.length > 0){
            for(View view : childView){
                if(viewGroup.indexOfChild(view) < 0){
                    viewGroup.addView(view);
                }
            }
        }
    }
}
