package xyz.ttyz.tourfrxohc.practice;

import java.util.Timer;
import java.util.TimerTask;

public class PracticeRoomThread {

    Timer timer;
    public PracticeRoomThread() {

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
