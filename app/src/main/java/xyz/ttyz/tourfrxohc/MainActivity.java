package xyz.ttyz.tourfrxohc;

import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.os.Bundle;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import io.reactivex.functions.Consumer;
import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.toubasemvvm.ui.BaseTouActivity;
import xyz.ttyz.tourfrxohc.databinding.ActivityMainBinding;


public class MainActivity extends BaseTouActivity<ActivityMainBinding> {

    @Override
    protected int initLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected String[] initPermission() {
        return new String[]{Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};
    }

    @Override
    protected void initData() {
        mBinding.setViewModel( new MainViewModel(this, this));
    }

    @Override
    protected void initServer() {

    }
}


