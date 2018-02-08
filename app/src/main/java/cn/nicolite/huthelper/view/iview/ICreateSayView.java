package cn.nicolite.huthelper.view.iview;

import cn.nicolite.huthelper.base.iview.IBaseView;

/**
 * Created by nicolite on 17-11-15.
 */

public interface ICreateSayView extends IBaseView {
    void selectImages();

    void uploadSayInfo(String imagesInfo);

    void publishSuccess();
}
