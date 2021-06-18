package xyz.ttyz.tourfrxohc.models.game;


import java.io.Serializable;
import java.util.List;

import xyz.ttyz.tourfrxohc.models.UserModel;

public class HomeModel implements Serializable{
    int limitNumber;//房间最多的人数
    long roomId;//房间id
    List<UserModel> roomUserList;//房间人员
    UserModel putKeyUser;//投放钥匙人员, 根据status判断该人员是刚刚投放的，还是即将投放的
    int status;//阶段

    List<UserModel> pkMemberList;//pk人员
    UserModel speakingUserModel;//指定发言人员
    int turnRoundNumber;//轮次 1，2，3
    List<Integer> keyStatusList;//钥匙状态
    boolean saveWin;//守护者是否胜利

    /**
     * {@link ActionType}
     */
    int actionType;//动作类型

    public boolean isSaveWin() {
        return saveWin;
    }

    public List<UserModel> getPkMemberList() {
        return pkMemberList;
    }

    public void setPkMemberList(List<UserModel> pkMemberList) {
        this.pkMemberList = pkMemberList;
    }

    public List<Integer> getKeyStatusList() {
        return keyStatusList;
    }

    public void setKeyStatusList(List<Integer> keyStatusList) {
        this.keyStatusList = keyStatusList;
    }

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
