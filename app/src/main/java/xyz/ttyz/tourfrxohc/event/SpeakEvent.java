package xyz.ttyz.tourfrxohc.event;

import xyz.ttyz.tourfrxohc.models.UserModel;

public class SpeakEvent {
    UserModel speakUser;
    int status;//0 正在发言 1结束发言
    int countDownTime;//倒计时

    public SpeakEvent(UserModel speakUser, int status, int countDownTime) {
        this.speakUser = speakUser;
        this.status = status;
        this.countDownTime = countDownTime;
    }

    public UserModel getSpeakUser() {
        return speakUser;
    }

    public int getStatus() {
        return status;
    }

    public int getCountDownTime() {
        return countDownTime;
    }
}
