package xyz.ttyz.tourfrxohc.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.Nullable;
import androidx.databinding.ObservableBoolean;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import xyz.ttyz.mylibrary.method.RetrofitUtils;
import xyz.ttyz.mylibrary.method.RxOHCUtils;
import xyz.ttyz.mylibrary.protect.SharedPreferenceUtil;
import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.vm.ToolBarViewModel;
import xyz.ttyz.tourfrxohc.BaseApplication;
import xyz.ttyz.tourfrxohc.DefaultUtils;
import xyz.ttyz.tourfrxohc.MainActivity;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityGoodsDetailBinding;
import xyz.ttyz.tourfrxohc.event.GoodsOperatorEvent;
import xyz.ttyz.tourfrxohc.http.BaseSubscriber;
import xyz.ttyz.tourfrxohc.models.GoodsModel;
import xyz.ttyz.tourfrxohc.models.LocationModel;

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
            Map map = new HashMap();
            map.put("barcodeNo", goodsModel.getBarcodeNo());
            map.put("goodsActualNo", goodsModel.getGoodsActualNo());
            map.put("quantity", goodsModel.getQuantity());
            map.put("remark", goodsModel.getRemark());
            map.put("goodsPriceMin", goodsModel.getPriceMinStr());
            map.put("warehouseAreaId", DefaultUtils.getLocalLocationModel().warehouseAreaId);
            map.put("warehouseId", DefaultUtils.getLocalLocationModel().warehouseId);
            if(isEdit.get()){
                map.put("id", goodsModel.getId());
                new RxOHCUtils<>(GoodsDetailActivity.this).executeApi(BaseApplication.apiService.updateGoods(RetrofitUtils.getNormalBody(map)), new BaseSubscriber<Boolean>(GoodsDetailActivity.this) {
                    @Override
                    public void success(Boolean data) {
                        EventBus.getDefault().post(new GoodsOperatorEvent());
                        finish();

                    }
                });
            } else {
                new RxOHCUtils<>(GoodsDetailActivity.this).executeApi(BaseApplication.apiService.saveAndEntering(RetrofitUtils.getNormalBody(map)), new BaseSubscriber<Boolean>(GoodsDetailActivity.this) {
                    @Override
                    public void success(Boolean data) {

                        ComSuccessActivity.show();
                        finish();
                    }
                });
            }
        }
    };
}
