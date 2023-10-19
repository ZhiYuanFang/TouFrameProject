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
import com.trello.rxlifecycle2.LifecycleProvider;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import xyz.ttyz.mylibrary.method.RxOHCUtils;
import xyz.ttyz.mylibrary.protect.SharedPreferenceUtil;
import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.utils.ToastUtil;
import xyz.ttyz.tourfrxohc.BR;
import xyz.ttyz.tourfrxohc.BaseApplication;
import xyz.ttyz.tourfrxohc.MainActivity;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.activity.LoginActivity;
import xyz.ttyz.tourfrxohc.databinding.DialogLocationBinding;
import xyz.ttyz.tourfrxohc.event.LocationSelectEvent;
import xyz.ttyz.tourfrxohc.http.BaseSubscriber;
import xyz.ttyz.tourfrxohc.models.LocationModel;
import xyz.ttyz.tourfrxohc.models.WareHouseChildModel;
import xyz.ttyz.tourfrxohc.models.WareHouseModel;


/**
 * @author 投投
 * @date 2023/10/12
 * @email 343315792@qq.com
 */
public class LocationDialog extends Dialog implements Observable {
    private static LocationDialog locationDialog;
    private static CallBackDelegate callBackDelegate;
    public interface CallBackDelegate {
        void select(LocationModel locationModel);
    }
    public static void showDialog(CallBackDelegate delegate){
//        if(locationDialog == null){
            locationDialog = new LocationDialog(ActivityManager.getInstance());
//        }
        callBackDelegate = delegate;
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
    List<WareHouseModel> wareHouseModelList;
    List<WareHouseChildModel> wareHouseChildModelList;
    WareHouseChildModel selectWareHouseChildModel;
    private String selectOne, selectTwo;
    public LocationDialog(@NonNull Context context) {
        super(context, R.style.DialogLocationTheme);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_location, null);
        mBinding = DataBindingUtil.bind(view);
        mBinding.setContext(this);
        setContentView(view);
        setCancelable(true);
        setCanceledOnTouchOutside(true);

        new RxOHCUtils<>(context).executeApi(BaseApplication.apiService.getWarehouseList(), new BaseSubscriber<List<WareHouseModel>>((LifecycleProvider) ActivityManager.getInstance()) {
            @Override
            public void success(List<WareHouseModel> data) {
                wareHouseModelList = data;
                firstList.clear();
                for (WareHouseModel model: data) {
                    firstList.add(model.getWarehouseName());
                }
                notifyChange(BR.firstList);


                //视觉上面，不修改数据
                mBinding.wheelFirst.setSelectedItemPosition(0);
                selectFirst(firstList.get(0));
            }
        });

    }

    public WheelPicker.OnItemSelectedListener firstSelect = new WheelPicker.OnItemSelectedListener() {
        @Override
        public void onItemSelected(WheelPicker picker, Object data, int position) {
            selectFirst((String) data);
        }
    };

    private void selectFirst(String selectOne){
        this.selectOne = selectOne;

        WareHouseModel selectWareHouseModel = wareHouseModelList.get(firstList.indexOf(selectOne));

        new RxOHCUtils<>(getContext()).executeApi(BaseApplication.apiService.getWarehouseChildList(selectWareHouseModel.getId()), new BaseSubscriber<List<WareHouseChildModel>>((LifecycleProvider) ActivityManager.getInstance()) {
            @Override
            public void success(List<WareHouseChildModel> data) {
                wareHouseChildModelList = data;
                secondList.clear();
                for (WareHouseChildModel model: data) {
                    secondList.add(model.getAreaName());
                }
                notifyChange(BR.secondList);
            }
        });
    }
    public WheelPicker.OnItemSelectedListener secondSelect = new WheelPicker.OnItemSelectedListener() {
        @Override
        public void onItemSelected(WheelPicker picker, Object data, int position) {
            selectTwo = (String) data;
            selectWareHouseChildModel = wareHouseChildModelList.get(secondList.indexOf(selectTwo));

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
            if(selectWareHouseChildModel == null) {
                ToastUtil.showToast("请选择");
                return;
            }
            dismiss();

            if(callBackDelegate != null){
                callBackDelegate.select(new LocationModel(selectWareHouseChildModel.getId(),selectWareHouseChildModel.getWarehouseId(), selectOne, selectTwo));
            }
            EventBus.getDefault().post(new LocationSelectEvent());
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
