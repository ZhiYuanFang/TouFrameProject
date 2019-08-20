package xyz.ttyz.tourfrxohc;

import android.content.Context;
import android.widget.Toast;

import com.trello.rxlifecycle2.LifecycleProvider;

import xyz.ttyz.mylibrary.method.BaseObserver;

/**
 * Created by tou on 2019/5/21.
 */

public abstract class BaseSubscriber<D> extends BaseObserver<BaseModule<D>> {

    public BaseSubscriber(LifecycleProvider lifeCycle) {
        super(lifeCycle);
    }

    @Override
    public Context initContext() {
        return ActivityManager.getInstance();
    }

    public void onStart() {
        ProgressUtil.showCircleProgress(ActivityManager.getInstance());
    }

    @Override
    public void onComplete() {
        ProgressUtil.missCircleProgress();
    }

    @Override
    public void onError(Throwable e) {
        e.getMessage();
        onComplete();
    }

    @Override
    public void onRfRxNext(BaseModule baseModule) {
        onStart();
        if(baseModule.getErr() == 0){
            success((D) baseModule.getData());
        } else {
            Toast.makeText(ActivityManager.getInstance(), baseModule.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public abstract void success(D data);
}
