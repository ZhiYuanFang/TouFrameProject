package xyz.ttyz.mylibrary.method;

import retrofit2.http.POST;

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

    public String getMsg() {
        return message;
    }

    public D getData() {
        return data;
    }
}
