package xyz.ttyz.mylibrary.method;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.trello.rxlifecycle2.LifecycleProvider;

import io.reactivex.Observable;
import xyz.ttyz.mylibrary.RfRxOHCUtil;
import xyz.ttyz.mylibrary.method.BaseObserver;
import xyz.ttyz.mylibrary.method.ProgressUtil;

/**
 * Created by tou on 2019/5/21.
 */

public abstract class BaseTouSubscriber<D> extends BaseObserver<BaseModule<D>> {
    private static final String TAG = "BaseTouSubscriber";
    Observable apiObservable;//与它绑定的观察者
    private boolean notShowProgress;

    public BaseTouSubscriber(LifecycleProvider lifeCycle) {
        super(lifeCycle);
    }

    @Override
    public Context initContext() {
        return ActivityManager.getInstance();
    }

    public void onStart() {
        if (!notShowProgress)
            ProgressUtil.showCircleProgress(ActivityManager.getInstance());
    }

    //如果当前接口对应页面已经关闭，会直接走onComplete，不会执行接口请求
    @Override
    public void onComplete() {
        ProgressUtil.missCircleProgress();
        HttpDefaultUtils.popSubscriber(this);
        HttpDefaultUtils.isRequestIng = false;//只要完成了，就行了

        //执行剩余接口
        if (HttpDefaultUtils.getWaitUiSubscriber().size() > 0) {
            BaseTouSubscriber baseSubscriber = HttpDefaultUtils.getWaitUiSubscriber().get(0);
            new RxOHCUtils<>(initContext()).executeApi(baseSubscriber.getApiObservable(), baseSubscriber);
        }
    }

    /**
     * 网络请求失败
     */
    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        Log.i(TAG, "onError: " + e.getMessage());
        Toast.makeText(ActivityManager.getInstance(), e.getMessage(), Toast.LENGTH_LONG).show();
        onComplete();
    }

    @Override
    public void onRfRxNext(BaseModule<D> baseModule) {
        onStart();
        if (baseModule.getCode() == RfRxOHCUtil.successCode) {
            success((D) baseModule.getData());
        } else {
            fail(baseModule);
        }
    }

    /**
     * 网络请求成功，返回正常
     * @param data data里的内容
     */
    public abstract void success(D data);

    /**
     * 網絡請求成功，但是不是正常返回
     * @param baseModule int code 自定义
     */
    protected void fail(BaseModule<D> baseModule){
        Toast.makeText(ActivityManager.getInstance(), baseModule.getMsg(), Toast.LENGTH_LONG).show();
    }

    //region public
    public BaseTouSubscriber<D> notShowProgress() {
        this.notShowProgress = true;
        return this;
    }

    public Observable getApiObservable() {
        return apiObservable;
    }

    public void setApiObservable(Observable apiObservable) {
        this.apiObservable = apiObservable;
    }
    //endregion
}
