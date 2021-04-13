package xyz.ttyz.tourfrxohc.models;


import java.util.List;

public class SocketEventModule {
    long roomId;
    List<UserModel> userModels;

    public SocketEventModule(long roomId, List<UserModel> userModels) {
        this.roomId = roomId;
        this.userModels = userModels;
    }

    public long getRoomId() {
        return roomId;
    }

    public List<UserModel> getUserModels() {
        return userModels;
    }
}
