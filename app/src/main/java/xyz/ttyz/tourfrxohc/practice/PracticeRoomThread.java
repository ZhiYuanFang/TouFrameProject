package xyz.ttyz.tourfrxohc.practice;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import xyz.ttyz.tourfrxohc.models.UserModel;
import xyz.ttyz.tourfrxohc.models.game.HomeModel;
import xyz.ttyz.tourfrxohc.utils.DefaultUtils;
import xyz.ttyz.tourfrxohc.utils.UserUtils;

public class PracticeRoomThread {
    HomeModel homeModel;
    Timer timer;
    public PracticeRoomThread() {
        homeModel = new HomeModel();
        //人物分配完成
        initPerson();
        //角色分配完成后，开启线程

    }
    /**
     * 成员初始
     * */
    private void initPerson(){
        List<UserModel> personList = new ArrayList<UserModel>();
        UserModel localUser = UserUtils.getCurUserModel();
        personList.add(localUser);
        for(int i = 1; i < DefaultUtils.roomLimitMaxNumber; i ++){
            UserModel machineUser = new UserModel();
            machineUser.setId(i * 999);
            machineUser.setNickname("mac_" + i);
            personList.add(machineUser);
        }
        homeModel.setRoomUserList(personList);
    }

    /**
     * 角色初始
     * 1/3 为保底角色数量
     * */
    private void initRoleType(){
        int limitNumber =  (int)(homeModel.getLimitNumber()/3);
        //随机选择limitNumber作为保底A角色
        // TODO: 2021/6/18  
        //随机选择保底B角色
        //其余人员随机分配角色
    }

    /*
    * 开启线程
    * */
    public void gameStart(){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

            }
        }, 500);
    }

    //region private



    //endregion

    /*
     * 关闭线程
     * */
    public void gameEnd(){
        if(timer != null){
            timer.cancel();
            timer = null;
        }
    }

}
