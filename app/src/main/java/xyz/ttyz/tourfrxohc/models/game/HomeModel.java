package xyz.ttyz.tourfrxohc.models.game;


import java.io.Serializable;
import java.util.List;

import xyz.ttyz.tourfrxohc.models.UserModel;

public class HomeModel implements Serializable{
    int limitNumber;
    boolean gameStart;//游戏是否开始，还是正在匹配中
    long roomId;//房间id
    List<UserModel> roomUserList;//房间人员
    List<UserModel> waitKeyPutMemberList;//待投币人员
    UserModel speakingUserModel;//指定发言人员
    int turnRoundNumber;//轮次 1，2，3

    /**
     * {@link ActionType}
     */
    int actionType;//动作类型

    public int getTurnRoundNumber() {
        return turnRoundNumber;
    }

    public void setTurnRoundNumber(int turnRoundNumber) {
        this.turnRoundNumber = turnRoundNumber;
    }

    public UserModel getSpeakingUserModel() {
        return speakingUserModel;
    }

    public void setSpeakingUserModel(UserModel speakingUserModel) {
        this.speakingUserModel = speakingUserModel;
    }

    public List<UserModel> getWaitKeyPutMemberList() {
        return waitKeyPutMemberList;
    }

    public void setWaitKeyPutMemberList(List<UserModel> waitKeyPutMemberList) {
        this.waitKeyPutMemberList = waitKeyPutMemberList;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public boolean isGameStart() {
        return gameStart;
    }

    public void setGameStart(boolean gameStart) {
        this.gameStart = gameStart;
    }

    public int getLimitNumber() {
        return limitNumber;
    }


    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public List<UserModel> getRoomUserList() {
        return roomUserList;
    }

    public void setRoomUserList(List<UserModel> roomUserList) {
        this.roomUserList = roomUserList;
    }

}
