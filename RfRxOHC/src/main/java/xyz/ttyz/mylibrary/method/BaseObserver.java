package xyz.ttyz.mylibrary.method;


import android.content.Context;

import com.trello.rxlifecycle2.LifecycleProvider;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import xyz.ttyz.mylibrary.protect.SharedPreferenceUtil;
import xyz.ttyz.mylibrary.protect.StringUtil;

public abstract class BaseObserver<T extends RfRxOHCBaseModule> implements Observer<T> {
    boolean localObserver;
    LifecycleProvider lifeCycle;

    public BaseObserver(LifecycleProvider lifeCycle) {
        this.lifeCycle = lifeCycle;
    }

    public abstract Context initContext();

    public abstract String initCacheKey();

    public void setLocalObserver(boolean localObserver) {
        this.localObserver = localObserver;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {
        System.out.println("---------- onNext");
        //本地数据展示 不做缓存
        if(!localObserver && !StringUtil.safeString(initCacheKey()).isEmpty()){
            //缓存请求的数据，便于网络异步请求刷新数据
            SharedPreferenceUtil.setCacheShareString(initContext(), initCacheKey(), StringUtil.object2String(t));
        }
        onRfRxNext(t);
    }

    public abstract void onRfRxNext(T t);

}
