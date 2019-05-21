package xyz.ttyz.tourfrxohc;

import xyz.ttyz.mylibrary.method.RfRxOHCBaseModule;

/**
 * Created by tou on 2019/5/20.
 */

public class BaseModule<D> extends RfRxOHCBaseModule {
    int err;
    String message;
    D data;

    public int getErr() {
        return err;
    }

    public void setErr(int err) {
        this.err = err;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }
}
