package xyz.ttyz.tourfrxohc.models;

import androidx.annotation.IdRes;
import androidx.databinding.Bindable;
import androidx.databinding.Observable;
import androidx.databinding.PropertyChangeRegistry;

import java.io.Serializable;

import xyz.ttyz.tourfrxohc.BR;
import xyz.ttyz.tourfrxohc.R;

/**
 * @author 投投
 * @date 2023/12/26
 * @email 343315792@qq.com
 */
public class CarModel implements Serializable, Observable {
    private boolean isSelect;

    private boolean isOpen;

    private int doorNumber;//对应的是哪个门，由 DefaultUtils.getDoorAddress 赋值

    @Bindable
    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
        notifyChange(BR.select);
    }

    @Bindable
    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
        notifyChange(BR.open);
    }

    @Bindable
    public int getDoorNumber() {
        return doorNumber;
    }

    public void setDoorNumber(int doorNumber) {
        this.doorNumber = doorNumber;
        notifyChange(BR.doorNumber);
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
