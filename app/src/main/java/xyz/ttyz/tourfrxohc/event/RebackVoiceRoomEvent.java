package xyz.ttyz.tourfrxohc.event;

import java.util.List;

import xyz.ttyz.tourfrxohc.models.UserModel;

public class RebackVoiceRoomEvent {
    List<UserModel> lastKeyUserList;//即将发言的人就是它【0】

    public RebackVoiceRoomEvent(List<UserModel> lastKeyUserList) {
        this.lastKeyUserList = lastKeyUserList;
    }

    public List<UserModel> getLastKeyUserList() {
        return lastKeyUserList;
    }
}
