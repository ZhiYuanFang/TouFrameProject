package xyz.ttyz.tourfrxohc.models;

import androidx.databinding.BaseObservable;

import java.io.Serializable;

public class UserModel extends BaseObservable implements Serializable {
    String nickname;
    String avatar;
    String accessToken;

    public String getNickname() {
        return nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
