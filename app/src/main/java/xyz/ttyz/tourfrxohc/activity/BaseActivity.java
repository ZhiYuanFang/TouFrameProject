package xyz.ttyz.tourfrxohc.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.toubasemvvm.event.NetEvent;
import xyz.ttyz.toubasemvvm.ui.BaseTouActivity;
import xyz.ttyz.tourfrxohc.BaseApplication;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.models.NetEventModel;
import xyz.ttyz.tourfrxohc.utils.LockUtil;
import xyz.ttyz.tourfrxohc.utils.NetUtils;

public abstract class BaseActivity<T extends ViewDataBinding> extends BaseTouActivity<T> {

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void seriChange(NetEventModel seriNetEvent){
        BaseApplication.isSerialConnected = seriNetEvent.netConnected;
        View view = findViewById(R.id.iv_net);
        if(view != null){
            if(seriNetEvent.netConnected){
                ((ImageView)view).setImageResource(R.mipmap.net);
            } else {
                ((ImageView)view).setImageResource(R.mipmap.no_net);
            }
        }

    }



    @Override
    public void missNoNetControl() {
        super.missNoNetControl();
        //有网络
        View view = findViewById(R.id.iv_wifi);
        if(view != null){
            ((ImageView)view).setImageResource(R.mipmap.wifi);
        }
    }

    @Override
    public void showNoNetControl() {
        //无网络
        View view = findViewById(R.id.iv_wifi);
        if(view != null){
            ((ImageView)view).setImageResource(R.mipmap.no_wifi);
        }
    }

    @Override
    protected void doInit() {
        ActivityManager.popActivity(this);
        super.doInit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LockUtil.clearCallBack();
        ActivityManager.exitActivity(this);
    }
}
