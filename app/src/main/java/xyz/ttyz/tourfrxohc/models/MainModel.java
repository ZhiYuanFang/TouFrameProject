package xyz.ttyz.tourfrxohc.models;

import androidx.databinding.BaseObservable;

import java.io.Serializable;
import java.util.List;

public class MainModel extends BaseObservable implements Serializable {
    int noticeNum;
    int courseNum;
    int myScore;
    int orderNums;
    int medalNum;
    int couponNum;
    List<ResorceModel> readHistory;

    public int getNoticeNum() {
        return noticeNum;
    }

    public int getCourseNum() {
        return courseNum;
    }

    public int getMyScore() {
        return myScore;
    }

    public int getOrderNums() {
        return orderNums;
    }

    public int getMedalNum() {
        return medalNum;
    }

    public int getCouponNum() {
        return couponNum;
    }

    public List<ResorceModel> getReadHistory() {
        return readHistory;
    }
}
