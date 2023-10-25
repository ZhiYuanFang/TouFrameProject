package xyz.ttyz.tourfrxohc.adapter;

import static xyz.ttyz.toubasemvvm.adapter.OnClickAdapter.CLICK_INTERVAL;

import android.view.View;

import androidx.databinding.BindingAdapter;


import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import xyz.ttyz.tou_example.init.ApplicationUtils;
import xyz.ttyz.toubasemvvm.utils.DialogUtils;

/**
 * @author 投投
 * @date 2023/10/25
 * @email 343315792@qq.com
 */
public class NinePhotoAdapter {
    public interface ClickDelegate {
        void singleClick(int position);

        void doubleClick(int position);
    }

    @BindingAdapter(value = {"nineData"}, requireAll = false)
    public static void nineData(BGANinePhotoLayout ninePhotoLayout, ArrayList<String> nineData) {
        if (nineData != null)
            ninePhotoLayout.setData(nineData);
    }

    private static long lastTime;
    @BindingAdapter(value = {"clickDelegate"}, requireAll = false)
    public static void clickDelegate(BGANinePhotoLayout ninePhotoLayout, ClickDelegate clickDelegate) {
        ninePhotoLayout.setDelegate(new BGANinePhotoLayout.Delegate() {
            @Override
            public void onClickNinePhotoItem(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
                if (clickDelegate != null) {
                    clickDelegate.singleClick(position);
                }
                long curTime = System.currentTimeMillis();
                if(curTime - lastTime < CLICK_INTERVAL){
                    clickDelegate.doubleClick(position);
                }
                lastTime = curTime;
            }

            @Override
            public void onClickExpand(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {

            }
        });
    }
}
