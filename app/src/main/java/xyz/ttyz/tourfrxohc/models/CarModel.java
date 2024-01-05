package xyz.ttyz.tourfrxohc.models;

import androidx.annotation.IdRes;
import androidx.databinding.Bindable;
import androidx.databinding.Observable;
import androidx.databinding.PropertyChangeRegistry;

import java.io.Serializable;
import java.util.Objects;

import xyz.ttyz.tourfrxohc.BR;
import xyz.ttyz.tourfrxohc.R;

/**
 * @author 投投
 * @date 2023/12/26
 * @email 343315792@qq.com
 */
public class CarModel implements Serializable, Observable {
    public String carUnique;//"VNNBBGC33SEG4DC23"
    public String carModel;//"中规 奥迪 奥迪A6L 奥迪A6L 2023款 改款 40 TFSI 豪华致雅型 白金色 黑色"
    public String staffId;//405
    public String keyId;//02287E26


    //以下为本地逻辑辅助处理
    private boolean isSelect;

    private boolean isOpen;

    private int boxNum;//对应的是哪个门，由 DefaultUtils.getDoorAddress 赋值
    public boolean isErrorDoor;//取钥匙的时候作为辅助判断 本地数据

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
        return boxNum;
    }

    public void setDoorNumber(int doorNumber) {
        this.boxNum = doorNumber;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarModel carModel = (CarModel) o;
        return carUnique.equals(carModel.carUnique);
    }

    @Override
    public int hashCode() {
        return Objects.hash(carUnique);
    }
}
