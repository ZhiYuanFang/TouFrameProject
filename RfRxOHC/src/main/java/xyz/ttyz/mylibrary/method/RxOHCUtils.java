package xyz.ttyz.mylibrary.method;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import xyz.ttyz.mylibrary.protect.NetworkUtil;
import xyz.ttyz.mylibrary.protect.SharedPreferenceUtil;
import xyz.ttyz.mylibrary.protect.StringUtil;

/**
 * Created by tou on 2019/5/17.
 * 网络请求工具集成
 * 集成本地缓存
 */

public class RxOHCUtils<M extends RfRxOHCBaseModule> {
    private static final String TAG = "RxOHCUtils";
    private Context c;

    public RxOHCUtils(Context c) {
        this.c = c;
    }

    @SuppressWarnings("unchecked")
    public void executeApi(Observable apiObservable,//touService.login()
                           final BaseTouSubscriber uiSubscriber) {
        if(ActivityManager.getInstance() == null){
            return;
        }
        uiSubscriber.setApiObservable(apiObservable);//綁定訂閲
        HttpDefaultUtils.pushSubscriber(uiSubscriber);//進入請求隊列
        if (HttpDefaultUtils.isRequestIng) {//保证请求排队进行
            return;
        } else HttpDefaultUtils.isRequestIng = true;
        //绑定网络被观察者和观察者
        if(NetworkUtil.isNetWorkConnected(ActivityManager.getInstance().getApplication())){
            Log.i(TAG, "executeApi: 请求接口");
            if(!uiSubscriber.notShowProgress){
                ProgressUtil.showCircleProgress(ActivityManager.getInstance());
            }
            uiSubscriber.setLocalObserver(false);//网络数据获取，可以做本地数据缓存
            apiObservable
                    .compose(uiSubscriber.lifeCycle == null ? new ObservableTransformer() {
                        @Override
                        public ObservableSource apply(Observable upstream) {
                            return null;
                        }
                    } : uiSubscriber.lifeCycle.bindToLifecycle())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(uiSubscriber);
        } else {
            //本地缓存处理被观察者定义
            Observable cacheObservable = Observable.create(new ObservableOnSubscribe<Object>() {
                @Override
                public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                    //定义订阅关系
                    Log.i(TAG, "subscribe: 获取缓存数据");
                    //获取本地缓存
                    Object cacheObj = StringUtil.string2Object(SharedPreferenceUtil.getShareString(c, uiSubscriber.initCacheKey()));
                    //如果有本地缓存则通知订阅
                    if (cacheObj instanceof BaseModule) {
                        uiSubscriber.setLocalObserver(true);//本地数据获取，不做本地数据缓存
                        Log.i(TAG, "subscribe: 缓存渲染");
                        emitter.onNext(cacheObj);
                        emitter.onComplete();
                    }
                }
            });

            //绑定缓存被观察者和观察者
            cacheObservable.subscribeOn(Schedulers.io())//获取本地信息操作在子线程处理， subscribeOn 定义上面运行的在哪个线程
                    .compose(uiSubscriber.lifeCycle == null ? new ObservableTransformer() {
                        @Override
                        public ObservableSource apply(Observable upstream) {
                            return null;
                        }
                    } : uiSubscriber.lifeCycle.bindToLifecycle())
                    .observeOn(AndroidSchedulers.mainThread())//observeOn 定义接下来运行的在哪个线程
                    .subscribe(uiSubscriber);
        }
    }
}
