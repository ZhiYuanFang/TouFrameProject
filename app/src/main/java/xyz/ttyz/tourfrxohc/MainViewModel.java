package xyz.ttyz.tourfrxohc;

import android.content.Context;
import android.view.View;

import androidx.databinding.ObservableField;

import com.trello.rxlifecycle2.LifecycleProvider;

import java.util.HashMap;

import xyz.ttyz.mylibrary.method.BaseSubscriber;
import xyz.ttyz.mylibrary.method.RetrofitUtils;
import xyz.ttyz.mylibrary.method.RxOHCUtils;
import xyz.ttyz.mylibrary.protect.EncodeUtils;
import xyz.ttyz.mylibrary.encryption_decryption.AES;
import xyz.ttyz.mylibrary.encryption_decryption.SecureRandomUtil;

public class MainViewModel {
    private static final String TAG = "MainViewModel";
    Context context;
    LifecycleProvider lifecycleProvider;
    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> code = new ObservableField<>();

    public MainViewModel(Context context, LifecycleProvider lifecycleProvider) {
        this.context = context;
        this.lifecycleProvider = lifecycleProvider;
    }

    public void publish(View view) {

        HashMap jsonObject = new HashMap();
        jsonObject.put("phoneNumber", "15306510807");
        jsonObject.put("CountryCode", "+86");
        new RxOHCUtils<>(context).executeApi(BaseApplication.apiService.phoneCode(RetrofitUtils.getRSARequestBody(jsonObject)), new BaseSubscriber<PhoneCodeModule>(lifecycleProvider) {

            @Override
            public void success(PhoneCodeModule data) {
                name.set("验证码请求成功");
            }

            @Override
            public String initCacheKey() {
                return null;
            }

        });
    }
    String random;
    public void login(View view){
        EncodeUtils.AESKey = null;
        random = SecureRandomUtil.getRandom(32);
        HashMap jsonObject = new HashMap();
        jsonObject.put("phoneNumber", "15306510807");
        jsonObject.put("CountryCode", "+86");
        jsonObject.put("phoneCode", code.get());
        jsonObject.put("aesKey", random);
        new RxOHCUtils<>(context).executeApi(BaseApplication.apiService.codeLogin(RetrofitUtils.getRSARequestBody(jsonObject)), new BaseSubscriber<LoginModule>(lifecycleProvider) {

            @Override
            public void success(LoginModule data) {
                DefaultUtils.token = data.token;
                name.set(data.token);
                EncodeUtils.AESKey = random;
            }

            @Override
            public String initCacheKey() {
                return null;
            }

        });
    }

    public void userInfo(View view){
        HashMap hashMap = new HashMap();
        hashMap.put("code", "123");
        new RxOHCUtils<>(context).executeApi(BaseApplication.apiService.userInfo(RetrofitUtils.getAESRequestBody(hashMap)), new BaseSubscriber<LoginModule>(lifecycleProvider) {

            @Override
            public void success(LoginModule data) {
                name.set("用户信息");
            }

            @Override
            public String initCacheKey() {
                return null;
            }

        });
    }

    public void jiami(View view){
        EncodeUtils.AESKey = "36dZju9E6p2040KA1s7dU0475E7463p4";
        HashMap hashMap = new HashMap();
        hashMap.put("code", "123");
        RetrofitUtils.getAESRequestBody(hashMap);
    }

    public void jiemi(View view){
        AES.decryptFromBase64("kU/omXbRtwOVP/fppMqXfhcjkcnXX7gbOZc8OzHd7DanVFJ5CLL+X33YTu8qgLtJg88kAPpCzcKvZsMtdbivnAgIS5UX08W4gK/3oJ6ZINQ=", "4UE3io49516H2flldXm89W1a4S3r98v4");
    }
}
