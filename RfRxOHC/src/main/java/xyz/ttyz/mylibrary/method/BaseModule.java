package xyz.ttyz.mylibrary.method;

/**
 * Created by tou on 2019/5/20.
 */

public class BaseModule<D> extends RfRxOHCBaseModule {
    int code;
    String message;
    D data;

    Page paging;

    public Page getPaging() {
        return paging;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return message;
    }

    public D getData() {
        return data;
    }

    public static class Page {
        int totalCount;

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }
    }
}
