package xyz.ttyz.tourfrxohc.event;

import xyz.ttyz.tourfrxohc.models.game.HomeModel;

public class GameEndEvent {
    HomeModel homeModel;

    public GameEndEvent(HomeModel homeModel) {
        this.homeModel = homeModel;
    }

    public HomeModel getHomeModel() {
        return homeModel;
    }
}
