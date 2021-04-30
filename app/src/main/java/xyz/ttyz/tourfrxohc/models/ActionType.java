package xyz.ttyz.tourfrxohc.models;

public class ActionType {

    public static final int memberComeIn = 0;//成员进入
    public static final int memberComeOut = 1;//成员离开
    public static final int memberConfirmStartGame = 2;//成员确认开始游戏
    public static final int memberRoleTypeConfirm = 3;//成员角色分配完成
    public static final int home_come_key = 4;//房间进入钥匙房
    public static final int home_normal_speak = 5;//房间正常发言流程
    public static final int home_select_criminal_start = 6;//房间开始投票选出嫌疑人环节
    public static final int home_vote_criminal_end = 9;//房间开始投票选出嫌疑人环节结束
    public static final int game_end = 10;//房间开始投票选出嫌疑人环节结束

    public static final int key_put_end_and_next_speak = 7;//钥匙投放完毕，进入下一个流程-众人发言并指定好发言人员
}
