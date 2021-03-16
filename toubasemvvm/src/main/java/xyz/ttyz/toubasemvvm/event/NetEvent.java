package xyz.ttyz.toubasemvvm.event;

import android.net.NetworkCapabilities;

public class NetEvent {
    public enum NetType{
        WIFI,
        CMWAP,
        AUTO
    }
    NetType netType;//网络连接类型
    boolean isAvailable;//网络是否连接


    public NetEvent(NetType netType, boolean isAvailable) {
        this.netType = netType;
        this.isAvailable = isAvailable;
    }

    public NetEvent(NetType netType) {
        this(netType, true);
    }

    public NetEvent(boolean isAvailable) {
        this(NetType.AUTO, isAvailable);
    }

    public NetType getNetType() {
        return netType;
    }

    public boolean isAvailable() {
        return isAvailable;
    }
}
