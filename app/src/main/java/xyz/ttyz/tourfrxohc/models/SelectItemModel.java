package xyz.ttyz.tourfrxohc.models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import xyz.ttyz.tourfrxohc.BR;


public class SelectItemModel extends BaseObservable {
    String selectStr;
    int strColor;

    public SelectItemModel(String selectStr, int strColor) {
        this.selectStr = selectStr;
        this.strColor = strColor;
    }

    @Bindable
    public String getSelectStr() {
        return selectStr;
    }

    public void setSelectStr(String selectStr) {
        this.selectStr = selectStr;
        notifyPropertyChanged(BR.selectStr);
    }

    @Bindable
    public int getStrColor() {
        return strColor;
    }

    public void setStrColor(int strColor) {
        this.strColor = strColor;
        notifyPropertyChanged(BR.strColor);
    }
}
