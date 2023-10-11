package xyz.ttyz.toubasemvvm.adapter;

import androidx.cardview.widget.CardView;
import androidx.databinding.BindingAdapter;

import java.io.File;

import xyz.ttyz.toubasemvvm.utils.DensityUtil;
import xyz.ttyz.toubasemvvm.weight.CardImageView;

public class CardViewAdapter {
    @BindingAdapter("card_elevation")
    public static void d(CardView cardView, int elv){
        cardView.setCardElevation(DensityUtil.dp2px(elv));
    }
    @BindingAdapter("card_cornerRadius")
    public static void c(CardView cardView, float card_cornerRadius){
        cardView.setRadius(DensityUtil.dp2px(card_cornerRadius));
    }
    @BindingAdapter("card_elevation")
    public static void v(com.bigman.wmzx.customcardview.library.CardView cardView, int elv){
        cardView.setCardElevation(DensityUtil.dp2px(elv));
    }

    @BindingAdapter(value = {"loadProgressBar", "isCircle", "imageRadius", "imageUrl", "imageFilePath", "imageFile", "isBlur", "thumbnil"}, requireAll = false)
    public static void loadImage(CardImageView cardImageView, boolean loadProgressBar, boolean isCircle, float imageRadius, String imageUrl, String imageFilePath, File imageFile, boolean isBlur, float thumbnil) {
        cardImageView.loadProgressBar.set(loadProgressBar);
        cardImageView.isCircle.set(isCircle);
        cardImageView.imageRadius.set(imageRadius);
        cardImageView.imageUrl.set(imageUrl);
        cardImageView.imageFilePath.set(imageFilePath);
        cardImageView.imageFile.set(imageFile);
        cardImageView.isBlur.set(isBlur);
        cardImageView.thumbnil.set(thumbnil);

    }
}
