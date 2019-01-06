package cn.nicolite.huthelper.network.api;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;

/**
 * 反馈API
 * Created by nicolite on 17-10-24.
 */

public interface FeedBackAPI {
    @GET("opensource/staticAPI/feedback.json")
    Observable<ResponseBody> feedBack();
}
