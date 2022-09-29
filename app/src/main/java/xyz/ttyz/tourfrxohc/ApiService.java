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
import xyz.ttyz.tourfrxohc.models.MainModel;
import xyz.ttyz.tourfrxohc.models.TicketDetail;
import xyz.ttyz.tourfrxohc.models.UserModel;

/**
 * Created by tou on 2019/5/20.
 *
 */

public interface ApiService {
//    @POST("msu/v1/signin/code")
//    Observable<BaseModule<UserModel>> login(@Body RequestBody data);
//
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
    @POST("lg/UserLoginValidate")
    Observable<BaseModule<UserModel>> login(@Query("uservalue")String uservalue, @Query("psw")String psw);
    @POST("lg/UserLogout")
    Observable<BaseModule> loginOut(@Query("LoginID")String LoginID);
    @POST("mj/searchTicket")//查票
    Observable<BaseModule<TicketDetail>> searchTicket(@Query("checkNo")String checkNo,//条码/身份证
                                                      @Query("checkType")int checkType,//检票类型 1:条码 2:身份证 3:IC 卡
                                                      @Query("checkName")String checkName,//checkType为2时必填
                                                      @Query("doorID")String doorID,//通道号
                                                      @Query("isFace")boolean isFace,//	是否是人脸刷票 T:是 F:否
                                                      @Query("LoginID")String LoginID //登录接口返回
    );
    @POST("mj/checkTicket")//检票
    Observable<BaseModule<TicketDetail>> checkTicket(@Query("checkNo")String checkNo,//条码/身份证
                                                      @Query("checkType")int checkType,//检票类型 1:条码 2:身份证 3:IC 卡
                                                      @Query("doorID")String doorID,//通道号
                                                      @Query("isFace")boolean isFace,//	是否是人脸刷票 T:是 F:否
                                                     @Query("id")String id,//检票类型识别号 查票接口调用
                                                     @Query("communicationId")String communicationId,//通信 ID 检票接口
                                                      @Query("LoginID")String LoginID //登录接口返回
    );
}
