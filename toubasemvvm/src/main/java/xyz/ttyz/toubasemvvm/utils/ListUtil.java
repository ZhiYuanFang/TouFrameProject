package xyz.ttyz.toubasemvvm.utils;

import android.content.Context;
import android.graphics.Rect;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import xyz.ttyz.toubasemvvm.adapter.utils.BaseEmptyAdapterParent;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;

/**
 * Created by tou on 2018/12/25.
 */

public class ListUtil {
    /**
     * 初始化listView的高度
     */
    public static void fixListViewHeight(ListView listView) {
        // 如果没有设置数据适配器，则ListView没有子项，返回。
        Adapter listAdapter = listView.getAdapter();
        int totalHeight = 0;
        if (listAdapter == null) {
            return;
        }
        for (int index = 0, len = listAdapter.getCount(); index < len; index++) {
            View listViewItem = listAdapter.getView(index, null, listView);
            // 计算子项View 的宽高
            if (listViewItem != null) {
                listViewItem.measure(0, 0);
                // 计算所有子项的高度和
                totalHeight += listViewItem.getMeasuredHeight();
            }

        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        // listView.getDividerHeight()获取子项间分隔符的高度
        // params.height设置ListView完全显示需要的高度
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static void fixListViewHeight(RecyclerView listView) {
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        // listView.getDividerHeight()获取子项间分隔符的高度
        // params.height设置ListView完全显示需要的高度
        params.height = recyclerViewHeight(listView);
        listView.setLayoutParams(params);
    }

    public static void fixListViewHeight(RecyclerView listView, int height) {
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        // listView.getDividerHeight()获取子项间分隔符的高度
        // params.height设置ListView完全显示需要的高度
        params.height = height;
        listView.setLayoutParams(params);
    }

    public static int recyclerViewHeight(RecyclerView listView) {
        // 如果没有设置数据适配器，则ListView没有子项，返回。
        RecyclerView.Adapter listAdapter = listView.getAdapter();
        int totalHeight = 0;
        if (listAdapter == null) {
            return 0;
        }
        if(listAdapter.getItemCount() > 0){
            View listViewItem = listView.getChildAt(0);
            if (listViewItem != null) {
                if (listViewItem.getMeasuredHeight() == 0)
                    listViewItem.measure(0, 0);
                // 计算所有子项的高度和
                totalHeight = listViewItem.getMeasuredHeight() * listAdapter.getItemCount();
            }
        }
        return totalHeight;
    }

    /**
     * list 排重
     */
    public static List removeDoubleFromList(List list) {
        List newList = new ArrayList();
        if (null != list) {
            for (Object obj : list) {
                if (!newList.contains(obj)) {
                    newList.add(obj);
                }
            }
        }
        return newList;
    }


    public static void enableRefresh(SmartRefreshLayout smartRefreshLayout, BaseEmptyAdapterParent baseEmptyAdapterParent, int pageIndex, int limit, int total) {
        enableRefresh(smartRefreshLayout, baseEmptyAdapterParent, !isFinish(pageIndex, limit, total));
    }

    /**
     * 控制底部无更多布局展示
     */
    public static void enableRefresh(SmartRefreshLayout smartRefreshLayout, BaseEmptyAdapterParent baseEmptyAdapterParent, boolean hasMore) {
        if (!hasMore) {
            if (smartRefreshLayout != null)
                smartRefreshLayout.setEnableLoadMore(false);
            baseEmptyAdapterParent.setNoMore(true);
        } else {
            if (smartRefreshLayout != null)
                smartRefreshLayout.setEnableLoadMore(true);
            baseEmptyAdapterParent.setNoMore(false);
        }
    }

    /**
     * 一个接口数据是否请求完毕
     */
    public static boolean isFinish(int pageIndex, int limit, int total) {
        return pageIndex * limit >= total;
    }

    /**
     * 瀑布流缺省,底部占屏设置
     */
    public static void staggeredRecyclerResetNormal(BaseEmptyAdapterParent baseEmptyAdapterParent, RecyclerView.ViewHolder holder, int position) {
        if (baseEmptyAdapterParent.getItemViewType(position) == -100 || baseEmptyAdapterParent.getItemViewType(position) == -200) {
            StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            lp.setFullSpan(true);
            holder.itemView.setLayoutParams(lp);
        }
    }

    /**
     * @param list 获取list移除第一个数据后的对象, 但不影响list的值
     */
    public static List filterListFirst(List list) {
        List resultList = new ArrayList();
        for (int i = 1; i < list.size(); i++) {
            resultList.add(list.get(i));
        }
        return resultList;
    }

    private static int maxRelyIntegerSmall(@NonNull List<Integer> integerList, int curInteger) {
        int resultInteger = -1;
        for (Integer integer : integerList) {
            if (integer != null && integer < curInteger) {
                if (integer > resultInteger) {
                    resultInteger = integer;
                }
            }
        }
        return resultInteger;
    }

    /**
     * recyclerView
     * use @Link{BaseEmptyAdapterParent}
     * 格局布局权重设置(缺省,尾部 横全)
     *
     * @param filterPositions 将这些位置设置为横满
     */
    public static void gridLayoutSpanSizeControl(final GridLayoutManager gridLayoutManager, final BaseEmptyAdapterParent baseEmptyAdapterParent, Integer... filterPositions) {
        if (gridLayoutManager == null || baseEmptyAdapterParent == null) {
            return;
        }
        List<Integer> filterPositionList = new ArrayList<>();
        if (filterPositions != null) {
            filterPositionList = Arrays.asList(filterPositions);
        }
        final List<Integer> finalFilterPositionList = filterPositionList;
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (finalFilterPositionList.contains(position)) {
                    return gridLayoutManager.getSpanCount();
                }
                if (baseEmptyAdapterParent.getItemCount() > 0) {
                    if (baseEmptyAdapterParent.isFooterItem(position)) {
                        return gridLayoutManager.getSpanCount();
                    } else {
                        return 1;
                    }
                } else {
                    return gridLayoutManager.getSpanCount();
                }
            }
        });
    }

    /**
     * 格局布局权重设置(缺省,尾部 横全)
     */
    public static void gridLayoutSpanSizeControl(final GridLayoutManager gridLayoutManager, final BaseEmptyAdapterParent baseEmptyAdapterParent) {
        if (gridLayoutManager == null || baseEmptyAdapterParent == null) {
            return;
        }
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (baseEmptyAdapterParent.getItemCount() > 0) {
                    if (baseEmptyAdapterParent.isFooterItem(position)) {
                        return gridLayoutManager.getSpanCount();
                    } else {
                        return 1;
                    }
                } else {
                    return gridLayoutManager.getSpanCount();
                }
            }
        });
    }

    /**
     * 滚动到顶部
     *
     * @param baseEmptyAdapterParent 如果仅仅是回到顶部功能, 不需要传
     */
    public static void initRecyclerScrollToFirst(@NonNull RecyclerView recyclerView, @NonNull final View iv_goTop, final BaseEmptyAdapterParent baseEmptyAdapterParent, final ListUtil.RecyclerScrollInterface recyclerScrollInterface) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == SCROLL_STATE_IDLE) {
                    int position = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                    if (position > 20) {
                        iv_goTop.animate().alpha(1f);
                    } else {
                        iv_goTop.animate().alpha(0f);
                    }
                    if (null != baseEmptyAdapterParent) {
                        if (baseEmptyAdapterParent.getItemCount() - 20 > 0 && position > baseEmptyAdapterParent.getItemCount() - 20) {
                            if (recyclerScrollInterface != null) {
                                recyclerScrollInterface.canLoadMore();
                            }
                        }
                    }
                } else {
                    iv_goTop.animate().alpha(0f);
                }
            }
        });
    }

    /**
     * 辅助{@link #initRecyclerScrollToFirst(RecyclerView, View, BaseEmptyAdapterParent, RecyclerScrollInterface)}
     */
    public interface RecyclerScrollInterface {
        void canLoadMore();
    }


    private static final String SEP1 = ",";
    private static final String SEP2 = "|";
    private static final String SEP3 = "=";

    /**
     * List转换String
     *
     * @param list :需要转换的List
     * @return String转换后的字符串
     */
    public static String list2String(List<?> list) {
        StringBuffer sb = new StringBuffer();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) == null || list.get(i) == "") {
                    continue;
                }
                // 如果值是list类型则调用自己
                if (list.get(i) instanceof List) {
                    sb.append(list2String((List<?>) list.get(i)));
                    sb.append(SEP1);
                } else if (list.get(i) instanceof Map) {
                    sb.append(MapToString((Map<?, ?>) list.get(i)));
                    sb.append(SEP1);
                } else {
                    sb.append(list.get(i));
                    sb.append(SEP1);
                }
            }
        }
        return sb.toString();
    }

    /**
     * Map转换String
     *
     * @param map :需要转换的Map
     * @return String转换后的字符串
     */
    public static String MapToString(Map<?, ?> map) {
        StringBuffer sb = new StringBuffer();
        // 遍历map
        for (Object obj : map.keySet()) {
            if (obj == null) {
                continue;
            }
            Object key = obj;
            Object value = map.get(key);
            if (value instanceof List<?>) {
                sb.append(key.toString() + SEP1 + list2String((List<?>) value));
                sb.append(SEP2);
            } else if (value instanceof Map<?, ?>) {
                sb.append(key.toString() + SEP1
                        + MapToString((Map<?, ?>) value));
                sb.append(SEP2);
            } else {
                sb.append(key.toString() + SEP3 + value.toString());
                sb.append(SEP2);
            }
        }
        return "M" + sb.toString();
    }
}
