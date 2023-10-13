package xyz.ttyz.tourfrxohc.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.databinding.PropertyChangeRegistry;

import com.aigestudio.wheelpicker.WheelPicker;

import java.util.ArrayList;
import java.util.List;

import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.tourfrxohc.BR;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.DialogLocationBinding;


/**
 * @author 投投
 * @date 2023/10/12
 * @email 343315792@qq.com
 */
public class LocationDialog extends Dialog implements Observable {
    private static LocationDialog locationDialog;
    public static void showDialog(){
        if(locationDialog == null){
            locationDialog = new LocationDialog(ActivityManager.getInstance());
        }
        ActivityManager.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(!ActivityManager.getInstance().isDestroyed())
                    locationDialog.show();
            }
        });
    }

    DialogLocationBinding mBinding;
    @Bindable
    public  List<String> firstList = new ArrayList<>();
    @Bindable
    public  List<String> secondList = new ArrayList<>();
    private String selectOne, selectTwo;
    public LocationDialog(@NonNull Context context) {
        super(context, R.style.DialogLocationTheme);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_location, null);
        mBinding = DataBindingUtil.bind(view);
        mBinding.setContext(this);
        setContentView(view);
        setCancelable(true);
        setCanceledOnTouchOutside(true);

        for (int i = 0; i < 10; i++) {
            firstList.add("first_" + i);
            secondList.add("second_" + i);
        }
        notifyChange(BR.firstList);
        notifyChange(BR.secondList);
    }

    public WheelPicker.OnItemSelectedListener firstSelect = new WheelPicker.OnItemSelectedListener() {
        @Override
        public void onItemSelected(WheelPicker picker, Object data, int position) {
            selectOne = (String) data;
        }
    };
    public WheelPicker.OnItemSelectedListener secondSelect = new WheelPicker.OnItemSelectedListener() {
        @Override
        public void onItemSelected(WheelPicker picker, Object data, int position) {
            selectTwo = (String) data;
        }
    };

    public OnClickAdapter.onClickCommand cancelClick = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            dismiss();
        }
    };
    public OnClickAdapter.onClickCommand confirmClick = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            dismiss();
            System.out.println("选择的项目：" + selectOne + " " + selectTwo);
        }
    };

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
