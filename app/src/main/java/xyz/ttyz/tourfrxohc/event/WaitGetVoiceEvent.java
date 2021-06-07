package xyz.ttyz.tourfrxohc.event;

public class WaitGetVoiceEvent {
    int countDownTime;

    public WaitGetVoiceEvent(int countDownTime) {
        this.countDownTime = countDownTime;
    }

    public int getCountDownTime() {
        return countDownTime;
    }
}
