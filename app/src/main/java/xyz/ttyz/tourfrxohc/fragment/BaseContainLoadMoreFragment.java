package xyz.ttyz.tourfrxohc.fragment;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableInt;
import androidx.databinding.ViewDataBinding;


import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;

import java.util.Map;

import io.reactivex.Observable;
import xyz.ttyz.mylibrary.method.BaseModule;
import xyz.ttyz.mylibrary.method.RecordsModule;
import xyz.ttyz.mylibrary.method.RfRxOHCBaseModule;
import xyz.ttyz.mylibrary.method.RxOHCUtils;
import xyz.ttyz.toubasemvvm.adapter.utils.BaseEmptyAdapterParent;
import xyz.ttyz.toubasemvvm.ui.BaseTouFragment;
import xyz.ttyz.toubasemvvm.utils.ListUtil;
import xyz.ttyz.tourfrxohc.http.BaseSubscriber;

public abstract class BaseContainLoadMoreFragment<T extends ViewDataBinding,D, B extends RecordsModule<D>> extends BaseTouFragment<T> {
    public final int START_PAGE = 1;
    public ObservableInt pageIndex = new ObservableInt(START_PAGE);
    public ObservableInt pageCount = new ObservableInt(10);

    @Override
    protected void initData() {
    }

    @Override
    protected void initServer() {
        loadPageInfo(true);
    }

    protected void clearData(){
        if (initLoadPageInfoAdapter() != null) {
            initLoadPageInfoAdapter().clear();
        }
    }

    protected void loadPageInfo(boolean refresh) {
        if (!refresh) {
            int p = pageIndex.get();
            pageIndex.set(++p);
        } else {
            pageIndex.set(START_PAGE);
        }
        Map<String, Object> map = initLoadMoreParam();
        if (map != null) {
            map.put("size", pageCount.get());
            map.put("current", pageIndex.get());
        }
        requestUrl(refresh, map);
    }

    protected abstract @Nullable Observable<B> initApiService(Map map);

    protected void requestUrl(boolean refresh, Map map){
        new RxOHCUtils<>(getContext()).executeApi(initApiService(map), new BaseSubscriber<B>(this, loadEnd) {
            @Override
            public void onRfRxNext(BaseModule<B> baseModule) {
                super.onRfRxNext(baseModule);
                if (baseModule.getData() != null) {
                    initLoadPageInfoAdapter().setNoMore(ListUtil.isFinish(pageIndex.get(), pageCount.get(), baseModule.getData().getTotal()));
                } else {
                    initLoadPageInfoAdapter().setNoMore(true);
                }
            }

            @Override
            public void success(B data) {
                if (refresh) {
                    clearData();
                }
                dealLoadMoreSuccess(data.getRecords());
            }
        });

    }

    protected abstract Map<String, Object> initLoadMoreParam();

    protected abstract BaseEmptyAdapterParent initLoadPageInfoAdapter();

    protected abstract void dealLoadMoreSuccess(D data);

    public OnLoadMoreListener loadMoreListener = new OnLoadMoreListener() {
        @Override
        public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
            loadEnd.set(false);
            loadPageInfo(false);
        }
    };

    @Override
    protected void lazyLoad() {
        if (!isInViewPager() || (isUIVisible && isViewCreated)) {
            isUIVisible = false;
            new Handler().postDelayed(new Runnable() {//页面里切换混乱，会先触发initData 再触发onCreateView  或许这就是删除setUserVisibleHint的原因 误差不超过50， 安全起见 300稳稳的
                @Override
                public void run() {
                    if (getContext() != null){
                        initData();
                        initServer();
                    }
                }
            }, 0);//不是页面内切换， 这里对速度要求极高，所以不做延迟
        }
    }
}
