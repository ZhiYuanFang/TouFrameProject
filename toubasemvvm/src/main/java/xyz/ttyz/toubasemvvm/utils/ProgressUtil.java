package xyz.ttyz.toubasemvvm.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;

import xyz.ttyz.toubasemvvm.R;


/**
 * Created by tou on 2018/8/1 0001.
 * 进度弹框
 */

public class ProgressUtil {
    static ProgressDialog progressDialog;
    protected static void showProgress(final Context c, final String message, final float curProgress, final float maxProgress){
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
                progressDialog.setCancelable(false);
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

    protected static void updateProgress(float curProgress){
        updateProgress(null, curProgress);
    }

    protected static void missProgress(){
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    protected static void updateProgress(String message, float curProgress){
        if(null != progressDialog){
            progressDialog.setProgress((int) curProgress);
            if(null != message){
                progressDialog.setMessage(message);
            }
        }
    }
}
