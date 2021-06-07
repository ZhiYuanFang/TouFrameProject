package xyz.ttyz.tourfrxohc.event;

import java.util.List;

import xyz.ttyz.tourfrxohc.models.UserModel;

public class GoKeyRoomEvent {
    long roomId;
    UserModel goKeyUser;

    public GoKeyRoomEvent(long roomId, UserModel goKeyUser) {
        this.roomId = roomId;
        this.goKeyUser = goKeyUser;
    }

    public long getRoomId() {
        return roomId;
    }

    public UserModel getGoKeyUser() {
        return goKeyUser;
    }
}
