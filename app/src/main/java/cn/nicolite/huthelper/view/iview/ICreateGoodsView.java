package cn.nicolite.huthelper.view.iview;

import cn.nicolite.huthelper.base.IBaseView;

/**
 * Created by nicolite on 17-11-11.
 */

public interface ICreateGoodsView extends IBaseView {
    void selectImages();

    void uploadGoodsInfo(String imagesInfo);

    void uploadProgress(String msg);

    void uploadFailure(String msg);

    void publishSuccess();
}
