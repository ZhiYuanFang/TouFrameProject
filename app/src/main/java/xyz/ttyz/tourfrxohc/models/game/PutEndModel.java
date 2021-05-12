package xyz.ttyz.tourfrxohc.models.game;

import java.io.Serializable;

public class PutEndModel implements Serializable {
    boolean isCurHasKey;
    int putNumber;

    public boolean isCurHasKey() {
        return isCurHasKey;
    }

    public void setCurHasKey(boolean curHasKey) {
        isCurHasKey = curHasKey;
    }

    public int getPutNumber() {
        return putNumber;
    }

    public void setPutNumber(int putNumber) {
        this.putNumber = putNumber;
    }
}
