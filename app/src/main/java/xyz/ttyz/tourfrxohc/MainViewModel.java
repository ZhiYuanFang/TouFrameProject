package xyz.ttyz.tourfrxohc;

import android.content.Context;
import android.databinding.ObservableField;

import java.util.List;

import xyz.ttyz.mylibrary.method.RxOHCUtils;

public class MainViewModel {
    public ObservableField<String> name = new ObservableField<>();

    public MainViewModel(Context context) {
        new RxOHCUtils<>(context).executeApi(BaseApplication.apiService.indexNormalEngineering(), new BaseSubscriber<List<EngineerModule>>() {
            @Override
            public void success(List<EngineerModule> data) {
                name.set(data.get(0).getName());
            }

            @Override
            public String initCacheKey() {
                return "indexNormalEngineering";
            }

        });
    }
}
