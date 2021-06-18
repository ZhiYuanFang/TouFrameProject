package xyz.ttyz.tourfrxohc.models.game;

/**
 * 武器形态
 */
public class ArmModel {
    long id;//武器ID
    String armName;//武器名字
    String armInfo;//武器介绍

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getArmName() {
        return armName;
    }

    public void setArmName(String armName) {
        this.armName = armName;
    }

    public String getArmInfo() {
        return armInfo;
    }

    public void setArmInfo(String armInfo) {
        this.armInfo = armInfo;
    }
}
