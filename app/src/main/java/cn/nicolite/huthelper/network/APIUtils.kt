package cn.nicolite.huthelper.network

import cn.nicolite.huthelper.network.api.*
import cn.nicolite.huthelper.network.retrofit.RetrofitUtils

/**
 * 接口工具类
 * Created by nicolite on 17-10-14.
 */

object APIUtils {
    private val retrofit = RetrofitUtils.getInstance().retrofit()

    fun getLoginAPI(): LoginAPI = retrofit.create(LoginAPI::class.java)

    fun getMarketAPI(): MarketAPI = retrofit.create(MarketAPI::class.java)

    fun getSayAPI(): SayAPI = retrofit.create(SayAPI::class.java)

    fun getWeatherAPI(): WeatherAPI = retrofit.create(WeatherAPI::class.java)

    fun getDateLineAPI(): TimeAxisAPI = retrofit.create(TimeAxisAPI::class.java)

    fun getUpdateAPI(): UpdateAPI = retrofit.create(UpdateAPI::class.java)

    fun getFeedBackAPI(): FeedBackAPI = retrofit.create(FeedBackAPI::class.java)

    fun getUserAPI(): UserAPI = retrofit.create(UserAPI::class.java)

    fun getUploadAPI(): UploadAPI = retrofit.create(UploadAPI::class.java)

    fun getElectricAPI(): ElectricAPI = retrofit.create(ElectricAPI::class.java)

    fun getVoteAPI(): VoteAPI = retrofit.create(VoteAPI::class.java)

    fun getExamAPI(): ExamAPI = retrofit.create(ExamAPI::class.java)

    fun getExpLessonAPI(): ExpLessonAPI = retrofit.create(ExpLessonAPI::class.java)

    fun getCareerTalkAPI(): CareerTalkAPI = retrofit.create(CareerTalkAPI::class.java)

    fun getLostAndFoundAPI(): LostAndFoundAPI = retrofit.create(LostAndFoundAPI::class.java)

    fun getGradeAPI(): GradeAPI = retrofit.create(GradeAPI::class.java)

    fun getVideoAPI(): VideoAPI = retrofit.create(VideoAPI::class.java)

    fun getSyllabusAPI(): SyllabusAPI = retrofit.create(SyllabusAPI::class.java)
}
