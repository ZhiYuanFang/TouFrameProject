package xyz.ttyz.tourfrxohc.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import org.greenrobot.eventbus.EventBus;

import xyz.ttyz.tourfrxohc.R;


/**
 * Created by tou on 2018/12/28.
 */

public abstract class BaseDialogFragment<T extends ViewDataBinding> extends DialogFragment {
    protected Dialog dialog;
    protected View parentView;
    protected abstract int initLayoutID();
    protected T mBinding;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) { 
        if(getActivity() == null){
            return super.onCreateDialog(savedInstanceState);
        }
        if(useEventBus() && !EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        dialog = new Dialog(getActivity(), initDialogStyle()){
            @Override
            public void hide() {
                super.hide();//对话框消失， 触发Fragment.dismiss回调  服务语音输入
                try {
//                    BaseDialogFragment.this.dismiss();
                } catch (Exception e) {
//                    e.printStackTrace();
                }
            }
        };
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        parentView = LayoutInflater.from(getActivity()).inflate(initLayoutID(), null);
        dialog.setContentView(parentView);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消
        initLayoutParams();
        mBinding = DataBindingUtil.bind(parentView);
        initViriable(mBinding);
        initData();
        return dialog;
    }

    protected abstract void initViriable(T mBinding);

    protected abstract void initData();

    protected int initDialogStyle(){
        return R.style.DialogFragment;
    }

    public boolean isDismiss(){
        if(dialog != null){
            return !dialog.isShowing();
        }
        return true;
    }

    // 设置窗体尺寸
    protected void initLayoutParams() {
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            limitHeight(lp);
            lp.gravity = Gravity.BOTTOM;
            window.setAttributes(lp);
        }
    }

    protected void limitHeight(WindowManager.LayoutParams lp){
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
    }

    public void show(FragmentManager manager){


        //这里直接调用show方法会报java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
        FragmentTransaction ft = manager.beginTransaction();
        if(isAdded()){
            ft.hide(this);
            ft.remove(this);
        }
        ft.add(this, "");
        ft.commitAllowingStateLoss();
        manager.executePendingTransactions();
    }

    protected boolean useEventBus(){
        return false;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(useEventBus() && EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }
}
