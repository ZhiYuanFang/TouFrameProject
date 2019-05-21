package xyz.ttyz.tourfrxohc;

import android.content.Context;
import android.widget.Toast;

import xyz.ttyz.mylibrary.method.RfRxOHCBaseModule;
import xyz.ttyz.mylibrary.method.RfRxSubscriber;

/**
 * Created by tou on 2019/5/21.
 */

public abstract class BaseSubscriber<D> extends RfRxSubscriber<BaseModule<D>> {
    @Override
    public Context initContext() {
        return ActivityManager.getInstance();
    }

    @Override
    public void onStart() {
        ProgressUtil.showCircleProgress(ActivityManager.getInstance());
    }

    @Override
    public void onCompleted() {
        ProgressUtil.missCircleProgress();
    }

    @Override
    public void onError(Throwable e) {
        e.getMessage();
        onCompleted();
    }

    @Override
    public void onRfRxNext(BaseModule baseModule) {
        if(baseModule.getErr() == 0){
            success((D) baseModule.getData());
        } else {
            Toast.makeText(ActivityManager.getInstance(), baseModule.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public abstract void success(D data);
}
