package cn.nicolite.huthelper.network

import cn.nicolite.huthelper.model.dao.FreshmanStrategy
import cn.nicolite.huthelper.model.wrapper.RestResult
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * Created by nicolite on 2019/3/5.
 * email nicolite@nicolite.cn
 */
object APIModel {
    val freshmanStrategyAPI = retrofit.create(FreshmanStrategyAPI::class.java)
}

interface FreshmanStrategyAPI {
    @GET("/huthelper/getFreshmanStrategyList")
    fun getFreshmanStrategyList(): Observable<RestResult<List<FreshmanStrategy>>>
}