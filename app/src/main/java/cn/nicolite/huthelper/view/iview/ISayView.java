package cn.nicolite.huthelper.view.iview;

import java.util.List;

import cn.nicolite.huthelper.base.iview.IBaseView;
import cn.nicolite.huthelper.model.bean.Say;

/**
 * Created by nicolite on 17-11-14.
 */

public interface ISayView extends IBaseView {
    void showSayList(List<Say> list);
    void loadMore(List<Say> list);
    void noMoreData();
}
