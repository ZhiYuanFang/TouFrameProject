package xyz.ttyz.mylibrary.method;

/**
 * Created by tou on 2019/5/20.
 */

public class BaseModule<D> extends RfRxOHCBaseModule {
    int code;
    String Message;
    D Ex;
    boolean Success;

    public boolean isSuccess() {
        return Success;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return Message;
    }

    public D getEx() {
        return Ex;
    }
}
