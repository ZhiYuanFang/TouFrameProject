package xyz.ttyz.toubasemvvm.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TouchFrameLayout extends FrameLayout {
    boolean canInterceptTouch;
    public TouchFrameLayout(@NonNull Context context) {
        super(context);
    }

    public TouchFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TouchFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(!canInterceptTouch) return true;
        return super.onInterceptTouchEvent(ev);
    }

    public boolean isCanInterceptTouch() {
        return canInterceptTouch;
    }

    public void setCanInterceptTouch(boolean canInterceptTouch) {
        this.canInterceptTouch = canInterceptTouch;
    }
}
