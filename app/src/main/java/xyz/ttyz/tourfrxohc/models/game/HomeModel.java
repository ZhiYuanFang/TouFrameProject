package xyz.ttyz.tourfrxohc.models.game;


import java.io.Serializable;
import java.util.List;

import xyz.ttyz.tourfrxohc.models.UserModel;

public class HomeModel implements Serializable {
    int limitNumber;
    long roomId;//房间id
    List<UserModel> roomUserList;//历史房间人员

    public HomeModel() {
    }

    public int getLimitNumber() {
        return limitNumber;
    }

    public long getRoomId() {
        return roomId;
    }

    public List<UserModel> getRoomUserList() {
        return roomUserList;
    }
}
