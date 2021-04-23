package xyz.ttyz.tourfrxohc.event;

import xyz.ttyz.tourfrxohc.models.game.HomeModel;

public class StartVoteEvent {
    HomeModel homeModel;

    public StartVoteEvent(HomeModel homeModel) {
        this.homeModel = homeModel;
    }

    public HomeModel getHomeModel() {
        return homeModel;
    }


}
