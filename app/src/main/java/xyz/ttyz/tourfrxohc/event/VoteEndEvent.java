package xyz.ttyz.tourfrxohc.event;

import java.util.List;

import xyz.ttyz.tourfrxohc.models.UserModel;
import xyz.ttyz.tourfrxohc.models.game.HomeModel;

public class VoteEndEvent {
    List<UserModel> pkList;
    HomeModel homeModel;

    public VoteEndEvent(List<UserModel> pkList, HomeModel homeModel) {
        this.pkList = pkList;
        this.homeModel = homeModel;
    }

    public List<UserModel> getPkList() {
        return pkList;
    }

    public HomeModel getHomeModel() {
        return homeModel;
    }
}
