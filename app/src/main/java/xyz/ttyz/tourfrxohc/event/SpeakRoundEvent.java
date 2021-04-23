package xyz.ttyz.tourfrxohc.event;

import java.util.List;

import xyz.ttyz.tourfrxohc.models.UserModel;
import xyz.ttyz.tourfrxohc.models.game.HomeModel;

public class SpeakRoundEvent {
    UserModel speakUser;
    HomeModel homeModel;

    public SpeakRoundEvent(UserModel speakUser, HomeModel homeModel) {
        this.speakUser = speakUser;
        this.homeModel = homeModel;
    }

    public UserModel getSpeakUser() {
        return speakUser;
    }

    public HomeModel getHomeModel() {
        return homeModel;
    }
}
