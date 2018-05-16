package cn.nicolite.huthelper.network.api;

import cn.nicolite.huthelper.model.bean.SyllabusResult;
import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by nicolite on 17-12-2.
 * 课程表API
 */

public interface SyllabusAPI {
    @GET("master/staticAPI/syllabus.json")
    Observable<SyllabusResult> getSyllabus();
}
