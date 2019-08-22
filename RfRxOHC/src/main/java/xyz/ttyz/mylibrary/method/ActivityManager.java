package xyz.ttyz.mylibrary.method;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tou on 2019/5/21.
 */

public class ActivityManager {
    private static List<Activity> activityList = new ArrayList<>();

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

    public static Activity getInstance(){
        if(activityList.size() > 0){
            return activityList.get(activityList.size() - 1);
        } else return null;
    }
}
