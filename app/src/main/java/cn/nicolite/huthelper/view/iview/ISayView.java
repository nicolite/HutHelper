package cn.nicolite.huthelper.view.iview;

import java.util.List;

import cn.nicolite.huthelper.base.IBaseView;
import cn.nicolite.huthelper.model.bean.Say;

/**
 * Created by nicolite on 17-11-14.
 */

public interface ISayView extends IBaseView {
    void showSayList(List<Say> list);
    void loadMore(List<Say> list);
    void noMoreData();
    void deleteSuccess(Say say);
    void commentSuccess(String comment, int position, String userId, String username);
    void loadFailure();
    void loadMoreFailure();
}
