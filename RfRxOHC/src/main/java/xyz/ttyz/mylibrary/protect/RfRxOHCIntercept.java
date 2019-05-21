package xyz.ttyz.mylibrary.protect;

import android.app.Application;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by tou on 2019/5/15.
 *
 - noCache();//不使用缓存，用网络请求
 - noStore();//不使用缓存，也不存储缓存
 - onlyIfCached();//只使用缓存
 - noTransform();//禁止转码
 - maxAge(10, TimeUnit.MILLISECONDS);//设置缓存超时时间为10ms。
 - maxStale(10, TimeUnit.SECONDS);//超时之外的超时时间为10s
 - minFresh(10, TimeUnit.SECONDS);//超时时间为当前时间加上10秒钟。
 - FORCE_NETWORK ： 强制走网络
 - FORCE_CACHE ：强制走缓存

 ------Retrofit 可以设置单个接口缓存配置 -----
 //@Headers("Cache-Control: max-age=640000") 秒级 这里表示1周 时间不从后台获取最新数据
 */

public class RfRxOHCIntercept implements Interceptor {
    Application context;

    public RfRxOHCIntercept(Application context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        //无网络时，使用本地缓存策略
        if(!NetworkUtil.isNetWorkConnected(context)){
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        return chain.proceed(request);
    }
}
