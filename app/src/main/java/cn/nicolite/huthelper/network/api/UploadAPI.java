package cn.nicolite.huthelper.network.api;

import cn.nicolite.huthelper.model.Constants;
import cn.nicolite.huthelper.model.bean.UploadImages;
import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * 上传API接口
 * Created by nicolite on 17-10-28.
 */

public interface UploadAPI {

    /**
     * 上传图片接口
     *
     * @return
     */
    @GET(Constants.API_BASE_URL + "/master/staticAPI/upload.json")
    Observable<UploadImages> uploadImages();
}
