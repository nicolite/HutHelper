package cn.nicolite.huthelper.view.iview;

import cn.nicolite.huthelper.base.iview.IBaseView;

/**
 * Created by nicolite on 17-11-11.
 */

public interface ICreateGoodsView extends IBaseView {
    void selectImages();

    void uploadGoodsInfo(String imagesInfo);
    void publishSuccess();
}
