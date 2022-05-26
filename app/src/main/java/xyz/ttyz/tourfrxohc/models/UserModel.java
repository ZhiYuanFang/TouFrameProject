package xyz.ttyz.tourfrxohc.models;

import androidx.databinding.BaseObservable;

import java.io.Serializable;

public class UserModel extends BaseObservable implements Serializable {
    String type;//票据类型
    String nickname;
    String avatar;
    String accessToken;
    String cardNumber;//身份证号码
    String sex;//性别
    String name;//姓名

    public String getType() {
        return type;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getSex() {
        return sex;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setName(String name) {
        this.name = name;
    }
}
