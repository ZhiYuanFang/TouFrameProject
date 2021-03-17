package xyz.ttyz.tourfrxohc.models;

import androidx.databinding.BaseObservable;

import java.io.Serializable;

public class UserModel extends BaseObservable implements Serializable {
    String name;
    String avatar;
    String accessToken;

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
