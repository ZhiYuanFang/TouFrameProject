package xyz.ttyz.mylibrary.method;

import xyz.ttyz.mylibrary.method.RfRxOHCBaseModule;

/**
 * Created by tou on 2019/5/20.
 */

public class BaseModule<D> extends RfRxOHCBaseModule {
    int code;
    String message;
    D data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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
