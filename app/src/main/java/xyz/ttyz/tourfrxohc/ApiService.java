package xyz.ttyz.tourfrxohc;


import java.io.File;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import xyz.ttyz.mylibrary.method.BaseModule;
import xyz.ttyz.tourfrxohc.models.MainModel;
import xyz.ttyz.tourfrxohc.models.UserModel;
import xyz.ttyz.tourfrxohc.models.game.HomeModel;
import xyz.ttyz.tourfrxohc.models.game.PutEndModel;

/**
 * Created by tou on 2019/5/20.
 */

public interface ApiService {

    //region game
    /**
     * 加入游戏
     * */
    @POST("game/startRandomGame")
    Observable<BaseModule<HomeModel>> startRandomGame(@Query("roomNumber") int roomNumber, @Query("userId") long userId);

    /**
     * 开始创建好的房间（一群人在一个房间内同时匹配）
     * */
    @POST("game/startCreatedGame")
    Observable<BaseModule<HomeModel>> startCreatedGame(@Query("roomId") long roomId);

    /**
     * 创建房间
     * */
    @POST("game/createRoom")
    Observable<BaseModule<HomeModel>> createRoom(@Query("roomNumber") int roomNumber, @Query("roomId") long roomId);

    /**
     * 加入创建的房间
     * */
    @POST("game/joinRoom")
    Observable<BaseModule<HomeModel>> joinRoom(@Query("roomId") long roomId, @Query("userId") int userId);

    /**
     * 离开创建的房间
     * */
    @POST("game/leaveCreatedRoom")
    Observable<BaseModule<Object>> leaveCreatedRoom(@Query("roomId") long roomId, @Query("userId") long userId);
    /**
     * 离开游戏房间
     * */
    @POST("game/leave")
    Observable<BaseModule<Object>> leave(@Query("roomId") long roomId, @Query("userId") long userId);

    /**
     * 查看房间信息
     * */
    @GET("game/roomInfo")
    Observable<BaseModule<HomeModel>> roomInfo(@Query("roomId") long roomId);

    /**
     * 投放钥匙
     * */
    @POST("game/confirmPutKey")
    Observable<BaseModule<PutEndModel>> confirmPutKey(@Query("roomId") long roomId, @Query("userId") long userId, @Query("number") int number);

    /**
     * 查看钥匙状态
     * */
    @POST("game/lookKeyStatus")
    Observable<BaseModule<List<Integer>>> lookKeyStatus(@Query("roomId") long roomId, @Query("userId") long userId);

    /**
     * 发言的时候，主动结束发言
     * */
    @POST("game/speakEnd")
    Observable<BaseModule<Object>> speakEnd(@Query("roomId") long roomId, @Query("userId") long userId);

    /**
     * 抢麦，我想要发言
     * */
    @POST("game/wantSpeak")
    Observable<BaseModule<Object>> wantSpeak(@Query("roomId") long roomId, @Query("userId") long userId);

    /**
     * 投票的时候，点击了人头 表示本轮投票完成
     * */
    @POST("game/vote")
    Observable<BaseModule<Object>> vote(@Query("roomId") long roomId, @Query("userId") long userId, @Query("voteUserId") long voteUserId);
    //endregion

    /**
     * 上传图片
     * */
    @POST("pic/up")
    @Multipart
    Observable<BaseModule<String>> picUp(@Part MultipartBody.Part  file);

    //region user
    @POST("user/register")
    Observable<BaseModule<Object>> register(@Query("phone") String phone, @Query("password") String password, @Query("nickname") String nickname);

    @POST("user/login")
    Observable<BaseModule<UserModel>> login(@Query("phone") String phone, @Query("password") String password);

    @GET("user/info")
    Observable<BaseModule<UserModel>> info(@Query("id") long id);

    @POST("user/updateInfo")
    Observable<BaseModule<UserModel>> updateNickName(@Query("phone") String phone, @Query("password") String password, @Query("nickname") String nickname);

    @POST("user/updateInfo")
    Observable<BaseModule<UserModel>> updateAvatar(@Query("phone") String phone, @Query("password") String password, @Query("avatar") String avatar);
    //endregion








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
