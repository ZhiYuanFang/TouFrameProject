package xyz.ttyz.toubasemvvm.adapter;

import android.widget.SeekBar;

import androidx.databinding.BindingAdapter;

public class SeekBarAdapter {
    @BindingAdapter("onSeekBarChangeListener")
    public static void sbar(SeekBar seekBar, SeekBar.OnSeekBarChangeListener onSeekBarChangeListener){
        seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
    }
}
