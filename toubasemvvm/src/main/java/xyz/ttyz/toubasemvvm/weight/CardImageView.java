package xyz.ttyz.toubasemvvm.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableFloat;


import java.io.File;

import xyz.ttyz.toubasemvvm.R;
import xyz.ttyz.toubasemvvm.databinding.ViewCardImageBinding;

public class CardImageView extends RelativeLayout {

    public ObservableBoolean loadProgressBar = new ObservableBoolean(false);
    public ObservableBoolean isCircle = new ObservableBoolean(false);
    public ObservableFloat imageRadius = new ObservableFloat(0f);
    public ObservableField<String> imageUrl = new ObservableField<>("");
    public ObservableField<String> imageFilePath = new ObservableField<>("");
    public ObservableField<File> imageFile  = new ObservableField<>();
    public ObservableBoolean isBlur = new ObservableBoolean(false);
    public ObservableFloat thumbnil = new ObservableFloat(0f);


    public CardImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public CardImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_card_image, this, false);
        ViewCardImageBinding mBinding = DataBindingUtil.bind(view);
        if (mBinding != null) mBinding.setContext(CardImageView.this);
        addView(view);
    }
}
