package xyz.ttyz.tourfrxohc.models;

import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.toubasemvvm.utils.MobileInfoUtil;

public class Hardware {

    int type = 7;
    String uuid = MobileInfoUtil.getUUID(ActivityManager.getInstance());
    String model = MobileInfoUtil.getSystemModel();
    String system = MobileInfoUtil.getSystemVersion();
}
