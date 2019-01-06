package cn.nicolite.huthelper.network.api;

import java.util.List;

import cn.nicolite.huthelper.model.bean.HttpPageResult;
import cn.nicolite.huthelper.model.bean.HttpResult;
import cn.nicolite.huthelper.model.bean.Say;
import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * 说说API
 * Created by nicolite on 17-10-13.
 */

public interface SayAPI {
    /**
     * 发布说说
     *
     * @return
     */
    @GET("opensource/staticAPI/createSay.json")
    Observable<HttpResult<String>> createSay();


    /**
     * 发布评论
     *
     * @return
     */
    @GET("opensource/staticAPI/createComment.json")
    Observable<HttpResult<String>> createComment();

    /**
     * 获取指定userId的说说
     *
     * @return
     */
    @GET("opensource/staticAPI/getSay.json")
    Observable<HttpPageResult<List<Say>>> getSayListByuserId();

    /**
     * 获取所有说说
     *
     * @return
     */
    @GET("opensource/staticAPI/getSay.json")
    Observable<HttpPageResult<List<Say>>> getSayList();


    /**
     * 说说点赞
     *
     * @return
     */
    @GET("opensource/staticAPI/likeSay.json")
    Observable<HttpResult<String>> likeSay();

    /**
     * 删除说说
     *
     * @return
     */
    @GET("opensource/staticAPI/likeSay.json")
    Observable<HttpResult<String>> deleteSay();

    /**
     * 获取自己点赞的说说
     *
     * @return
     */
    @GET("opensource/staticAPI/getLikeSay.json")
    Observable<HttpResult<List<String>>> getLikedSay();

}
