package xyz.ttyz.tourfrxohc.models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import xyz.ttyz.tourfrxohc.BR;
import xyz.ttyz.tourfrxohc.models.game.ArmModel;
import xyz.ttyz.tourfrxohc.models.game.PutKeyModel;
import xyz.ttyz.tourfrxohc.models.game.VoteModel;


public class UserModel extends BaseObservable implements Serializable {
    //基础信息
    private long id;
    private String avatar;//头像
    private String phone;//手机号
    private String nickname;//昵称
    private String password;//密码
    private int score;//积分
    int level;
    boolean isSaveType;
    boolean isConfirmGame;
    boolean isConfirmRoleType;
    boolean thisTurnHasPutedKey;
    //房间内信息
    int identity;//身份 0守护者 1进攻者
    HashMap<Integer, PutKeyModel> putKeyMap;//投放钥匙累计 第Integer轮，投票PutKeyModel, eg. 第2轮划水没投票，则不存在KEY = 2
    List<ArmModel> armModels;//拥有的武器
    HashMap<Integer, VoteModel> voteMap;//投票累计， 第Integer次投票

    boolean isVote;//是否投票
    boolean isInHome;//是否在房间内,用户中途离开
    boolean hasComeInKeyRoom;//是否已经进入过钥匙房间
    boolean isDead;//是否死亡

    int thisTurnSpeakTimes;//每个人 一轮里面 最多可以发三次言论

    int putNumber;//动作对象 1，2，3

    int paddle;//划水次数，超过两次就宣布死亡
    int speakTime = 60;//初始60秒

    long voteUserId;//投票对象的id
    int votedNumber;//被投票数量

    boolean wantSpeak;//希望在本轮发言

    List<ArmModel> armModelList = new ArrayList<ArmModel>();

    //region local
    boolean isSpeaking;
    boolean isVoted;//他是否被我投票
    boolean canBeVoted;//是否可以被投票

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isSaveType() {
        return isSaveType;
    }

    public void setSaveType(boolean saveType) {
        isSaveType = saveType;
    }

    public boolean isConfirmGame() {
        return isConfirmGame;
    }

    public void setConfirmGame(boolean confirmGame) {
        isConfirmGame = confirmGame;
    }

    public boolean isConfirmRoleType() {
        return isConfirmRoleType;
    }

    public void setConfirmRoleType(boolean confirmRoleType) {
        isConfirmRoleType = confirmRoleType;
    }

    public boolean isThisTurnHasPutedKey() {
        return thisTurnHasPutedKey;
    }

    public void setThisTurnHasPutedKey(boolean thisTurnHasPutedKey) {
        this.thisTurnHasPutedKey = thisTurnHasPutedKey;
    }

    public boolean isVote() {
        return isVote;
    }

    public void setVote(boolean vote) {
        isVote = vote;
    }

    public boolean isInHome() {
        return isInHome;
    }

    public void setInHome(boolean inHome) {
        isInHome = inHome;
    }

    public boolean isHasComeInKeyRoom() {
        return hasComeInKeyRoom;
    }

    public void setHasComeInKeyRoom(boolean hasComeInKeyRoom) {
        this.hasComeInKeyRoom = hasComeInKeyRoom;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public int getThisTurnSpeakTimes() {
        return thisTurnSpeakTimes;
    }

    public void setThisTurnSpeakTimes(int thisTurnSpeakTimes) {
        this.thisTurnSpeakTimes = thisTurnSpeakTimes;
    }

    public int getPutNumber() {
        return putNumber;
    }

    public void setPutNumber(int putNumber) {
        this.putNumber = putNumber;
    }

    public int getPaddle() {
        return paddle;
    }

    public void setPaddle(int paddle) {
        this.paddle = paddle;
    }

    public int getSpeakTime() {
        return speakTime;
    }

    public void setSpeakTime(int speakTime) {
        this.speakTime = speakTime;
    }

    public long getVoteUserId() {
        return voteUserId;
    }

    public void setVoteUserId(long voteUserId) {
        this.voteUserId = voteUserId;
    }

    public int getVotedNumber() {
        return votedNumber;
    }

    public void setVotedNumber(int votedNumber) {
        this.votedNumber = votedNumber;
    }

    public boolean isWantSpeak() {
        return wantSpeak;
    }

    public void setWantSpeak(boolean wantSpeak) {
        this.wantSpeak = wantSpeak;
    }

    public List<ArmModel> getArmModelList() {
        return armModelList;
    }

    public void setArmModelList(List<ArmModel> armModelList) {
        this.armModelList = armModelList;
    }

    public boolean isSpeaking() {
        return isSpeaking;
    }

    public void setSpeaking(boolean speaking) {
        isSpeaking = speaking;
    }

    public boolean isVoted() {
        return isVoted;
    }

    public void setVoted(boolean voted) {
        isVoted = voted;
    }

    public boolean isCanBeVoted() {
        return canBeVoted;
    }

    public void setCanBeVoted(boolean canBeVoted) {
        this.canBeVoted = canBeVoted;
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
