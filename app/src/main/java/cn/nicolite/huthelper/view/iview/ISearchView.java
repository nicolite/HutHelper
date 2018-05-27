package cn.nicolite.huthelper.view.iview;

import java.util.List;

import cn.nicolite.huthelper.base.IBaseView;
import cn.nicolite.huthelper.model.bean.SearchHistory;

/**
 * Created by nicolite on 17-11-10.
 */

public interface ISearchView extends IBaseView {
    void showHistory(List<SearchHistory> historyList);
}
