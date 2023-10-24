package xyz.ttyz.tourfrxohc.models;

import java.io.Serializable;

/**
 * @author 投投
 * @date 2023/10/20
 * @email 343315792@qq.com
 */
public class PantRecordModel implements Serializable {
    long id;

    String startTime;
    String endTime;
    String stocktakeDuration;//盘点任务时间范围
    String successQuantity;//成功盘点个数
    String totalQuantity;

    public long getId() {
        return id;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getStocktakeDuration() {
        return stocktakeDuration;
    }

    public String getSuccessQuantity() {
        return successQuantity;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }
}
