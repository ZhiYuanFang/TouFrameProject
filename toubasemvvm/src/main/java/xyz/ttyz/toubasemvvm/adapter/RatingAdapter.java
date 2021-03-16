package xyz.ttyz.toubasemvvm.adapter;

import android.widget.RatingBar;

import androidx.databinding.BindingAdapter;

public class RatingAdapter {
    @BindingAdapter("rattingChangeListener")
    public static void l(RatingBar ratingBar,final RattingChangeListener rattingChangeListener){
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(rattingChangeListener != null){
                    rattingChangeListener.change(rating);
                }
            }
        });
    }

    public interface RattingChangeListener{
        void change(float rating);
    }
}
