package xyz.ttyz.tourfrxohc.utils;

import xyz.ttyz.mylibrary.method.BaseModule;
import xyz.ttyz.mylibrary.method.ProgressUtil;
import xyz.ttyz.mylibrary.method.RxOHCUtils;
import xyz.ttyz.mylibrary.socket.SocketUtils;
import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.tourfrxohc.BaseApplication;
import xyz.ttyz.tourfrxohc.MainActivity;
import xyz.ttyz.tourfrxohc.activity.GameActivity;
import xyz.ttyz.tourfrxohc.dialog.WaitDialogFragment;
import xyz.ttyz.tourfrxohc.http.BaseSubscriber;
import xyz.ttyz.tourfrxohc.models.game.HomeModel;

public class HomeUtils {
    public static void joinHome(){
        ProgressUtil.showCircleProgress(xyz.ttyz.mylibrary.method.ActivityManager.getInstance(), "正在匹配中...");
        new RxOHCUtils<HomeModel>(ActivityManager.getInstance()).executeApi(BaseApplication.apiService.startRandomGame(UserUtils.getCurUserModel().getId()), new BaseSubscriber<HomeModel>(ActivityManager.getInstance()) {
            @Override
            public void success(HomeModel data) {
                SocketUtils.closeMinaReceiver(ActivityManager.getInstance().getApplication());
                //为了让socket附带房间信息
                SocketUtils.openMinaReceiver(ActivityManager.getInstance().getApplication(), new SocketUtils.SocketDelegate() {
                    @Override
                    public void connectSuccess() {
                        ActivityManager.getInstance().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ProgressUtil.missCircleProgress();
                                if(data.getRoomUserList().size() == data.getLimitNumber()){
                                    //直接进入房间
                                    GameActivity.show(data.getRoomId());
                                } else {
                                    WaitDialogFragment waitDialogFragment = WaitDialogFragment.getInstance(data.getRoomId());
                                    waitDialogFragment.refreshList(data.getRoomUserList());
                                    try {
                                        waitDialogFragment.show(ActivityManager.getInstance().getSupportFragmentManager());
                                    } catch (Exception e) {
                                        System.out.println(e.getMessage());
                                    }

                                }
                            }
                        });

                    }

                    @Override
                    public long roomId() {
                        return data.getRoomId();
                    }

                    @Override
                    public long userId() {
                        return UserUtils.getCurUserModel().getId();
                    }
                });
            }

            @Override
            protected boolean autoCloseCircleProgress() {
                return false;
            }

            @Override
            public String initCacheKey() {
                return null;
            }

            @Override
            protected void fail(BaseModule<HomeModel> baseModule) {
                super.fail(baseModule);
                ProgressUtil.missCircleProgress();
            }
        }.notShowProgress());
    }
}
