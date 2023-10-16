package xyz.ttyz.tourfrxohc.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.Nullable;
import androidx.databinding.ObservableBoolean;

import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.vm.ToolBarViewModel;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityGoodsDetailBinding;
import xyz.ttyz.tourfrxohc.models.GoodsModel;

/**
 * @author 投投
 * @date 2023/10/16
 * @email 343315792@qq.com
 */
public class GoodsDetailActivity extends BaseActivity<ActivityGoodsDetailBinding>{

    public static void show(@Nullable GoodsModel goodsModel, boolean isEdit) {
        Intent intent = new Intent(ActivityManager.getInstance(), GoodsDetailActivity.class);
        intent.putExtra("goodsModel", goodsModel);
        intent.putExtra("isEdit", isEdit);
        ActivityManager.getInstance().startActivity(intent);
    }

    ToolBarViewModel toolBarViewModel;
    public GoodsModel goodsModel;

    public ObservableBoolean isEdit = new ObservableBoolean(false);

    @Override
    protected int initLayoutId() {
        return R.layout.activity_goods_detail;

    }

    @Override
    protected String[] initPermission() {
        return new String[0];
    }

    @Override
    protected void initData() {
        mBinding.setContext(this);
        toolBarViewModel = new ToolBarViewModel.Builder()
                .title(isEdit.get() ? "商品详情" : "扫描结果")
                .build();
        mBinding.setToolBarViewModel(toolBarViewModel);
        goodsModel = (GoodsModel) getIntent().getSerializableExtra("goodsModel");
        isEdit.set((boolean) getIntent().getBooleanExtra("isEdit", false));
        mBinding.setGoodsModel(goodsModel);
    }

    @Override
    protected void initServer() {

    }

    public OnClickAdapter.onClickCommand clickUpImg = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            // TODO: 2023/10/16 上传照片
        }
    };

    public OnClickAdapter.onClickCommand clickSave = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            // 入库
            ComSuccessActivity.show();
            finish();
            System.out.println("件数:" + goodsModel.getNumber());
        }
    };
}
