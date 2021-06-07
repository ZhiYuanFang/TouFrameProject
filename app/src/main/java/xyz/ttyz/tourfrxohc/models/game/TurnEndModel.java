package xyz.ttyz.tourfrxohc.models.game;


import xyz.ttyz.tourfrxohc.models.UserModel;

public class TurnEndModel {
    boolean saveWin;
    ArmModel armModel;//掉落的武器
    UserModel earnArmUser;//获得掉落的武器

    public TurnEndModel(boolean saveWin, ArmModel armModel, UserModel earnArmUser) {
        this.saveWin = saveWin;
        this.armModel = armModel;
        this.earnArmUser = earnArmUser;
    }

    public boolean isSaveWin() {
        return saveWin;
    }

    public void setSaveWin(boolean saveWin) {
        this.saveWin = saveWin;
    }

    public ArmModel getArmModel() {
        return armModel;
    }

    public void setArmModel(ArmModel armModel) {
        this.armModel = armModel;
    }

    public UserModel getEarnArmUser() {
        return earnArmUser;
    }

    public void setEarnArmUser(UserModel earnArmUser) {
        this.earnArmUser = earnArmUser;
    }
}
