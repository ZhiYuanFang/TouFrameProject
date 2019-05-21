package xyz.ttyz.mylibrary.protect;

import android.content.Context;

import rx.Subscriber;
import xyz.ttyz.mylibrary.method.RfRxOHCBaseModule;
import xyz.ttyz.mylibrary.method.RfRxSubscriber;

/**
 * Created by tou on 2019/5/20.
 */

public class DefaultUiSubscriber extends RfRxSubscriber {
    Context c;

    public DefaultUiSubscriber(Context c) {
        this.c = c;
    }

    @Override
    public Context initContext() {
        return c;
    }

    @Override
    public String initCacheKey() {
        return null;
    }

    @Override
    public void onRfRxNext(RfRxOHCBaseModule rfRxOHCBaseModule) {

    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(Object o) {

    }
}
