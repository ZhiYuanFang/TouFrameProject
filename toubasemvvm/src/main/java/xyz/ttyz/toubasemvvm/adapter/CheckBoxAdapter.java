package xyz.ttyz.toubasemvvm.adapter;

import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.databinding.BindingAdapter;

public class CheckBoxAdapter {
    @BindingAdapter("onCheckedChangeListener")
    public static void a(CheckBox checkBox, CompoundButton.OnCheckedChangeListener onCheckedChangeListener){
        checkBox.setOnCheckedChangeListener(onCheckedChangeListener);
    }
}
