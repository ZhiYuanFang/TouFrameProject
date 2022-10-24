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
    @GET("aspx/make_image.aspx") //获取登录失败次数
    Observable<BaseModule> getSms();
    @POST("lg/GetUserLoginError") //获取登录失败次数
    Observable<BaseModule<Integer>> getUserLoginError(@Query("uservalue")String uservalue);
    @POST("lg/UserLoginValidate")
    Observable<BaseModule<UserModel>> login(@Query("uservalue")String uservalue, @Query("psw")String psw, @Query("Validate")String sms);
    @POST("lg/UserLogout")
    Observable<BaseModule> loginOut(@Query("LoginID")String LoginID);
    @POST("mj/searchTicket")//查票
    Observable<BaseModule<Integer>> searchTicket(@Query("checkNo")String checkNo,//条码/身份证
                                                      @Query("checkType")int checkType,//检票类型 1:条码 2:身份证 3:IC 卡
                                                      @Query("checkName")String checkName,//checkType为2时必填
                                                      @Query("doorID")String doorID,//通道号
                                                      @Query("isFace")boolean isFace,//	是否是人脸刷票 T:是 F:否
                                                      @Query("LoginID")String LoginID //登录接口返回
    );
    @POST("mj/checkTicket")//检票
    Observable<BaseModule<Integer>> checkTicket(@Query("checkNo")String checkNo,//条码/身份证
                                                      @Query("checkType")int checkType,//检票类型 1:条码 2:身份证 3:IC 卡
                                                      @Query("doorID")String doorID,//通道号
                                                      @Query("isFace")boolean isFace,//	是否是人脸刷票 T:是 F:否
                                                     @Query("id")String id,//检票类型识别号 查票接口调用
                                                     @Query("extend")String communicationId,//通信 ID 检票接口
                                                      @Query("LoginID")String LoginID //登录接口返回
    );
}
