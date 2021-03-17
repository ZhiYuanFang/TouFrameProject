package xyz.ttyz.mylibrary.method;

/**
 * Created by tou on 2019/5/20.
 */

public class BaseModule<D> extends RfRxOHCBaseModule {
    int code;
    String msg;
    D data;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public D getData() {
        return data;
    }
}
