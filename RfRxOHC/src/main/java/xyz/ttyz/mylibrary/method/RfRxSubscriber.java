package xyz.ttyz.mylibrary.method;

import android.content.Context;

import rx.Subscriber;
import xyz.ttyz.mylibrary.method.RfRxOHCBaseModule;
import xyz.ttyz.mylibrary.protect.SharedPreferenceUtil;
import xyz.ttyz.mylibrary.protect.StringUtil;

/**
 * Created by tou on 2019/5/17.
 */

public abstract class RfRxSubscriber<T extends RfRxOHCBaseModule> extends Subscriber<T> {
    public abstract Context initContext();

    public abstract String initCacheKey();

    @Override
    public void onNext(T t) {
        if(!StringUtil.safeString(initCacheKey()).isEmpty()){
            //缓存请求的数据，便于网络异步请求刷新数据
            SharedPreferenceUtil.setCacheShareString(initContext(), initCacheKey(), StringUtil.object2String(t));
        }
        onRfRxNext(t);
    }

    public abstract void onRfRxNext(T t);

}
