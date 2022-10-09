package xyz.ttyz.tourfrxohc.models;

import androidx.databinding.BaseObservable;

import java.io.Serializable;

public class TicketDetail extends BaseObservable implements Serializable {
    String begindate;
    String communicationId;// "4cd68b45-1840-4cce-9a0d-8aac607b2dba",
    String enddate;
    String id;//": null,
    String leftCount;//": null,
    String playSoundName;//": null,
    String sellTime;//": null,
    String ticketkindname;//": "无效票",
    String tktModelName;//": "无效票",
    String useTime;//": null,
    String useflag;//": "11",
    String useflagStr;//": "无效票"

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
