package cn.nicolite.huthelper.network.api;

import java.util.List;

import cn.nicolite.huthelper.model.bean.Goods;
import cn.nicolite.huthelper.model.bean.GoodsItem;
import cn.nicolite.huthelper.model.bean.HttpPageResult;
import cn.nicolite.huthelper.model.bean.HttpResult;
import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * 二手市场API
 * Created by nicolite on 17-10-13.
 */

public interface MarketAPI {
    /**
     * 发布商品
     *
     * @return
     */
    @GET("opensource/staticAPI/getGoods.json")
    Observable<HttpResult<String>> createGoods();

    /**
     * 获取商品
     *
     * @return
     */
    @GET("opensource/staticAPI/getGoods.json")
    Observable<HttpPageResult<List<Goods>>> getGoodsList();

    /**
     * 获取指定学号的商品
     *
     * @return
     */
    @GET("opensource/staticAPI/getGoods.json")
    Observable<HttpPageResult<List<Goods>>> getGoodsListByUserId();

    /**
     * 获取商品详情
     *
     * @return
     */
    @GET("opensource/staticAPI/getGoodsInfo.json")
    Observable<HttpPageResult<GoodsItem>> getGoodsInfo();

    /**
     * 删除商品
     *
     * @return
     */
    @GET("opensource/staticAPI/deleteGoods.json")
    Observable<HttpResult<String>> deleteGoods();


    /**
     * 模糊查询
     *
     * @return
     */
    @GET("opensource/staticAPI/getGoods.json")
    Observable<HttpPageResult<List<Goods>>> searchGoods();


}
