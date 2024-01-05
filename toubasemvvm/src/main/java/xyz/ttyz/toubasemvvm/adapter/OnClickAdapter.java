package xyz.ttyz.toubasemvvm.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import xyz.ttyz.toubasemvvm.utils.NetworkUtil;

public class OnClickAdapter {
    //防重复点击间隔(毫秒)
    public static final int CLICK_INTERVAL = 300;

    @SuppressLint("CheckResult")
    @BindingAdapter(value = {"onClickCommand", "noteJudgeBindWareHouse", "noteJudgeNet","noteJudgeWifi"}, requireAll = false)
    public static void onClickCommand(View view, final onClickCommand onClickCommand, final boolean noteJudgeBindWareHouse, final boolean noteJudgeNet, final boolean noteJudgeWifi) {
        Observable<? super Object> observable = RxView.clicks(view);
        observable = observable.throttleFirst(CLICK_INTERVAL, TimeUnit.MILLISECONDS);
        observable.subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                if (onClickCommand != null) {
                    //判断设备
                    if(!ApplicationUtils.touDelegate.isConnectSerial() && !noteJudgeNet){
                        DialogUtils.showSingleDialog("钥匙柜未链接", "请重新插拔钥匙柜链接端口重试");
                        return;
                    }
                    //判断网络
                    if(!NetworkUtil.isNetWorkConnected(view.getContext()) && !noteJudgeWifi){
                        DialogUtils.showDialog("网络未链接", "请重新连接网络后重试", new DialogUtils.DialogButtonModule("前往wifi设置", new DialogUtils.DialogClickDelegate() {
                            @Override
                            public void click(DialogUtils.DialogButtonModule dialogButtonModule) {
                                Intent intent = new Intent();

                                intent.setAction("android.net.wifi.PICK_WIFI_NETWORK");

                                view.getContext().startActivity(intent);
                            }
                        }));
                        return;
                    }

                    //判断仓库
                    if (!noteJudgeBindWareHouse) {
                        if (!ApplicationUtils.touDelegate.isBindWareHouse()) {
                            DialogUtils.showDialog("仓库未绑定", "请绑定仓库后使用", new DialogUtils.DialogButtonModule("前往绑定", new DialogUtils.DialogClickDelegate() {
                                @Override
                                public void click(DialogUtils.DialogButtonModule dialogButtonModule) {
                                    ApplicationUtils.touDelegate.gotoBindWareHouseActivity();
                                }
                            }));
                            return;
                        }
                    }
                    onClickCommand.click();
                }
            }
        });
    }


    public abstract static class onClickCommand {
        public abstract void click();
    }

    @BindingAdapter(value = {"doubleClickCommand"}, requireAll = false)
    public static void doubleClickCommand(View view,final onClickCommand onClickCommand) {
        Observable<? super Object> observable = RxView.clicks(view).share();
        observable
                .buffer(observable.debounce(CLICK_INTERVAL, TimeUnit.MILLISECONDS))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (onClickCommand != null) {
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
