package cn.nicolite.huthelper.network.api;

import cn.nicolite.huthelper.model.bean.HttpResult;
import cn.nicolite.huthelper.model.bean.User;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * 登录API接口
 * Created by nicolite on 17-10-17.
 */

public interface LoginAPI {
    /**
     * 登录，登录成功将会返回用户信息
     *
     * @return
     */
    @GET("master/staticAPI/login.json")
    Observable<HttpResult<User>> login();

    @GET("https://api.tutuweb.cn/api/v1/get/login/{num}/{pass}")
    Observable<HttpResult<User>> login(@Path("num") String num, @Path("pass") String password);

}
