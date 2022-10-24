package xyz.ttyz.tourfrxohc.models;

import androidx.databinding.BaseObservable;

import java.io.Serializable;

public class TicketDetail extends BaseObservable implements Serializable {
    String begindate;
    String communicationId;// "4cd68b45-1840-4cce-9a0d-8aac607b2dba",
    String enddate;
    String id;//": null,检票类型识别号（检票接口调用）
    String leftCount;//": 剩余检票次数,
    String playSoundName;//": null,
    String sellTime;//": 购买时间,
    String ticketkindname;//": "无效票",
    String tktModelName;//": "无效票",
    String useTime;//": 使用时间,
    String useflag;//": "11",
    String useflagStr;//": "无效票"

    String extend;// 扩展字段（调用检票接口时原样提供）
    String auditMode;// 检票授权标志: 00:不用授权 01:需要授权
    String healthShow;// 是否需要校验健康码 T F
    String healthCode;// 健康码信息：(1:绿码)(2:黄码)(3:红码)(4:灰码)
    String travelShow;// 是否需要校验行程码
    String nucleinShow;// 是否需要校验核酸
    String nucleinResult;// 核酸信息：(1:校验通过)(2:校验不通过)(3:无结果)
    String vaccinumShow;// 是否需要校验疫苗
    String vaccinumResult;// 疫苗信息：(0:0 针)(1:1 针)(2:2 针)(3:3 针)~~~~

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getAuditMode() {
        return auditMode;
    }

    public void setAuditMode(String auditMode) {
        this.auditMode = auditMode;
    }

    public String getHealthShow() {
        return healthShow;
    }

    public void setHealthShow(String healthShow) {
        this.healthShow = healthShow;
    }

    public String getHealthCode() {
        return healthCode;
    }

    public void setHealthCode(String healthCode) {
        this.healthCode = healthCode;
    }

    public String getTravelShow() {
        return travelShow;
    }

    public void setTravelShow(String travelShow) {
        this.travelShow = travelShow;
    }

    public String getNucleinShow() {
        return nucleinShow;
    }

    public void setNucleinShow(String nucleinShow) {
        this.nucleinShow = nucleinShow;
    }

    public String getNucleinResult() {
        return nucleinResult;
    }

    public void setNucleinResult(String nucleinResult) {
        this.nucleinResult = nucleinResult;
    }

    public String getVaccinumShow() {
        return vaccinumShow;
    }

    public void setVaccinumShow(String vaccinumShow) {
        this.vaccinumShow = vaccinumShow;
    }

    public String getVaccinumResult() {
        return vaccinumResult;
    }

    public void setVaccinumResult(String vaccinumResult) {
        this.vaccinumResult = vaccinumResult;
    }

    public String getBegindate() {
        return begindate;
    }

    public void setBegindate(String begindate) {
        this.begindate = begindate;
    }

    public String getCommunicationId() {
        return communicationId;
    }

    public void setCommunicationId(String communicationId) {
        this.communicationId = communicationId;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLeftCount() {
        return leftCount;
    }

    public void setLeftCount(String leftCount) {
        this.leftCount = leftCount;
    }

    public String getPlaySoundName() {
        return playSoundName;
    }

    public void setPlaySoundName(String playSoundName) {
        this.playSoundName = playSoundName;
    }

    public String getSellTime() {
        return sellTime;
    }

    public void setSellTime(String sellTime) {
        this.sellTime = sellTime;
    }

    public String getTicketkindname() {
        return ticketkindname;
    }

    public void setTicketkindname(String ticketkindname) {
        this.ticketkindname = ticketkindname;
    }

    public String getTktModelName() {
        return tktModelName;
    }

    public void setTktModelName(String tktModelName) {
        this.tktModelName = tktModelName;
    }

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public String getUseflag() {
        return useflag;
    }

    public void setUseflag(String useflag) {
        this.useflag = useflag;
    }

    public String getUseflagStr() {
        return useflagStr;
    }

    public void setUseflagStr(String useflagStr) {
        this.useflagStr = useflagStr;
    }
}
