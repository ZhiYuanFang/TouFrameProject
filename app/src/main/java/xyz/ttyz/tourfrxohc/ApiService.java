package xyz.ttyz.tourfrxohc;


import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import xyz.ttyz.mylibrary.method.BaseModule;
import xyz.ttyz.mylibrary.method.RecordsModule;
import xyz.ttyz.tourfrxohc.models.GoodsModel;
import xyz.ttyz.tourfrxohc.models.MainModel;
import xyz.ttyz.tourfrxohc.models.PantRecordModel;
import xyz.ttyz.tourfrxohc.models.StatisticsModel;
import xyz.ttyz.tourfrxohc.models.TokenResponse;
import xyz.ttyz.tourfrxohc.models.UserModel;
import xyz.ttyz.tourfrxohc.models.WareHouseChildModel;
import xyz.ttyz.tourfrxohc.models.WareHouseModel;

/**
 * Created by tou on 2019/5/20.
 *
 */

public interface ApiService {
    @POST("/warehouse/login/login")
    Observable<BaseModule<Boolean>> login(@Query("username") String username, @Query("password") String password);
    @POST("/warehouse/login/logout")
    Observable<BaseModule<Boolean>> logout();


    @POST("/warehouse/goods/pda/page")
    Observable<BaseModule<RecordsModule<List<GoodsModel>>>> goodsList(@Body RequestBody data);//分页查询仓库货品

    @POST("/warehouse/stocktake/pda/pageCurrentDetails")
    Observable<BaseModule<RecordsModule<List<GoodsModel>>>> pageCurrentDetails(@Body RequestBody data);//分页查询待盘点

    @POST("/warehouse/stocktake/pda/pageDetails")
    Observable<BaseModule<RecordsModule<List<GoodsModel>>>> pageDetails(@Body RequestBody data);//分页查询某盘点任务的明细

    @POST("/warehouse/stocktake/pda/pageDone")
    Observable<BaseModule<RecordsModule<List<PantRecordModel>>>> pageDone(@Body RequestBody data);//分页查询已完成的盘点任务

    @POST("/warehouse/stocktake/pda/stocktaking")
    Observable<BaseModule<Boolean>> stocktaking(@Body RequestBody data);//盘点

    @POST("/warehouse/goods/pda/exiting")
    Observable<BaseModule<Boolean>> goodsOut(@Body RequestBody data);//出库货品
    @POST("/warehouse/goods/pda/stockedStatistics")
    Observable<BaseModule<StatisticsModel>> stockedStatistics(@Body RequestBody data);//首屏的在库货品统计信息
    @POST("/warehouse/goods/pda/update")
    Observable<BaseModule<Boolean>> updateGoods(@Body RequestBody data);//更新仓库货品
    @POST("/warehouse/goods/pda/saveAndEntering")
    Observable<BaseModule<Boolean>> saveAndEntering(@Body RequestBody data);//录入仓库货品并入库


    @GET("/warehouse/goods/pda/detail/{warehouseGoodsId}")
    Observable<BaseModule<GoodsModel>> getGoodsDetail(@Path("warehouseGoodsId") long warehouseGoodsId);
    @GET("/warehouse/manager/list")
    Observable<BaseModule<List<WareHouseModel>>> getWarehouseList();//查询仓库列表

    @GET("/warehouse/manager/area/list/{warehouseId}")
    Observable<BaseModule<List<WareHouseChildModel>>> getWarehouseChildList(@Path("warehouseId") long warehouseId);//查询仓库分区列表


    /**
     * 获取七牛存储凭证，非私有
     * https://b.maihaoche.com/
     */
    @GET("/common/upload/token")
    Observable<BaseModule<String>> getToken();
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
