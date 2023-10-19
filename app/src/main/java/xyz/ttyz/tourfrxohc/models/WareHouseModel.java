package xyz.ttyz.tourfrxohc.models;

import java.io.Serializable;

/**
 * @author 投投
 * @date 2023/10/19
 * @email 343315792@qq.com
 */
public class WareHouseModel implements Serializable {
    long id;
    int status;//仓库状态code。0，未启用；10，已启用；20，已停用
    String warehouseName;

    public long getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }

    public String getWarehouseName() {
        return warehouseName;
    }
}
