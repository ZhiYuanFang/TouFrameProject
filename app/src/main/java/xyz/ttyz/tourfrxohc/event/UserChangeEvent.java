package xyz.ttyz.tourfrxohc.event;

import xyz.ttyz.tourfrxohc.models.UserModel;

public class UserChangeEvent {
    UserModel userModel;

    public UserChangeEvent(UserModel userModel) {
        this.userModel = userModel;
    }

    public UserModel getUserModel() {
        return userModel;
    }
}
