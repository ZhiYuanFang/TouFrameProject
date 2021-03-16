package xyz.ttyz.toubasemvvm.adapter;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.BindingAdapter;

public class ToolAdapter {
    @BindingAdapter("titleStr")
    public static void t(Toolbar toolbar, String titleStr){
        toolbar.setTitle(titleStr);
    }
}
