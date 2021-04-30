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
import xyz.ttyz.tourfrxohc.models.UserModel;
import xyz.ttyz.tourfrxohc.models.game.HomeModel;

/**
 * Created by tou on 2019/5/20.
 */

public interface ApiService {
    @POST("user/register")
    Observable<BaseModule> register(@Query("phone") String phone, @Query("password") String password, @Query("nickname") String nickname);

    @POST("user/login")
    Observable<BaseModule<UserModel>> login(@Query("phone") String phone, @Query("password") String password);

    @POST("game/join")
    Observable<BaseModule<HomeModel>> join(@Query("id") long id);

    @POST("game/leave")
    Observable<BaseModule> leave(@Query("roomId") long roomId, @Query("userId") long userId);

    @GET("game/roomInfo")
    Observable<BaseModule<HomeModel>> roomInfo(@Query("roomId") long roomId);

    @POST("game/confirmStartGame")
    Observable<BaseModule<Object>> confirmStartGame(@Query("roomId") long roomId, @Query("userId") long userId);

    @POST("game/confirmRoleType")
    Observable<BaseModule<Object>> confirmRoleType(@Query("roomId") long roomId, @Query("userId") long userId);
    @POST("game/confirmPutKey")
    Observable<BaseModule<UserModel>> confirmPutKey(@Query("roomId") long roomId, @Query("userId") long userId, @Query("number") int number, @Query("put") boolean put);

    @POST("game/voteEnd")
    Observable<BaseModule<Object>> voteEnd(@Query("roomId") long roomId, @Query("userId") long userId, @Query("voteUserId") long voteUserId);
    @POST("game/speakEnd")
    Observable<BaseModule<Object>> speakEnd(@Query("roomId") long roomId, @Query("userId") long userId);

    @POST("game/exit")
    Observable<BaseModule<Object>> exit(@Query("roomId") long roomId, @Query("userId") long userId);
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
