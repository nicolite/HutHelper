package cn.nicolite.huthelper.network.api;

import java.util.List;

import cn.nicolite.huthelper.model.Constants;
import cn.nicolite.huthelper.model.bean.ExpLesson;
import cn.nicolite.huthelper.model.bean.HttpResult;
import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * 实验课表API
 * Created by nicolite on 17-11-4.
 */

public interface ExpLessonAPI {
    @GET(Constants.API_BASE_URL + "/master/staticAPI/expLesson.json")
    Observable<HttpResult<List<ExpLesson>>> getExpLesson();
}
