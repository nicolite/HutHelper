package cn.nicolite.huthelper.network.api;

import cn.nicolite.huthelper.model.Constants;
import cn.nicolite.huthelper.model.bean.Electric;
import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * 电费API接口
 * Created by nicolite on 17-10-31.
 */

public interface ElectricAPI {
    @GET("master/staticAPI/getPower.json")
    Observable<Electric> getElectric();
}
