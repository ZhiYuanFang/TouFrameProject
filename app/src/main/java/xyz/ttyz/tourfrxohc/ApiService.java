package xyz.ttyz.tourfrxohc;


import java.util.List;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by tou on 2019/5/20.
 *
 */

public interface ApiService {
    @POST("goods/new_index")
    Observable<BaseModule<List<EngineerModule>>> indexNormalEngineering();
}
