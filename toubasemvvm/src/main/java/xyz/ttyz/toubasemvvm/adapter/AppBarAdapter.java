package xyz.ttyz.toubasemvvm.adapter;

import androidx.databinding.BindingAdapter;

import com.google.android.material.appbar.AppBarLayout;

public class AppBarAdapter {
    @BindingAdapter("offsetChangedListeners")
    public static void appbarChange(AppBarLayout appBarLayout, AppBarLayout.OnOffsetChangedListener... onOffsetChangedListeners){
        if(onOffsetChangedListeners == null || onOffsetChangedListeners.length < 1) return;
        for(AppBarLayout.OnOffsetChangedListener listener : onOffsetChangedListeners){
            appBarLayout.addOnOffsetChangedListener(listener);
        }
    }
}
