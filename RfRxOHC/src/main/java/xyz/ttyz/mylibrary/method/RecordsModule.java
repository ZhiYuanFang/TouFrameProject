package xyz.ttyz.mylibrary.method;

import java.io.Serializable;

/**
 * @author 投投
 * @date 2023/10/19
 * @email 343315792@qq.com
 */
public class RecordsModule<D> implements Serializable {
    int current;
    int limit;
    int offset;
    int pages;
    D records;
    int size;
    int total;

    public int getCurrent() {
        return current;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    public int getPages() {
        return pages;
    }

    public D getRecords() {
        return records;
    }

    public int getSize() {
        return size;
    }

    public int getTotal() {
        return total;
    }
}