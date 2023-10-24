package xyz.ttyz.tourfrxohc.models;

import java.io.Serializable;
import java.util.List;

/**
 * @author 投投
 * @date 2023/10/16
 * @email 343315792@qq.com
 */
public class GoodsModel implements Serializable {
    String barcodeNo;
    String coverImageUrl;
    String enteringTimeStr;
    String exitingTimeStr;
    String goodsActualNo;
    long id;
    List<String> imageUrls;
    String priceMinStr;
    String quantity;
    String remark;
    int status;//库存状态：0，待入库；10，已入库；20，已出库
    String statusDesc;
    long warehouseAreaId;
    long warehouseId;

    String detailStatus;//盘点任务明细状态
    String detailStatusDesc;//盘点任务明细状态描述

    public GoodsModel() {
    }

    public GoodsModel(String barcodeNo) {
        this.barcodeNo = barcodeNo;
    }

    public String getDetailStatus() {
        return detailStatus;
    }

    public String getDetailStatusDesc() {
        return detailStatusDesc;
    }

    public String getBarcodeNo() {
        return barcodeNo;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setBarcodeNo(String barcodeNo) {
        this.barcodeNo = barcodeNo;
    }

    public String getEnteringTimeStr() {
        return enteringTimeStr;
    }

    public void setEnteringTimeStr(String enteringTimeStr) {
        this.enteringTimeStr = enteringTimeStr;
    }

    public String getExitingTimeStr() {
        return exitingTimeStr;
    }

    public void setExitingTimeStr(String exitingTimeStr) {
        this.exitingTimeStr = exitingTimeStr;
    }

    public String getGoodsActualNo() {
        return goodsActualNo;
    }

    public void setGoodsActualNo(String goodsActualNo) {
        this.goodsActualNo = goodsActualNo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getPriceMinStr() {
        return priceMinStr;
    }

    public void setPriceMinStr(String priceMinStr) {
        this.priceMinStr = priceMinStr;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public long getWarehouseAreaId() {
        return warehouseAreaId;
    }

    public void setWarehouseAreaId(long warehouseAreaId) {
        this.warehouseAreaId = warehouseAreaId;
    }

    public long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(long warehouseId) {
        this.warehouseId = warehouseId;
    }
}
