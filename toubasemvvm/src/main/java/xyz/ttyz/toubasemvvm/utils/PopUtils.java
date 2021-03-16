package xyz.ttyz.toubasemvvm.utils;

import android.annotation.SuppressLint;
import android.view.View;

import xyz.ttyz.toubasemvvm.weight.CustomPopWindow;


public class PopUtils {
    @SuppressLint("StaticFieldLeak")
    private static CustomPopWindow customPopWindow;

    public static void show(View donwView, View popView){
        show(donwView, 0, 0, popView);
    }
    public static void show(View donwView, float downX, float downY, View popView){
       show(donwView, popView, new ShowDelegate() {
           @Override
           public void show(CustomPopWindow customPopWindow, View view) {
               customPopWindow.showAsDropDown(view, view.getWidth()/2, - view.getHeight()/2);
           }
       });
    }

    public static void show(View donwView, View popView, ShowDelegate showDelegate){
        customPopWindow = new CustomPopWindow.PopupWindowBuilder(donwView.getContext())
                .enableBackgroundDark(true) //弹出popWindow时，背景是否变暗
                .setBgDarkAlpha(0.7f) // 控制亮度
                .setView(popView)
                .create();

        popView.measure(ViewUtils.makeDropDownMeasureSpec(customPopWindow.getWidth()), ViewUtils.makeDropDownMeasureSpec(customPopWindow.getHeight()));

        if(showDelegate != null){
            showDelegate.show(customPopWindow, donwView);
        }
    }

    public interface ShowDelegate{
        void show(CustomPopWindow customPopWindow, View donwView);
    }

    public static void disMiss(){
        if(customPopWindow != null){
            customPopWindow.dissmiss();
        }
    }
}
