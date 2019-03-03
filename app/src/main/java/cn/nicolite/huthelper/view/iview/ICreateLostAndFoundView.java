package cn.nicolite.huthelper.view.iview;

import cn.nicolite.huthelper.base.IBaseView;

/**
 * Created by nicolite on 17-11-12.
 */

public interface ICreateLostAndFoundView extends IBaseView {
    void selectImages();

    void uploadLostAndFoundInfo(String hidden);

    void uploadProgress(String msg);

    void uploadFailure(String msg);

    void publishSuccess();
}
