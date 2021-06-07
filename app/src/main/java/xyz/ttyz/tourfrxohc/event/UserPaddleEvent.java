package xyz.ttyz.tourfrxohc.event;

import xyz.ttyz.tourfrxohc.models.UserModel;

public class UserPaddleEvent {
    boolean isDead;//是否淘汰
    UserModel user;

    public UserPaddleEvent(boolean isDead, UserModel user) {
        this.isDead = isDead;
        this.user = user;
    }

    public boolean isDead() {
        return isDead;
    }

    public UserModel getUser() {
        return user;
    }
}
