package cn.nicolite.huthelper.network.api;

import java.util.List;

import cn.nicolite.huthelper.model.bean.CareerTalk;
import cn.nicolite.huthelper.model.bean.CareerTalkItem;
import cn.nicolite.huthelper.model.bean.CareerTalkResult;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 宣讲会API
 * Created by nicolite on 17-11-5.
 */

public interface CareerTalkAPI {

    @GET("http://api.haitou.cc/xjh/list")
    Observable<CareerTalkResult<List<CareerTalk>>> getCareerTalkList(@Query("kind") String kind, @Query("zone") String zone, @Query("page") int page);

    @GET("http://api.haitou.cc/xjh/view")
    Observable<CareerTalkResult<CareerTalkItem>> getCareerTalk(@Query("id") int id);

    @GET("http://api.haitou.cc/xjh/list?zone=cs&key={key}&page={page}")
    Observable<CareerTalkResult<List<CareerTalk>>> searchCareerTalk(@Path("key") String key, @Path("page") int page);

    @GET("http://api.haitou.cc/xjh/list")
    Observable<CareerTalkResult<List<CareerTalk>>> getCareerTalkList(@Query("univ_id") int univId, @Query("kind") String kind, @Query("zone") String zone, @Query("page") int page);

}
