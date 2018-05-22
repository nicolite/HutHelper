package cn.nicolite.huthelper.db;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

import cn.nicolite.huthelper.app.MApplication;
import cn.nicolite.huthelper.db.dao.ConfigureDao;
import cn.nicolite.huthelper.db.dao.DaoSession;
import cn.nicolite.huthelper.model.bean.Configure;

/**
 * Created by nicolite on 17-10-24.
 */

public class DaoUtils {

    /**
     * 获取daoSession
     */
    public static DaoSession getDaoSession() {
        return DaoHelper.getDaoHelper(MApplication.application).getDaoSession();
    }

    /**
     * 获取配置
     */
    public static List<Configure> getConfigureList() {
        ConfigureDao configureDao = getDaoSession().getConfigureDao();
        return configureDao.queryBuilder().where(ConfigureDao.Properties.UserId.eq(getLoginUser())).list();
    }

    /**
     * 获取当前登录用户
     */
    public static String getLoginUser() {
        SharedPreferences preferences = MApplication.application.getSharedPreferences("login_user", Context.MODE_PRIVATE);
        return preferences.getString("userId", "*");
    }
}
