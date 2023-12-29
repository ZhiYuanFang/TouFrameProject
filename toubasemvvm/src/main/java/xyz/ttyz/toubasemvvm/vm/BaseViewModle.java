package xyz.ttyz.toubasemvvm.vm;

import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableFloat;
import androidx.databinding.ObservableInt;

import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;


public class BaseViewModle {
    public ObservableInt bacColor = new ObservableInt(ContextCompat.getColor(ActivityManager.getInstance(), android.R.color.transparent));
    public ObservableInt bacDraID = new ObservableInt(-1);
    public ObservableField<Drawable> bacDra = new ObservableField<>();
    public ObservableField<OnClickAdapter.onClickCommand> onClickCommand = new ObservableField<>();
    public ObservableFloat alpha = new ObservableFloat(1);
    public ObservableBoolean visible = new ObservableBoolean(true);
}
