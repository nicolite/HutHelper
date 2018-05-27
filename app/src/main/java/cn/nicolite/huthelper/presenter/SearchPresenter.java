package cn.nicolite.huthelper.presenter;

import android.text.TextUtils;

import java.util.List;

import cn.nicolite.huthelper.base.BasePresenter;
import cn.nicolite.huthelper.db.dao.SearchHistoryDao;
import cn.nicolite.huthelper.model.bean.SearchHistory;
import cn.nicolite.huthelper.utils.ListUtils;
import cn.nicolite.huthelper.view.activity.SearchActivity;
import cn.nicolite.huthelper.view.iview.ISearchView;

/**
 * Created by nicolite on 17-11-10.
 */

public class SearchPresenter extends BasePresenter<ISearchView, SearchActivity> {

    public static final int TYPE_USER_SEARCH = 0;
    public static final int TYPE_MARKET_SEARCH = 1;
    public static final int TYPE_LOSTANDFOUND_SERACH = 2;
    public static final int TYPE_MARKET_MYGOODS = 3;
    public static final int TYPE_MYLOSTANDFOUND = 4;
    public static final int TYPE_MYSAY = 586;
    public SearchPresenter(ISearchView view, SearchActivity activity) {
        super(view, activity);
    }

    public void showHistory(int type) {
        SearchHistoryDao searchHistoryDao = daoSession.getSearchHistoryDao();
        List<SearchHistory> list = searchHistoryDao
                .queryBuilder()
                .where(SearchHistoryDao.Properties.Type.eq(type))
                .orderDesc(SearchHistoryDao.Properties.Id)
                .list();
        if (getView() != null) {
            getView().showHistory(list);
        }

    }

    public void addHistory(int type, String history) {
        if (TextUtils.isEmpty(history)) {
            if (getView() != null) {
                getView().showMessage("你还没有输入搜索内容！");
            }
            return;
        }
        SearchHistoryDao searchHistoryDao = daoSession.getSearchHistoryDao();

        List<SearchHistory> list = searchHistoryDao
                .queryBuilder()
                .where(SearchHistoryDao.Properties.Type.eq(type),
                        SearchHistoryDao.Properties.History.eq(history))
                .list();

        if (!ListUtils.isEmpty(list)) {
            for (SearchHistory searchHistory : list) {
                searchHistoryDao.delete(searchHistory);
            }
        }

        SearchHistory searchHistory = new SearchHistory();
        searchHistory.setType(type);
        searchHistory.setHistory(history);

        searchHistoryDao.insert(searchHistory);

        showHistory(type);
    }

    public void deleteHistory(int type) {
        SearchHistoryDao searchHistoryDao = daoSession.getSearchHistoryDao();
        List<SearchHistory> list = searchHistoryDao
                .queryBuilder()
                .where(SearchHistoryDao.Properties.Type.eq(type))
                .list();

        if (!ListUtils.isEmpty(list)) {
            for (SearchHistory searchHistory : list) {
                searchHistoryDao.delete(searchHistory);
            }
        }

        showHistory(type);
    }

    public void deleteHistoryItem(SearchHistory history){
        SearchHistoryDao searchHistoryDao = daoSession.getSearchHistoryDao();
        searchHistoryDao.delete(history);
    }

}
