package xyz.ttyz.tourfrxohc.utils;

import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.toubasemvvm.utils.ToastUtil;
import xyz.ttyz.tourfrxohc.R;

/**
 * @author 投投
 * @date 2023/12/26
 * @email 343315792@qq.com
 */
public class ToastUtils {
    private static Toast toast;
    public static void showSuccess(String txt){
        ActivityManager.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageView iv_icon = initToast().findViewById(R.id.iv_icon);
                TextView tv_msg = initToast().findViewById(R.id.tv_msg);
                tv_msg.setText(txt);
                iv_icon.setImageResource(R.mipmap.success);
                toast.show();
            }
        });

    }
    public static void showError(String txt){
        ActivityManager.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageView iv_icon = initToast().findViewById(R.id.iv_icon);
                TextView tv_msg = initToast().findViewById(R.id.tv_msg);
                tv_msg.setText(txt);
                iv_icon.setImageResource(R.mipmap.error);
                toast.show();
            }
        });
    }

    private static View initToast(){
        if(toast == null){

            toast = new Toast(ActivityManager.getInstance());
            View layout = ActivityManager.getInstance().getLayoutInflater().inflate(R.layout.layout_toast, null);
            toast.setView(layout);
            toast.setGravity(Gravity.CENTER, 0, 0);
        }
        return toast.getView();
    }

}
