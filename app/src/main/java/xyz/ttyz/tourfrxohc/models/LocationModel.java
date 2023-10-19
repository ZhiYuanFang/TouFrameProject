package xyz.ttyz.tourfrxohc.models;

import java.io.Serializable;

/**
 * @author 投投
 * @date 2023/10/16
 * @email 343315792@qq.com
 */
public class LocationModel implements Serializable {
    public long warehouseAreaId;
    public long warehouseId;
    public String selectOne;
    public String selectTwo;

    public LocationModel() {
    }

    public LocationModel(long warehouseAreaId, long warehouseId,String selectOne, String selectTwo) {
        this.warehouseAreaId = warehouseAreaId;
        this.warehouseId = warehouseId;
        this.selectOne = selectOne;
        this.selectTwo = selectTwo;
    }
}
