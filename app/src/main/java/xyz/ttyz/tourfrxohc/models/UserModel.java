package xyz.ttyz.tourfrxohc.models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.io.Serializable;
import java.util.Objects;

import xyz.ttyz.tourfrxohc.BR;

public class UserModel extends BaseObservable implements Serializable {
    long id;
    String avatar;
    boolean isSpeaking;
    PlayStatus playStatus;
    String nickname;
    String accessToken;
    String password;
    int level;//等级
    int comeInType;//0 离开 1 进入
    String phone;
    byte[] voiceBytes;//语音流

    public void setVoiceBytes(byte[] bytes){
        voiceBytes = bytes;
    }


    public void setComeInType(int comeInType){
        this.comeInType = comeInType;
        notifyPropertyChanged(BR.comeInType);
    }
    @Bindable
    public int getComeInType() {
        return comeInType;
    }

    public byte[] getVoiceBytes() {
        return voiceBytes;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public int getLevel() {
        return level;
    }


    @Bindable
    public PlayStatus getPlayStatus() {
        return playStatus;
    }

    public void setPlayStatus(PlayStatus playStatus) {
        this.playStatus = playStatus;
        notifyPropertyChanged(BR.playStatus);
    }

    @Bindable
    public boolean isSpeaking() {
        return isSpeaking;
    }

    public void setSpeaking(boolean speaking) {
        isSpeaking = speaking;
        notifyPropertyChanged(BR.speaking);
    }

    public void setId(long id) {
        this.id = id;
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

    public long getId() {
        return id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserModel)) return false;
        UserModel userModel = (UserModel) o;
        return id == userModel.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
