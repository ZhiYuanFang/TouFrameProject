package xyz.ttyz.tourfrxohc.event;

import java.util.List;

import xyz.ttyz.tourfrxohc.models.UserModel;
import xyz.ttyz.tourfrxohc.models.game.HomeModel;

public class RebackVoiceRoomEvent {
    List<UserModel> lastKeyUserList;//即将发言的人就是它【0】
    HomeModel homeModel;

    public RebackVoiceRoomEvent(List<UserModel> lastKeyUserList, HomeModel homeModel) {
        this.lastKeyUserList = lastKeyUserList;
        this.homeModel = homeModel;
    }

    public List<UserModel> getLastKeyUserList() {
        return lastKeyUserList;
    }

    public HomeModel getHomeModel() {
        return homeModel;
    }
}
