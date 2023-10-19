package xyz.ttyz.tourfrxohc.activity;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableInt;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.GridLayoutManager;

import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.Map;

import io.reactivex.Observable;
import xyz.ttyz.mylibrary.method.BaseModule;
import xyz.ttyz.mylibrary.method.BaseTouSubscriber;
import xyz.ttyz.mylibrary.method.RecordsModule;
import xyz.ttyz.mylibrary.method.RxOHCUtils;
import xyz.ttyz.toubasemvvm.adapter.BaseGridAdapter;
import xyz.ttyz.toubasemvvm.adapter.utils.BaseEmptyAdapterParent;
import xyz.ttyz.toubasemvvm.ui.BaseTouActivity;
import xyz.ttyz.toubasemvvm.utils.ListUtil;
import xyz.ttyz.toubasemvvm.utils.ToastUtil;
import xyz.ttyz.tourfrxohc.http.BaseSubscriber;

public abstract class BaseContainLoadMoreActivity<T extends ViewDataBinding,D, B extends RecordsModule<D>> extends BaseTouActivity<T> {
    public final int START_PAGE = 1;
    public ObservableInt pageIndex = new ObservableInt(START_PAGE);
    public ObservableInt pageCount = new ObservableInt(10);

    @Override
    protected void initData() {
        initData1();
    }

    private void initData1() {
        if (initLoadPageInfoAdapter() != null) {
            //网格流尾部提示：暂无数据
            // 缺省页
            if (initLoadPageInfoAdapter() instanceof BaseGridAdapter) {
                ((BaseGridAdapter) initLoadPageInfoAdapter()).gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        if ((initLoadPageInfoAdapter().isFooterItem(position)) || initLoadPageInfoAdapter().getData().isEmpty()) {
                            return ((BaseGridAdapter) initLoadPageInfoAdapter()).gridLayoutManager.getSpanCount();
                        } else return 1;
                    }
                });
            }
        }
        loadPageInfo(true);
    }

    protected abstract Observable<B> initApiService(Map map);
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
            map.put("page", pageIndex.get());
        }
        new RxOHCUtils<>(this).executeApi(initApiService(map), new BaseSubscriber<B>(this) {

            @Override
            public void onRfRxNext(BaseModule<B> baseModule) {
                super.onRfRxNext(baseModule);
                if (baseModule.getData() != null) {
                    initLoadPageInfoAdapter().setNoMore(ListUtil.isFinish(pageIndex.get(), pageCount.get(),baseModule.getData().getTotal()));
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
    protected void clearData(){
        if (initLoadPageInfoAdapter() != null) {
            initLoadPageInfoAdapter().clear();
        }
    }
    protected abstract Map<String, Object> initLoadMoreParam();


    protected abstract BaseEmptyAdapterParent initLoadPageInfoAdapter();

    protected abstract void dealLoadMoreSuccess(D data);

    public OnRefreshListener refreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh(@NonNull RefreshLayout refreshLayout) {
            loadEnd.set(false);
            initData1();
        }
    };
    public OnLoadMoreListener loadMoreListener = new OnLoadMoreListener() {
        @Override
        public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
            loadEnd.set(false);
            loadPageInfo(false);
        }
    };

}
