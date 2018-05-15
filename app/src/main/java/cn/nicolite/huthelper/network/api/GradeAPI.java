package cn.nicolite.huthelper.network.api;

import java.util.List;

import cn.nicolite.huthelper.model.Constants;
import cn.nicolite.huthelper.model.bean.Grade;
import cn.nicolite.huthelper.model.bean.GradeRankResult;
import cn.nicolite.huthelper.model.bean.HttpResult;
import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * 成绩相关API
 * Created by nicolite on 17-11-12.
 */

public interface GradeAPI {

    @GET(Constants.API_BASE_URL + "/master/staticAPI/getScore.json")
    Observable<HttpResult<List<Grade>>> getGradeList();

    @GET(Constants.API_BASE_URL + "/master/staticAPI/gradeRank.json")
    Observable<GradeRankResult> getGradeRank();

}
