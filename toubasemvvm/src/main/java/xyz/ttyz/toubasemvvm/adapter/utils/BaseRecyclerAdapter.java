package xyz.ttyz.toubasemvvm.adapter.utils;

import android.view.ViewGroup;

import androidx.databinding.Bindable;
import androidx.databinding.Observable;
import androidx.databinding.PropertyChangeRegistry;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

import xyz.ttyz.toubasemvvm.BR;

/**
 * Created by toutou on 2018/7/19.
 * 多样式适配器
 */

public class BaseRecyclerAdapter extends RecyclerView.Adapter implements Observable {
    protected NormalAdapterDelegate normalAdapterDelegate;
    protected List<Object> list = initList();

    public BaseRecyclerAdapter(NormalAdapterDelegate normalAdapterDelegate) {
        this.normalAdapterDelegate = normalAdapterDelegate;
        if (list == null) {
            list = initList();
        }
    }
    
    protected List<Object> initList(){
        return new ArrayList<>();
    }

    public void clear() {
        if (list == null) {
            list = initList();
        }
        list.clear();
        notifyDataSetChanged();
        notifyChange(BR.data);
    }

    public void add(int index, Object t) {
        if (t == null) {
            return;
        }
        if (list == null) {
            list = initList();
        }
        list.add(index, t);
        notifyItemInserted(index);
        notifyChange(BR.data);
    }

    public void add(Object t) {
        if (list == null) {
            list = initList();
        }
        if (t != null) {
            list.add(t);
            if (list.size() < 2) {
                notifyDataSetChanged();
            } else {
                notifyItemInserted(list.size() - 1);
            }
        }
        notifyChange(BR.data);
    }

    public int getPositionOfFirstObject(Object obj) {
        if (null == list) {
            return -1;
        }
        for (int i = 0; i < list.size(); i++) {
            if (getItem(i) != null && obj != null && getItem(i).getClass() == obj.getClass()) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 移除position开始到后面的所有数据
     */
    public void clearFromPosition(int position) {
        if (list != null) {
            while (list.size() > position) {
                list.remove(position);
                notifyChange(BR.data);
//                notifyDataSetChanged();
                notifyItemRangeRemoved(position, list.size() - position);
            }
        }
    }

    public void addAll(List ts) {
        if (list == null) {
            list = initList();
        }
        if (null == ts) {
            ts = new ArrayList();
        }
        if(ts.isEmpty()) return;
//        int pos = list.size();
        list.addAll(ts);
        notifyChange(BR.data);
//        notifyItemRangeInserted(pos, ts.size());//会溢出 原因不详
        notifyDataSetChanged();
    }

    public void addAll(int index, List ts) {
        if (list == null) {
            list = initList();
        }
        if (null == ts) {
            ts = new ArrayList();
        }
        list.addAll(index, ts);
        notifyItemRangeInserted(index, ts.size());
        notifyChange(BR.data);
//        notifyDataSetChanged();
    }

    public void setList(List ts) {
        clear();
        addAll(ts);
    }

    @Bindable
    public List getData() {
        return list;
    }


    public void remove(Object o) {
        if (null != o && null != list && list.contains(o)) {
            int pos = list.indexOf(o);
            list.remove(o);
            notifyItemRemoved(pos);
            notifyChange(BR.data);
        }
    }

    public void removeAll(List curList){
        if (null != list && curList != null && list.containsAll(curList)  && !curList.isEmpty()) {
            int pos = list.indexOf(curList.get(0));
            list.removeAll(curList);
            notifyItemRangeRemoved(pos, curList.size());
//            notifyDataSetChanged();
            notifyChange(BR.data);
        }
    }

    public void remove(int position) {
        if (null != list && list.size() > position) {
            list.remove(position);
            notifyItemRemoved(position);
        }
        notifyChange(BR.data);
    }
    public void insteadObject(Object oldObj, Object newObj) {
        if (oldObj == null || newObj == null) {
            return;
        }
        int position = 0;
        for (int i = 0; i < list.size(); i++) {
            if (getItem(i) == oldObj) {
                position = i;
            }
        }
        remove(oldObj);
        list.add(position, newObj);
        notifyDataSetChanged();
        notifyChange(BR.data);
    }

    public Object getItem(int position) {
        if (position < list.size() && position > 0) {
            return list.get(position);
        } else {
            if (list.size() > 0) {
                return list.get(0);
            } else {
                return null;
            }
        }
    }

    public boolean isFooterItem(int position) {
        return position >= list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (normalAdapterDelegate != null) {
            return normalAdapterDelegate.getItemViewType(position);
        } else {
            return -1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (normalAdapterDelegate == null) return null;
        return normalAdapterDelegate.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (normalAdapterDelegate != null)
            normalAdapterDelegate.onBindViewHolder(holder, position);

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public interface NormalAdapterDelegate {
        int getItemViewType(int position);

        RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

        void onBindViewHolder(RecyclerView.ViewHolder holder, int position);
    }

    private transient PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();

    protected synchronized void notifyChange(int propertyId) {
        if (propertyChangeRegistry == null) {
            propertyChangeRegistry = new PropertyChangeRegistry();
        }
        propertyChangeRegistry.notifyChange(this, propertyId);
    }

    @Override
    public synchronized void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        if (propertyChangeRegistry == null) {
            propertyChangeRegistry = new PropertyChangeRegistry();
        }
        propertyChangeRegistry.add(callback);

    }

    @Override
    public synchronized void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        if (propertyChangeRegistry != null) {
            propertyChangeRegistry.remove(callback);
        }
    }

}
