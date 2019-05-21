package xyz.ttyz.tourfrxohc;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import xyz.ttyz.tourfrxohc.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.popActivity(this);
        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        MainViewModel mainViewModel = new MainViewModel(this);
        activityMainBinding.setVariable(BR.viewModel, mainViewModel);
    }
}
