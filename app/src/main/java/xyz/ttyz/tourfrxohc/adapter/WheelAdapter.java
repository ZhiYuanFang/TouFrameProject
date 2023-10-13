package xyz.ttyz.tourfrxohc.adapter;

import androidx.databinding.BindingAdapter;

import com.aigestudio.wheelpicker.WheelPicker;

import java.util.List;

/**
 * @author 投投
 * @date 2023/10/12
 * @email 343315792@qq.com
 */
public class WheelAdapter {
    @BindingAdapter("wheelData")
    public static void wheelDataSet(WheelPicker wheelPicker, List<String> wheelData){
        wheelPicker.setData(wheelData);
    }
    @BindingAdapter("wheelItemSelected")
    public static void wheelItemSelected(WheelPicker wheelPicker, WheelPicker.OnItemSelectedListener itemSelectedListener){
        wheelPicker.setOnItemSelectedListener(itemSelectedListener);
    }
}
