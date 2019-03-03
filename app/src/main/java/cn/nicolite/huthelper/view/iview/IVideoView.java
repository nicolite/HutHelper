package cn.nicolite.huthelper.view.iview;

import cn.nicolite.huthelper.base.IBaseView;
import cn.nicolite.huthelper.model.bean.Video;

/**
 * Created by nicolite on 17-12-4.
 */

public interface IVideoView extends IBaseView {
    void showVideoList(Video video);
}
