package xyz.ttyz.mylibrary.method;

import android.content.Context;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xyz.ttyz.mylibrary.protect.SharedPreferenceUtil;
import xyz.ttyz.mylibrary.protect.StringUtil;

/**
 * Created by tou on 2019/5/17.
 * 网络请求工具集成
 * 集成本地缓存
 */

public class RxOHCUtils<M extends RfRxOHCBaseModule> {
    private Context c;

    public RxOHCUtils(Context c) {
        this.c = c;
    }

    @SuppressWarnings("unchecked")
    public void executeApi(Observable apiObservable,//touService.login()
                           final RfRxSubscriber uiSubscriber) {
        //本地缓存处理被观察者定义
        Observable cacheObservable = Observable.create(new Observable.OnSubscribe<M>() {
            @Override
            public void call(Subscriber subscriber) {
                //定义订阅关系

                //获取本地缓存
                Object cacheObj = StringUtil.string2Object(SharedPreferenceUtil.getShareString(c, uiSubscriber.initCacheKey()));
                //如果有本地缓存则通知订阅
                if (cacheObj != null && cacheObj instanceof RfRxOHCBaseModule) {
                    subscriber.onNext(cacheObj);
                }
            }
        })
                .subscribeOn(Schedulers.io());//获取本地信息操作在子线程处理， subscribeOn 定义上面运行的在哪个线程

        //绑定缓存被观察者和观察者
        cacheObservable.observeOn(AndroidSchedulers.mainThread())//observeOn 定义接下来运行的在哪个线程
                .subscribe(uiSubscriber);

        //绑定网络被观察者和观察者
        apiObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(uiSubscriber);
    }
}
