package cn.nicolite.huthelper.network.api;

import cn.nicolite.huthelper.model.Constants;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 反馈API
 * Created by nicolite on 17-10-24.
 */

public interface FeedBackAPI {
    @FormUrlEncoded
    @POST(Constants.API_BASE_URL + "/master/staticAPI/feedback.json")
    Observable<ResponseBody> feedBack();
}
