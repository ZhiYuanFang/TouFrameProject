package xyz.ttyz.tourfrxohc.activity;

import android.content.Intent;

import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.toubasemvvm.ui.BaseTouActivity;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityEndChatBinding;

public class EndChatActivity extends BaseTouActivity<ActivityEndChatBinding> {
    public static void show(long roomId){
        Intent intent = new Intent(ActivityManager.getInstance(), EndChatActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("roomId", roomId);
        ActivityManager.getInstance().startActivity(intent);
    }
    @Override
    protected int initLayoutId() {
        return R.layout.activity_end_chat;
    }

    @Override
    protected String[] initPermission() {
        return new String[0];
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initServer() {

    }
}
