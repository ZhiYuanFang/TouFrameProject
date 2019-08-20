package xyz.ttyz.tourfrxohc;


import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.POST;

/**
 * Created by tou on 2019/5/20.
 *
 */

public interface ApiService {
    @POST("goods/new_index")
    Observable<BaseModule<List<EngineerModule>>> indexNormalEngineering();
}
