package xyz.ttyz.tourfrxohc.http;

import androidx.databinding.ObservableBoolean;

import com.trello.rxlifecycle2.LifecycleProvider;

import xyz.ttyz.mylibrary.method.BaseModule;
import xyz.ttyz.mylibrary.method.BaseTouSubscriber;
import xyz.ttyz.mylibrary.method.HttpDefaultUtils;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;

import static xyz.ttyz.mylibrary.method.HttpDefaultUtils.HTTPCODE;

public abstract class BaseSubscriber<D> extends BaseTouSubscriber<D> {

    ObservableBoolean loadEnd;
    public BaseSubscriber(LifecycleProvider lifeCycle) {
        this(lifeCycle, null);
    }
    public BaseSubscriber(LifecycleProvider lifeCycle, ObservableBoolean loadEnd) {
        super(lifeCycle);
        this.loadEnd = loadEnd;
    }

    @Override
    public String initCacheKey() {
        return null;
    }

    @Override
    public void onComplete() {
        super.onComplete();
        if(loadEnd != null)
            loadEnd.set(true);
    }

    @Override
    protected void fail(BaseModule<D> baseModule) {
        super.fail(baseModule);
        if(baseModule.getCode() == 1506){
            //token失效，去请求接口刷新token
            //刷新完成后
            if(false){

            }
        }
    }


}
