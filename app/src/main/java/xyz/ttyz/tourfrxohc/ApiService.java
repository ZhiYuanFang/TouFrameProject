package xyz.ttyz.tourfrxohc;


import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import xyz.ttyz.mylibrary.method.BaseModule;
import xyz.ttyz.tourfrxohc.models.CarModel;
import xyz.ttyz.tourfrxohc.models.MainModel;
import xyz.ttyz.tourfrxohc.models.UserModel;

/**
 * Created by tou on 2019/5/20.
 *
 */

public interface ApiService {
    //故障柜同步接口
    @POST("/newKeyCabinet/synFaultCabinet.json")
    Observable<BaseModule<Object>> synFaultCabinet(@Body RequestBody data);
    //钥匙柜入柜前状态回退接口
    @POST("/newKeyCabinet/cancelBeforeInterBoxKey.json")
    Observable<BaseModule<Object>> cancelBeforeInterBoxKey(@Body RequestBody data);
    //钥匙出柜前状态回退接口
    @POST("/newKeyCabinet/cancelBeforeOutBoxKey.json")
    Observable<BaseModule<Object>> cancelBeforeOutBoxKey(@Body RequestBody data);
    //钥匙出柜前调用接口
    @POST("/newKeyCabinet/applyTakeOutBoxKey.json")
    Observable<BaseModule<Object>> applyTakeOutBoxKey(@Body RequestBody data);
    //钥匙柜出柜关闭柜门接口
    @POST("/newKeyCabinet/afterTakeOutBoxKey.json")
    Observable<BaseModule<Object>> afterTakeOutBoxKey(@Body RequestBody data);
    //钥匙输入入柜校验码弹开柜门接口
    @POST("/newKeyCabinet/beforeInterBoxKey.json")
    Observable<BaseModule<Object>> beforeInterBoxKey(@Body RequestBody data);
    //钥匙入柜后关闭柜门调用接口
    @POST("/newKeyCabinet/afterInterBoxKey.json")
    Observable<BaseModule<Object>> afterInterBoxKey(@Body RequestBody data);
    @POST("/newKeyCabinet/initEmergencyPassword.json")
    Observable<BaseModule<Object>> initEmergencyPassword(@Body RequestBody data);
    @POST("/newKeyCabinet/verifyCabinetInit.json")
    Observable<BaseModule<Object>> verifyCabinetInit(@Body RequestBody data);
    //根据入柜校验码获取待入柜车辆信息
    @POST("/newKeyCabinet/getInterBoxCarInfo.json")
    Observable<BaseModule<CarModel>> getInterBoxCarInfo(@Body RequestBody data);
    //获取可出柜的车辆信息列表
    @POST("/newKeyCabinet/canOutBoxKeyList.json")
    Observable<BaseModule<List<CarModel>>> canOutBoxKeyList(@Body RequestBody data);

//    @GET("msr/v1/app/personal/index")
//    Observable<BaseModule<MainModel>> getHistory(@QueryMap Map<String, Object> map);


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
