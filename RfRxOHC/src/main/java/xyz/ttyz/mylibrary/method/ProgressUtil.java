package xyz.ttyz.mylibrary.method;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import xyz.ttyz.mylibrary.R;
import xyz.ttyz.mylibrary.protect.StringUtil;

/**
 * Created by tou on 2018/8/1 0001.
 * 进度弹框
 */

public class ProgressUtil {
    static ProgressDialog progressDialog;
    public static void showProgress(final Context c, final String message, final float curProgress, final float maxProgress){
        if(((Activity)c).isFinishing()){
            return;
        }
        ((Activity)c).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null == progressDialog){
                    progressDialog = new ProgressDialog(c);
                }
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setMessage(message);
                progressDialog.setMax((int) maxProgress);
                progressDialog.setProgress((int) curProgress);
                if(!progressDialog.isShowing()){
                    try {
                        progressDialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static void updateProgress(float curProgress){
        updateProgress(null, curProgress);
    }

    public static void missProgress(){
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private static Dialog circleProgressDialog;
    public static void showCircleProgress(Context c){
        showCircleProgress(c, "");
    }
    public static void showCircleProgress(final Context c, final String msg){
        if(null == c){
            return;
        }
        ((Activity)c).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                missCircleProgress();
                circleProgressDialog = new Dialog(c, R.style.style_dialog);
                circleProgressDialog.getWindow().setDimAmount(0f);
                circleProgressDialog.setCancelable(true);
                if(!circleProgressDialog.isShowing()){
                    try {
                        circleProgressDialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                View view = LayoutInflater.from(c).inflate(R.layout.layout_loadding, null);
                TextView tvMsg = view.findViewById(R.id.tvMsg);
                if(StringUtil.safeString(msg).isEmpty()){
                    tvMsg.setVisibility(View.GONE);
                } else {
                    tvMsg.setVisibility(View.VISIBLE);
                    tvMsg.setText(msg);
                }
                circleProgressDialog.setContentView(view);
            }
        });
    }

    public static void missCircleProgress(){
        if(circleProgressDialog != null && circleProgressDialog.isShowing()){
            circleProgressDialog.dismiss();
            circleProgressDialog = null;
        }
    }

    public static void updateProgress(String message, float curProgress){
        if(null != progressDialog){
            progressDialog.setProgress((int) curProgress);
            if(null != message){
                progressDialog.setMessage(message);
            }
        }
    }
}
