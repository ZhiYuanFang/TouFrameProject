package xyz.ttyz.toubasemvvm.adapter;

import androidx.databinding.BindingAdapter;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;


public class SmartRefreshAdapter {
    @BindingAdapter("refreshListener")
    public static void r(SmartRefreshLayout refreshLayout, OnRefreshListener refreshListener) {
        refreshLayout.setOnRefreshListener(refreshListener);
    }
    @BindingAdapter("loadMoreListener")
    public static void l(SmartRefreshLayout refreshLayout, OnLoadMoreListener loadMoreListener) {
        refreshLayout.setEnableLoadMore(true);
        refreshLayout.setOnLoadMoreListener(loadMoreListener);
    }
    @BindingAdapter("enableLoadMore")
    public static void m(SmartRefreshLayout refreshLayout, boolean enableLoadMore){
        refreshLayout.setEnableLoadMore(enableLoadMore);
    }
    @BindingAdapter("enableRefresh")
    public static void f(SmartRefreshLayout refreshLayout, boolean enableRefresh){
        refreshLayout.setEnableRefresh(enableRefresh);
    }

    @BindingAdapter("loadEnd")
    public static void e(SmartRefreshLayout refreshLayout, boolean loadEnd){
        if(loadEnd) {
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadMore();
        }
    }
}
