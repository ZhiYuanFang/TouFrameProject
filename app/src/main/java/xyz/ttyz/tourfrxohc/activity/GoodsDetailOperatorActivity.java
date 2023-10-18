package xyz.ttyz.tourfrxohc.activity;

import android.content.Intent;

import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityGoodsDetailBinding;

/**
 * @author 投投
 * @date 2023/10/18
 * @email 343315792@qq.com
 */
public class GoodsDetailOperatorActivity extends BaseActivity<ActivityGoodsDetailBinding>{
    public static void show(long id){
       Intent intent = new Intent(ActivityManager.getInstance(), GoodsDetailOperatorActivity.class);
       intent.putExtra("id", id);
       ActivityManager.getInstance().startActivity(intent);
    }
    long id;
    @Override
    protected int initLayoutId() {
        return R.layout.activity_goods_detail_operator;
    }

    @Override
    protected String[] initPermission() {
        return new String[0];
    }

    @Override
    protected void initData() {
        id = getIntent().getLongExtra("id", 0);
    }

    @Override
    protected void initServer() {

    }
}
