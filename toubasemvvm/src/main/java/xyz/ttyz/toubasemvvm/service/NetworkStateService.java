package xyz.ttyz.toubasemvvm.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;

import androidx.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

import xyz.ttyz.toubasemvvm.event.NetEvent;
import xyz.ttyz.toubasemvvm.utils.NetworkUtil;

/**
 * @author 投投
 * @date 2023/12/29
 * @email 343315792@qq.com
 */
public class NetworkStateService extends Service {

    BroadcastReceiver mReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(ConnectivityManager.CONNECTIVITY_ACTION)){

                EventBus.getDefault().post(new NetEvent(NetworkUtil.isNetWorkConnected(NetworkStateService.this)));
            }
        }
    };
    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReciver, mFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReciver);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
