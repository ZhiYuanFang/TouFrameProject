package xyz.ttyz.tourfrxohc.activity;

import android.content.Intent;

import xyz.ttyz.mylibrary.method.RxOHCUtils;
import xyz.ttyz.mylibrary.protect.SharedPreferenceUtil;
import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.toubasemvvm.utils.ToastUtil;
import xyz.ttyz.toubasemvvm.vm.ToolBarViewModel;
import xyz.ttyz.tourfrxohc.BaseApplication;
import xyz.ttyz.tourfrxohc.MainActivity;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityGoodsDetailBinding;
import xyz.ttyz.tourfrxohc.databinding.ActivityGoodsDetailOperatorBinding;
import xyz.ttyz.tourfrxohc.http.BaseSubscriber;
import xyz.ttyz.tourfrxohc.models.GoodsModel;

/**
 * @author 投投
 * @date 2023/10/18
 * @email 343315792@qq.com
 */
public class GoodsDetailOperatorActivity extends BaseActivity<ActivityGoodsDetailOperatorBinding>{
    public static void show(long id){
       Intent intent = new Intent(ActivityManager.getInstance(), GoodsDetailOperatorActivity.class);
       intent.putExtra("id", id);
       ActivityManager.getInstance().startActivity(intent);
    }
    ToolBarViewModel toolBarViewModel;
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
        mBinding.setContext(this);
        toolBarViewModel = new ToolBarViewModel.Builder()
                .title("货品详情")
                .build();
        mBinding.setToolBarViewModel(toolBarViewModel);
    }

    @Override
    protected void initServer() {
        new RxOHCUtils<>(this).executeApi(BaseApplication.apiService.getGoodsDetail(), new BaseSubscriber<GoodsModel>(this) {
            @Override
            public void success(GoodsModel data) {
                mBinding.setGoodsModel(data);
            }

        });
    }
}
