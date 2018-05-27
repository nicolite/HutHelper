package cn.nicolite.huthelper.view.iview;

import java.util.List;

import cn.nicolite.huthelper.base.IBaseView;
import cn.nicolite.huthelper.model.bean.LostAndFound;

/**
 * Created by nicolite on 17-11-12.
 */

public interface ILostAndFoundView extends IBaseView {
    void showLostAndFoundList(List<LostAndFound> lostAndFoundList);

    void showLoadMoreList(List<LostAndFound> lostAndFoundList);

    void noMoreData();

    void loadMoreFailure();

    void loadFailure();
}
