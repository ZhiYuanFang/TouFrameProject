package xyz.ttyz.toubasemvvm.adapter;

import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.databinding.BindingAdapter;

public class SwitchAdapter {
    @BindingAdapter("onCheckChangeListener")
    public static void d(Switch _switch, CompoundButton.OnCheckedChangeListener onCheckedChangeListener){
        _switch.setOnCheckedChangeListener(onCheckedChangeListener);
    }
}
