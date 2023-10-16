package xyz.ttyz.tourfrxohc.models;

import java.io.Serializable;

/**
 * @author 投投
 * @date 2023/10/16
 * @email 343315792@qq.com
 */
public class LocationModel implements Serializable {
    public long id;
    public String selectOne;
    public String selectTwo;

    public LocationModel(long id, String selectOne, String selectTwo) {
        this.id = id;
        this.selectOne = selectOne;
        this.selectTwo = selectTwo;
    }
}
