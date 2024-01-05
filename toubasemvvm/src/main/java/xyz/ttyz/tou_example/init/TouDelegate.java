package xyz.ttyz.tou_example.init;

public interface TouDelegate {

    boolean isBindWareHouse();//是否绑定仓库
    boolean isConnectSerial();//是否连接设备

    void gotoBindWareHouseActivity();

    void checkVersion(VersionDelegate versionDelegate);

    String applicationId();//BuildConfig.APPLICATION_ID

    //捕获主线程异常,往往这个时候
    //if (!BuildConfig.DEBUG) {
    //                            CrashReport.postCatchedException(e);//像bugly提交异常，做线上维护
    //                        }
    void cacheMainThrowable(Throwable e);

    interface VersionDelegate {
        void installVersion(String mUpdateUrl, String updateMSG, int versionCode, boolean isForceUpdate);
    }
}
