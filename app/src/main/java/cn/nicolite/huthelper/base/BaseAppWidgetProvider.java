package cn.nicolite.huthelper.base;

import android.appwidget.AppWidgetProvider;

import java.util.List;

import cn.nicolite.huthelper.db.DaoUtils;
import cn.nicolite.huthelper.db.dao.DaoSession;
import cn.nicolite.huthelper.model.bean.Configure;

/**
 * Created by nicolite on 17-12-15.
 */

public class BaseAppWidgetProvider extends AppWidgetProvider {
    protected final String TAG = getClass().getSimpleName();

    /**
     * 获取daoSession
     */
    protected DaoSession getDaoSession() {
        return DaoUtils.getDaoSession();
    }

    /**
     * 获取配置
     */
    protected List<Configure> getConfigureList() {
        return DaoUtils.getConfigureList();
    }

    /**
     * 获取当前登录用户
     */
    protected String getLoginUser() {
        return DaoUtils.getLoginUser();
    }
}
