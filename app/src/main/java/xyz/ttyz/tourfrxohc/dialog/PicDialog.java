package xyz.ttyz.tourfrxohc.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.luck.picture.lib.photoview.PhotoView;
import com.stx.xhb.androidx.XBanner;
import com.stx.xhb.androidx.entity.SimpleBannerInfo;

import java.util.ArrayList;
import java.util.List;

import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.utils.ImageLoaderUtil;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.DialogPicBinding;

/**
 * @author 投投
 * @date 2023/10/25
 * @email 343315792@qq.com
 */
public class PicDialog extends Dialog {
    private static PicDialog picDialog;
    public static void show(List<String> picList,int showPos){
        picDialog = new PicDialog(ActivityManager.getInstance(), picList, showPos);
        picDialog.show();
    }

    DialogPicBinding mBinding;

    public PicDialog(@NonNull Context context, List<String> picList,int showPos) {
        super(context, R.style.DialogFragmentPicShow);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_pic, null);
        mBinding = DataBindingUtil.bind(view);
        mBinding.setContext(this);
        setContentView(view);
        setCancelable(true);
        setCanceledOnTouchOutside(true);

        mBinding.xbanner.loadImage(new XBanner.XBannerAdapter() {
            @Override
            public void loadBanner(XBanner banner, Object model, View view, int position) {
                SimpleBannerInfo curBannerInfo = (SimpleBannerInfo) model;
                PhotoView photoView = view.findViewById(R.id.photoView);
                photoView.setOnPhotoTapListener((view1, x, y) -> dismiss());
                ImageLoaderUtil.display(photoView, view.findViewById(R.id.progress_bar), curBannerInfo.getXBannerUrl());

            }
        });

        List<SimpleBannerInfo> simpleBannerInfoList = new ArrayList<>();
        for (final String imageUrl : picList) {
            SimpleBannerInfo simpleBannerInfo = new SimpleBannerInfo() {
                @Override
                public Object getXBannerUrl() {
                    return imageUrl;
                }
            };
            simpleBannerInfoList.add(simpleBannerInfo);
        }
        mBinding.xbanner.setBannerData(R.layout.layout_scan_photo_item, simpleBannerInfoList);
        mBinding.xbanner.getViewPager().setCurrentItem(showPos);
    }

    public static void miss(){
        if(picDialog != null){
            picDialog.dismiss();
        }
    }
}
