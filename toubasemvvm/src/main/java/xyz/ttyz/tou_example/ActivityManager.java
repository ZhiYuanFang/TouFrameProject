package xyz.ttyz.tou_example;

import android.app.Activity;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tou on 2019/5/21.
 */

public class ActivityManager {
    private static List<Activity> activityList = new ArrayList<>();

    public static List<Activity> getActivityList() {
        return activityList;
    }

    public static List<Activity> activityTopNumList(int topNum){
        List<Activity> topActivityList = new ArrayList<>();
        if(topNum < activityList.size()){
            for(int i = 0; i < topNum; i ++ ){
                topActivityList.add(activityList.get(activityList.size() - 1 - i));
            }
        } else {
            topActivityList.addAll(activityList);
            topActivityList.remove(0);
        }
        return topActivityList;
    }

    public static void popActivity(Activity activity){
        if(!activityList.contains(activity)){
            activityList.add(activity);
        }
    }

    public static void exitActivity(Activity activity){
        if(activityList.contains(activity)){
            activityList.remove(activity);
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
    }

    public static void popOtherActivity(Class cla){
        if(activityList.size() >= 1){
            List<Activity> exitActivityList = new ArrayList<>();
            for(Activity ac : activityList){
                if(!ac.getClass().getSimpleName().equals(cla.getSimpleName())){
                    exitActivityList.add(ac);
                }
            }
            for(Activity ac : exitActivityList){
                exitActivity(ac);
            }
        }
    }

    public static @NonNull Activity getInstance(){
        if(activityList.size() > 0){
            return activityList.get(activityList.size() - 1);
        } else
            throw new NullPointerException();
    }

    public static boolean isOnlyActivity(Activity activity){
        if(activityList.size() > 0 && activityList.contains(activity)){
            return activityList.indexOf(activity) == 0;
        }
        return false;
    }
}
