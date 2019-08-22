package xyz.ttyz.tourfrxohc;

import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.os.Bundle;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import io.reactivex.functions.Consumer;
import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.tourfrxohc.databinding.ActivityMainBinding;


public class MainActivity extends RxAppCompatActivity {
    MainViewModel mainViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.popActivity(this);
        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainViewModel = new MainViewModel(this, this);
        activityMainBinding.setVariable(BR.viewModel, mainViewModel);
        requestRxPermissions();
    }

    private void requestRxPermissions() {
        RxPermissions rxPermissions = new RxPermissions(this);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            rxPermissions.request(Manifest.permission.INTERNET,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) throws Exception {
                            if(aBoolean){
//                                mainViewModel.jiemi(null);
                            }
                        }
                    });
        }
    }
}


