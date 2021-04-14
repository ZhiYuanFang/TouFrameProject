package xyz.ttyz.tourfrxohc.models.game;


import java.io.Serializable;
import java.util.List;

import xyz.ttyz.tourfrxohc.models.UserModel;

public class HomeModel implements Serializable {
    int limitNumber;
    int type;//房间类型 语音房0，钥匙房1，结束后的聊嗨房2
    long roomId;//房间id
    List<UserModel> roomUserList;//历史房间人员

    public HomeModel() {
    }

    public int getType() {
        return type;
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
