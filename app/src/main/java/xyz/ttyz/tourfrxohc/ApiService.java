package xyz.ttyz.tourfrxohc;


import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import xyz.ttyz.mylibrary.method.BaseModule;
import xyz.ttyz.tourfrxohc.models.GoodsModel;
import xyz.ttyz.tourfrxohc.models.MainModel;
import xyz.ttyz.tourfrxohc.models.UserModel;

/**
 * Created by tou on 2019/5/20.
 *
 */

public interface ApiService {
    @POST("/warehouse/login/login")
    Observable<BaseModule<UserModel>> login(@QueryMap Map<String, Object> map);
    @POST("/warehouse/login/logout")
    Observable<BaseModule<UserModel>> logout();


    @POST("/warehouse/goods/pda/page")
    Observable<BaseModule<GoodsModel>> goodsList(@Body RequestBody data);

    @GET("/warehouse/goods/pda/detail/{warehouseGoodsId}")
    Observable<BaseModule<MainModel>> getGoodsDetail();


//    @Headers("Content-type:text/x-plain-rsa-json")
//    @POST("api/Account/phoneCode")
//    Observable<BaseModule<PhoneCodeModule>> phoneCode(@Body RequestBody data);
//
//    @Headers("Content-type:text/x-plain-rsa-json")
//    @POST("api/Account/codeLogin")
//    Observable<BaseModule<LoginModule>> codeLogin(@Body RequestBody data);
//
//    @Headers("Content-type:text/x-plain-aes-json")
//    @POST("api/Account/userInfo")
//    Observable<BaseModule<LoginModule>> userInfo(@Body RequestBody data);
//
//    @POST("ecmobile/test.php")
//    Observable<BaseModule> test(@Body String data);
}
