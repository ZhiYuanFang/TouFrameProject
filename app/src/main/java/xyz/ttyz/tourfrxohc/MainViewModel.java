package xyz.ttyz.tourfrxohc;

import android.content.Context;

import androidx.databinding.ObservableField;

import com.trello.rxlifecycle2.LifecycleProvider;

import java.util.List;

import io.reactivex.disposables.Disposable;
import xyz.ttyz.mylibrary.method.RxOHCUtils;

public class MainViewModel {
    Context context;
    LifecycleProvider lifecycleProvider;
    public ObservableField<String> name = new ObservableField<>();

    public MainViewModel(Context context, LifecycleProvider lifecycleProvider) {
        this.context = context;
        this.lifecycleProvider = lifecycleProvider;
    }

    public void publish(){
                new RxOHCUtils<>(context).executeApi(BaseApplication.apiService.indexNormalEngineering(), new BaseSubscriber<List<EngineerModule>>(lifecycleProvider) {

                    @Override
                    public void success(List<EngineerModule> data) {
                        name.set("请求结果");
                    }

                    @Override
                    public String initCacheKey() {
                        return "kyhoih";
                    }

                });
    }
}
