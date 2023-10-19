package xyz.ttyz.tourfrxohc.models;

import java.io.Serializable;

/**
 * @author 投投
 * @date 2023/10/19
 * @email 343315792@qq.com
 */
public class StatisticsModel implements Serializable {
    int totalStockedNum;//	integer
    // 在库货品个数
    int totalStockedQuantityNum;//	integer
    //            在库货品实际包含件数
    long warehouseAreaId;//	integer
    //            仓库区域id
    long warehouseId;//仓库id

    public int getTotalStockedNum() {
        return totalStockedNum;
    }

    public int getTotalStockedQuantityNum() {
        return totalStockedQuantityNum;
    }

    public long getWarehouseAreaId() {
        return warehouseAreaId;
    }

    public long getWarehouseId() {
        return warehouseId;
    }
}
