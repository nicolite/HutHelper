package cn.nicolite.huthelper.network.api;

import java.util.List;

import cn.nicolite.huthelper.model.bean.HttpResult;
import cn.nicolite.huthelper.model.bean.User;
import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * 用户相关API
 * Created by nicolite on 17-10-17.
 */

public interface UserAPI {
    /**
     * 修改昵称
     *
     * @return
     */
    @GET("master/staticAPI/changeUsername.json")
    Observable<HttpResult> changeUsername();

    /**
     * 修改签名
     *
     * @return
     */
    @GET("master/staticAPI/changeBio.json")
    Observable<HttpResult> changeBio();

    @GET("master/staticAPI/.json")
    Observable<HttpResult<List<User>>> getStudents();

    /**
     * 获取用户信息
     *
     * @return
     */
    @GET("master/staticAPI/getUserInfo.json")
    Observable<HttpResult<User>> getUserInfo();
}
