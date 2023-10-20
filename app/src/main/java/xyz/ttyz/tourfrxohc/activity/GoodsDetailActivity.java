package xyz.ttyz.tourfrxohc.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.Nullable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xyz.ttyz.mylibrary.method.RetrofitUtils;
import xyz.ttyz.mylibrary.method.RxOHCUtils;
import xyz.ttyz.mylibrary.protect.SharedPreferenceUtil;
import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.utils.NormalImageEngine;
import xyz.ttyz.toubasemvvm.utils.StringUtil;
import xyz.ttyz.toubasemvvm.utils.ToastUtil;
import xyz.ttyz.toubasemvvm.utils.TouUtils;
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
import xyz.ttyz.tourfrxohc.utils.ImageUploader;

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
        return new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
        };
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
    public ObservableInt hasAddNumber = new ObservableInt(0);
    public ObservableInt maxAddNumber = new ObservableInt(5);

    public OnClickAdapter.onClickCommand clickUpImg = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            //  上传照片 选择图
            PictureSelector.create(ActivityManager.getInstance())
                    .openGallery(PictureMimeType.ofImage())
                    .imageEngine(new NormalImageEngine())
                    .theme(R.style.picture_custom_style)
                    .maxSelectNum(maxAddNumber.get() - hasAddNumber.get())
                    .minSelectNum(1)
                    .isPreviewImage(true)
                    .isEnablePreviewAudio(true)
                    .isCamera(false)
                    .circleDimmedLayer(true)
                    .showCropFrame(false)
                    .showCropGrid(false)
                    .isCompress(true)
                    .withAspectRatio(1, 1)
                    .minimumCompressSize(100)
                    .freeStyleCropEnabled(true)
                    .rotateEnabled(false)
                    .forResult(PictureConfig.CHOOSE_REQUEST);
        }
    };
    public ArrayList<String> selectList = new ArrayList<>();
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == RESULT_OK && requestCode == PictureConfig.CHOOSE_REQUEST) {
            ArrayList<String> selectPics = new ArrayList<>();
            TouUtils.filterResSelectResult(data, selectPics);

            for (String filePath : selectPics) {
                ImageUploader.upload(GoodsDetailActivity.this, filePath, new ImageUploader.Callback() {
                    @Override
                    public void success(String qiniuPath) {
                        selectList.add(qiniuPath);
//                        mBinding.bgaNine.setData(selectList);
                    }

                    @Override
                    public void fail(String msg) {
                        ToastUtil.showToast(msg);
                    }
                });
            }
        }

    }
    public OnClickAdapter.onClickCommand clickSave = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            // 入库
            Map map = new HashMap();
            map.put("barcodeNo", goodsModel.getBarcodeNo());
            map.put("goodsActualNo", goodsModel.getGoodsActualNo());
            map.put("quantity", goodsModel.getQuantity());
            map.put("remark", goodsModel.getRemark());
            map.put("goodsPriceMin", StringUtil.convertStr2Long(goodsModel.getPriceMinStr()));
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
