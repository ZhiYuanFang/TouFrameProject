package xyz.ttyz.tourfrxohc.models;


import java.util.List;

public class SocketEventModule {
    String roomId;
    List<UserModel> userModels;

    public SocketEventModule(String roomId, List<UserModel> userModels) {
        this.roomId = roomId;
        this.userModels = userModels;
    }

    public String getRoomId() {
        return roomId;
    }

    public List<UserModel> getUserModels() {
        return userModels;
    }
}
