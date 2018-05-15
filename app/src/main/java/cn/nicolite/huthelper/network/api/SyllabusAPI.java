package cn.nicolite.huthelper.network.api;

import cn.nicolite.huthelper.model.Constants;
import cn.nicolite.huthelper.model.bean.SyllabusResult;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by nicolite on 17-12-2.
 * 课程表API
 */

public interface SyllabusAPI {
    @GET(Constants.API_BASE_URL + "/master/staticAPI/syllabus.json")
    Observable<SyllabusResult> getSyllabus();
}
