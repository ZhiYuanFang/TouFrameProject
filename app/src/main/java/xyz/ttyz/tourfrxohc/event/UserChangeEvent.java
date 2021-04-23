package xyz.ttyz.tourfrxohc.event;

import xyz.ttyz.tourfrxohc.models.UserModel;

public class UserChangeEvent {
    UserModel userModel;
    boolean comeIn;

    public UserChangeEvent(UserModel userModel, boolean comeIn) {
        this.userModel = userModel;
        this.comeIn = comeIn;
    }

    public boolean isComeIn() {
        return comeIn;
    }

    public UserModel getUserModel() {
        return userModel;
    }
}
