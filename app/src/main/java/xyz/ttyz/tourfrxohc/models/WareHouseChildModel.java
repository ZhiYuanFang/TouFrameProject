package xyz.ttyz.tourfrxohc.models;

import java.io.Serializable;

/**
 * @author 投投
 * @date 2023/10/19
 * @email 343315792@qq.com
 */
public class WareHouseChildModel implements Serializable {
    long id;
    String areaName;
    int status;
    long warehouseId;

    public long getId() {
        return id;
    }

    public String getAreaName() {
        return areaName;
    }

    public int getStatus() {
        return status;
    }

    public long getWarehouseId() {
        return warehouseId;
    }
}
