package cn.nicolite.huthelper.network.api;

import java.util.List;

import cn.nicolite.huthelper.model.bean.HttpPageResult;
import cn.nicolite.huthelper.model.bean.HttpResult;
import cn.nicolite.huthelper.model.bean.LostAndFound;
import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * 失误招领API接口
 * Created by nicolite on 17-11-12.
 */

public interface LostAndFoundAPI {
    /**
     * 创建失物招领
     *
     * @return
     */
    @GET("master/staticAPI/createLostAndFound.json")
    Observable<HttpResult<String>> createLostAndFound();

    /**
     * 获取失物招领列表
     *
     * @return
     */
    @GET("master/staticAPI/getLostAndFound.json.json")
    Observable<HttpPageResult<List<LostAndFound>>> getLostAndFoundList();


    /**
     * 根据userId获取失误招领列表
     *
     * @return
     */
    @GET("master/staticAPI/getLostAndFound.json.json")
    Observable<HttpPageResult<List<LostAndFound>>> getLostAndFoundListByUserId();

    /**
     * 搜索失物招领
     *
     * @return
     */
    @GET("master/staticAPI/getLostAndFound.json.json")
    Observable<HttpPageResult<List<LostAndFound>>> searchLostAndFound();

    /**
     * 删除失误招领
     *
     * @return
     */
    @GET("master/staticAPI/deleteLostAndFound.json")
    Observable<HttpResult<String>> deleteLostAndFound();
}
