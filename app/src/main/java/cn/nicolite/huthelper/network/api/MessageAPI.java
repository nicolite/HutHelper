package cn.nicolite.huthelper.network.api;

import cn.nicolite.huthelper.model.bean.Valid;
import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * 私信API
 * Created by nicolite on 17-10-25.
 */

public interface MessageAPI {
    /**
     * 检查是否在其它设备登录
     *
     * @return {"code":true}
     */
    @GET("master/staticAPI/isTokenValid.json")
    Observable<Valid> isValid();

}
