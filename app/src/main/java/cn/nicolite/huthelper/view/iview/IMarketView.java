package cn.nicolite.huthelper.view.iview;

import java.util.List;

import cn.nicolite.huthelper.base.IBaseView;
import cn.nicolite.huthelper.model.bean.Goods;

/**
 * Created by nicolite on 17-11-6.
 */

public interface IMarketView extends IBaseView {
    void showGoodsList(List<Goods> goodsBeanList);
    void showLoadMoreList(List<Goods> goodsBeanList);
    void noMoreData();
    void loadMoreFailure();
    void loadFailure();
}
