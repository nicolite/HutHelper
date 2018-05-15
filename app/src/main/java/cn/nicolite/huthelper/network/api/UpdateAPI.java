package cn.nicolite.huthelper.network.api;

import cn.nicolite.huthelper.model.Constants;
import cn.nicolite.huthelper.model.bean.HttpResult;
import cn.nicolite.huthelper.model.bean.Update;
import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * 检查更新API
 * Created by nicolite on 17-10-24.
 */

public interface UpdateAPI {
    @GET(Constants.API_BASE_URL + "/master/staticAPI/version.json")
    Observable<HttpResult<Update>> checkUpdate();
}
