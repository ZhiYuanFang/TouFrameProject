package xyz.ttyz.tourfrxohc.event;

import java.util.List;

import xyz.ttyz.tourfrxohc.models.UserModel;

public class RoleTypeConfirm {
    UserModel selfUserModel;

    public RoleTypeConfirm(UserModel selfUserModel) {
        this.selfUserModel = selfUserModel;
    }

    public UserModel getSelfUserModel() {
        return selfUserModel;
    }
}
