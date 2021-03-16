package xyz.ttyz.toubasemvvm.adapter;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;

import androidx.databinding.BindingAdapter;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import xyz.ttyz.tou_example.init.ApplicationUtils;
import xyz.ttyz.toubasemvvm.utils.DialogUtils;

public class OnClickAdapter {
    //防重复点击间隔(毫秒)
    public static final int CLICK_INTERVAL = 300;

    @SuppressLint("CheckResult")
    @BindingAdapter(value = {"onClickCommand", "noteJudgeLogin"}, requireAll = false)
    public static void onClickCommand(View view, final onClickCommand onClickCommand, final boolean noteJudgeLogin) {
        Observable<? super Object> observable = RxView.clicks(view);
        observable = observable.throttleFirst(CLICK_INTERVAL, TimeUnit.MILLISECONDS);
        observable.subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                if (onClickCommand != null) {
                    if (!noteJudgeLogin) {
                        if (!ApplicationUtils.touDelegate.isLogin()) {
                            curClickCommand = onClickCommand;
                            DialogUtils.showDialog("登录失效", "请重新登录后体验", new DialogUtils.DialogButtonModule("前往登录", new DialogUtils.DialogClickDelegate() {
                                @Override
                                public void click(DialogUtils.DialogButtonModule dialogButtonModule) {
                                    ApplicationUtils.touDelegate.gotoLoginActivity();
                                }
                            }));
                            return;
                        }
                    }
                    curClickCommand = null;
                    onClickCommand.click();
                }
            }
        });
    }

    public static onClickCommand curClickCommand;//服务登录事件

    public abstract static class onClickCommand {
        public abstract void click();
    }

    @BindingAdapter(value = {"doubleClickCommand", "noteJudgeLogin"}, requireAll = false)
    public static void doubleClickCommand(View view,final onClickCommand onClickCommand,final boolean noteJudgeLogin) {
        Observable<? super Object> observable = RxView.clicks(view).share();
        observable
                .buffer(observable.debounce(CLICK_INTERVAL, TimeUnit.MILLISECONDS))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (onClickCommand != null) {
                            if (!noteJudgeLogin && false) {
                                if (!ApplicationUtils.touDelegate.isLogin()) {
                                    DialogUtils.showDialog("登录失效", "请重新登录后体验", new DialogUtils.DialogButtonModule("确定", new DialogUtils.DialogClickDelegate() {
                                        @Override
                                        public void click(DialogUtils.DialogButtonModule dialogButtonModule) {
                                            curClickCommand = onClickCommand;
                                            ApplicationUtils.touDelegate.gotoLoginActivity();
                                        }
                                    }));
                                    return;
                                }
                            }
                            curClickCommand = null;
                            onClickCommand.click();
                        }
                    }
                });
    }

    private static float downX, downY;

    @BindingAdapter(value = {"longClickCommand"}, requireAll = false)
    public static void longClickCommand(final View view,final onLongClickCommand onLongClickCommand) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = event.getX();
                        downY = event.getY();
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                    case MotionEvent.ACTION_UP:
                        downX = 0;
                        downY = 0;
                        break;
                }
                return false;
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onLongClickCommand != null) {
                    onLongClickCommand.longClick(view, downX, downY);
                }
                return true;
            }
        });
    }

    public interface onLongClickCommand {
        void longClick(View view, float downX, float downY);
    }

}
