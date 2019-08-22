package xyz.ttyz.tourfrxohc;


import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import xyz.ttyz.mylibrary.method.BaseModule;

/**
 * Created by tou on 2019/5/20.
 *
 */

public interface ApiService {
    @POST("goods/new_index")
    Observable<BaseModule<List<EngineerModule>>> indexNormalEngineering();

    @Headers("Content-type:text/x-plain-rsa-json")
    @POST("api/Account/phoneCode")
    Observable<BaseModule<PhoneCodeModule>> phoneCode(@Body RequestBody data);

    @Headers("Content-type:text/x-plain-rsa-json")
    @POST("api/Account/codeLogin")
    Observable<BaseModule<LoginModule>> codeLogin(@Body RequestBody data);

    @Headers("Content-type:text/x-plain-aes-json")
    @POST("api/Account/userInfo")
    Observable<BaseModule<LoginModule>> userInfo(@Body RequestBody data);

    @POST("ecmobile/test.php")
    Observable<BaseModule> test(@Body String data);
}
