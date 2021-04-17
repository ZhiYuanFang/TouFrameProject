package xyz.ttyz.tourfrxohc.models.game;

import xyz.ttyz.tourfrxohc.models.UserModel;

public class VoiceModel {
    SysVoiceModel sys;
    UserModel user;

    public VoiceModel(UserModel userModel){
        user = userModel;
    }

    public SysVoiceModel getSys() {
        return sys;
    }

    public UserModel getUser() {
        return user;
    }
}
