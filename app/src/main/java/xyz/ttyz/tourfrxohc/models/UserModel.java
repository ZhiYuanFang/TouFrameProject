package xyz.ttyz.tourfrxohc.models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.io.Serializable;
import java.util.Objects;

import xyz.ttyz.tourfrxohc.BR;

public class UserModel extends BaseObservable implements Serializable {
    private long id;
    private String avatar;
    private String phone;
    private String nickname;
    private String password;

    private int level;//等级

    /**
     * {@link RoleType}
     */
    int roleType;
    boolean isConfirmGame;//是否确认游戏
    boolean isConfirmRoleType;//是否确认角色
    boolean isConfirmPutKey;//是否确认投放钥匙
    boolean isVote;//是否投票
    boolean oneTurnHasSpoken;//当轮已经发过言了
    boolean isInHome;//是否在房间内
    boolean hasComeInKeyRoom;//是否已经进入过钥匙房间
    boolean isDoubtMember;//是否为本轮怀疑对象
    boolean isDead;//是否死亡

    long voteUserId;//投票对象的id
    int votedNumber;//被投票数量

    boolean isSpeaking;

    @Bindable
    public boolean isSpeaking() {
        return isSpeaking;
    }

    public void setSpeaking(boolean speaking) {
        isSpeaking = speaking;
        notifyPropertyChanged(BR.speaking);
    }

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

    public int getRoleType() {
        return roleType;
    }

    public void setRoleType(int roleType) {
        this.roleType = roleType;
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

    public boolean isConfirmPutKey() {
        return isConfirmPutKey;
    }

    public void setConfirmPutKey(boolean confirmPutKey) {
        isConfirmPutKey = confirmPutKey;
    }

    public boolean isVote() {
        return isVote;
    }

    public void setVote(boolean vote) {
        isVote = vote;
    }

    public boolean isOneTurnHasSpoken() {
        return oneTurnHasSpoken;
    }

    public void setOneTurnHasSpoken(boolean oneTurnHasSpoken) {
        this.oneTurnHasSpoken = oneTurnHasSpoken;
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

    public boolean isDoubtMember() {
        return isDoubtMember;
    }

    public void setDoubtMember(boolean doubtMember) {
        isDoubtMember = doubtMember;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
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
