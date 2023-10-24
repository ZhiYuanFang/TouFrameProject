package xyz.ttyz.tourfrxohc.activity;

import android.content.Intent;

import androidx.databinding.ObservableField;

import xyz.ttyz.mylibrary.method.ActivityManager;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.ActivityWebBinding;

/**
 * @author 投投
 * @date 2023/10/24
 * @email 343315792@qq.com
 */
public class WebActivity extends BaseActivity<ActivityWebBinding>{

    public static void show(){
        Intent intent = new Intent(ActivityManager.getInstance(), WebActivity.class);
        ActivityManager.getInstance().startActivity(intent);
    }
    public ObservableField<String> url = new ObservableField<>();

    @Override
    protected int initLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    protected String[] initPermission() {
        return new String[0];
    }

    @Override
    protected void initData() {

        mBinding.setContext(this);
    }

    @Override
    protected void initServer() {

    }

    public OnClickAdapter.onClickCommand clickWeb = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            url.set("https://shenzhen.icbc.com.cn/open/ecop/wxbank/openlink/wapcopen.html?appletId=icbc9230534050&appId=wapc&appCode=dgsign&pageId=10001&params=Y2lubz0wMzAyOTAwMDYwMzU3MjkmYXBwQ29kZT0xMTAwMDAwMDAwMDAwMDAxMjE3NCZzZXJpYWxObz02NTM2M2U5YWU0YjA3ZDM5Y2U3NTc5OTU=");
        }
    };
}
