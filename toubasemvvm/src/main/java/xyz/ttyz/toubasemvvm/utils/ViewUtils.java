package xyz.ttyz.toubasemvvm.utils;

import android.view.View;
import android.view.ViewGroup;
public class ViewUtils {
    //        popView.measure(ViewUtils.makeDropDownMeasureSpec(customPopWindow.getWidth()), ViewUtils.makeDropDownMeasureSpec(customPopWindow.getHeight()));
    @SuppressWarnings("ResourceType")
    public static int makeDropDownMeasureSpec(int measureSpec) {
        int mode;
        if (measureSpec == ViewGroup.LayoutParams.WRAP_CONTENT) {
            mode = View.MeasureSpec.UNSPECIFIED;
        } else {
            mode = View.MeasureSpec.EXACTLY;
        }
        return View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(measureSpec), mode);
    }


}
