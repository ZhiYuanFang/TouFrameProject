package xyz.ttyz.tourfrxohc.event;

public class KeyPuttingEvent {
    int countDownTime;

    public KeyPuttingEvent(int countDownTime) {
        this.countDownTime = countDownTime;
    }

    public int getCountDownTime() {
        return countDownTime;
    }
}
