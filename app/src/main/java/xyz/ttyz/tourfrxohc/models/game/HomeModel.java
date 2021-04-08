package xyz.ttyz.tourfrxohc.models.game;


import java.util.List;

import xyz.ttyz.tourfrxohc.models.UserModel;

public class HomeModel {
    String roomId;//房间id
    boolean isInHome;//我是否在这个房间
    List<UserModel> hisMemberList;//历史房间人员

    public HomeModel(String roomId, boolean isInHome, List<UserModel> hisMemberList) {
        this.roomId = roomId;
        this.isInHome = isInHome;
        this.hisMemberList = hisMemberList;
    }

    public String getRoomId() {
        return roomId;
    }

    public boolean isInHome() {
        return isInHome;
    }

    public List<UserModel> getHisMemberList() {
        return hisMemberList;
    }
}
