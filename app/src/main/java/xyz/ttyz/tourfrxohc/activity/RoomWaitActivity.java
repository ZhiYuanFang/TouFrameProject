package xyz.ttyz.tourfrxohc.activity;

import android.content.Intent;

import xyz.ttyz.mylibrary.method.RxOHCUtils;
import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.toubasemvvm.ui.BaseTouActivity;
import xyz.ttyz.tourfrxohc.BaseApplication;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityRoomWaitBinding;
import xyz.ttyz.tourfrxohc.http.BaseSubscriber;
import xyz.ttyz.tourfrxohc.models.game.HomeModel;

/**
 * 房间等待界面
 * */
public class RoomWaitActivity extends BaseTouActivity<ActivityRoomWaitBinding> {
    public static void show(long roomId){
        Intent intent = new Intent(ActivityManager.getInstance(), RoomWaitActivity.class);
        intent.putExtra("roomId", roomId);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        ActivityManager.getInstance().startActivity(intent);
    }

    long roomId;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_room_wait;
    }

    @Override
    protected String[] initPermission() {
        return new String[0];
    }

    @Override
    protected void initData() {
        roomId = getIntent().getLongExtra("roomId", 0);

    }

    @Override
    protected void initServer() {
        //获取房间信息
        new RxOHCUtils<HomeModel>(this).executeApi(BaseApplication.apiService.roomInfo(roomId), new BaseSubscriber<HomeModel>(this) {
            @Override
            public void success(HomeModel data) {
                // TODO: 2021/5/13 绘制等待界面
            }

            @Override
            public String initCacheKey() {
                return null;
            }
        });


    }
}
