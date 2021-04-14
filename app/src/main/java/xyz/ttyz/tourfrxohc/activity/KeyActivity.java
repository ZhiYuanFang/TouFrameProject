package xyz.ttyz.tourfrxohc.activity;

import xyz.ttyz.toubasemvvm.ui.BaseTouActivity;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityKeyBinding;

public class KeyActivity extends BaseTouActivity<ActivityKeyBinding> {

    public static void show(long roomId){}

    @Override
    protected int initLayoutId() {
        return R.layout.activity_key;
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
