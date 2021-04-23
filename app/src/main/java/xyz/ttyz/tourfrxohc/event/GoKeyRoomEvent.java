package xyz.ttyz.tourfrxohc.event;

import java.util.List;

import xyz.ttyz.tourfrxohc.models.UserModel;

public class GoKeyRoomEvent {
    long roomId;
    List<UserModel> goKeyUserList;

    public GoKeyRoomEvent(long roomId, List<UserModel> goKeyUserList) {
        this.roomId = roomId;
        this.goKeyUserList = goKeyUserList;
    }

    public long getRoomId() {
        return roomId;
    }

    public List<UserModel> getGoKeyUserList() {
        return goKeyUserList;
    }
}
