package cn.nicolite.huthelper.base;


import java.util.List;

import cn.nicolite.huthelper.db.DaoUtils;
import cn.nicolite.huthelper.db.dao.DaoSession;
import cn.nicolite.huthelper.model.bean.Configure;
import cn.nicolite.huthelper.utils.ListUtils;
import cn.nicolite.mvp.kBase.KBasePresenter;

/**
 * Presenter基类  所有Persenter都要继承此类
 * Created by nicolite on 17-10-13.
 */

public abstract class BasePresenter<I, V> extends KBasePresenter<I, V> {
    protected final String TAG = getClass().getSimpleName();

    protected DaoSession daoSession;
    protected String userId;
    protected Configure configure;

    public BasePresenter(I iView, V view) {
        super(iView, view);
        userId = getLoginUser();
        daoSession = getDaoSession();

        List<Configure> configureList = getConfigureList();
        if (!ListUtils.isEmpty(configureList)) {
            configure = configureList.get(0);
        }
    }

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


    /**
     * 获取Activity
     *
     * @return
     */
    public V getActivity() {
        return super.getView();
    }

}
