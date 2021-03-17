package xyz.ttyz.tourfrxohc.http;

import androidx.databinding.ObservableBoolean;

import com.trello.rxlifecycle2.LifecycleProvider;

import xyz.ttyz.mylibrary.method.BaseTouSubscriber;

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
    public void onComplete() {
        super.onComplete();
        if(loadEnd != null)
            loadEnd.set(true);
    }
}
