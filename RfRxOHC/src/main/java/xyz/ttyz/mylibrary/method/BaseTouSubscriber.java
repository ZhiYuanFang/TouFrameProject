package xyz.ttyz.mylibrary.method;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.trello.rxlifecycle2.LifecycleProvider;

import xyz.ttyz.mylibrary.RfRxOHCUtil;
import xyz.ttyz.mylibrary.method.BaseObserver;
import xyz.ttyz.mylibrary.method.ProgressUtil;

/**
 * Created by tou on 2019/5/21.
 */

public abstract class BaseTouSubscriber<D> extends BaseObserver<BaseModule<D>> {
    private static final String TAG = "BaseTouSubscriber";

    public BaseTouSubscriber(LifecycleProvider lifeCycle) {
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
        Log.i(TAG, "onError: " + e.getMessage());
        Toast.makeText(ActivityManager.getInstance(), e.getMessage(), Toast.LENGTH_LONG).show();
        onComplete();
    }

    @Override
    public void onRfRxNext(BaseModule baseModule) {
        onStart();
        if(baseModule.getCode() == RfRxOHCUtil.successCode){
            success((D) baseModule.getData());
        } else {
            Toast.makeText(ActivityManager.getInstance(), baseModule.getMsg(), Toast.LENGTH_LONG).show();
        }
    }

    public abstract void success(D data);
}
