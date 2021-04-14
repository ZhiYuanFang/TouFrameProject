package xyz.ttyz.tourfrxohc.models;

import xyz.ttyz.tourfrxohc.models.game.HomeModel;
import xyz.ttyz.tourfrxohc.models.game.VoiceModel;

//房间id
//动作类型actionType：人员变化0，语音变化1，房间变化2
//-人员变化changeUser
//--人员离开changeUser.level = 0
//--人员进来changeUser.level = 1
//
//-语音变化【系统语音，玩家语音】
//--voiceModel
//---voiceModel.sys【voiceBytes】
//---voiceModel.user【id, voiceBytes】
//
//-房间变化roomModel【语音房，钥匙房，结束后的聊嗨房】
//--语音房roomModel.type = 0  userList
//--钥匙房roomModel.type = 1
//--聊嗨房roomModel.type = 2
public class SocketEventModule {

    long roomId;
    int actionType;
    UserModel changeUser;
    VoiceModel voiceModel;
    HomeModel roomModel;

    public long getRoomId() {
        return roomId;
    }

    public int getActionType() {
        return actionType;
    }

    public UserModel getChangeUser() {
        return changeUser;
    }

    public VoiceModel getVoiceModel() {
        return voiceModel;
    }

    public HomeModel getRoomModel() {
        return roomModel;
    }
}
