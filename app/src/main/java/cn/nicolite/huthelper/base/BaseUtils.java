package cn.nicolite.huthelper.base;

import java.util.List;

import cn.nicolite.huthelper.db.DaoUtils;
import cn.nicolite.huthelper.db.dao.DaoSession;
import cn.nicolite.huthelper.model.bean.Configure;

/**
 * Utils基类 所有Utils都需要继承此类开发
 * Created by nicolite on 2018/3/5.
 */

public abstract class BaseUtils {
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
