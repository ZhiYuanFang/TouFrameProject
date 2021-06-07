package xyz.ttyz.tourfrxohc.models;

public class ActionType {
    public static final int home_matching = 21;//房间正在匹配

    public static final int memberComeIn = 0;//成员进入
    public static final int memberComeOut = 1;//成员离开
    public static final int memberRoleTypeConfirm = 3;//成员角色分配完成
    public static final int home_come_key = 4;//房间进入钥匙房
    public static final int game_end = 10;//房间开始投票选出死亡人环节结束

    public static final int key_puting = 11;//钥匙房的人正在操作
    public static final int user_paddle_warn = 12;//用户划水并警告
    public static final int user_paddle_dead = 13;//用户划水并宣布死亡

    public static final int speak_ing = 14;//用户正在发言
    public static final int speak_end = 17;//用户结束发言
    public static final int speak_wait = 18;//等待用户抢麦

    public static final int vote_ing = 15;//正在投票

    public static final int OneTurnEndAndPatchArm = 16;//一轮结束并分配武器
}
