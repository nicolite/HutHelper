package cn.nicolite.huthelper.base.presenter;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.List;

import cn.nicolite.huthelper.app.MApplication;
import cn.nicolite.huthelper.base.activity.BaseActivity;
import cn.nicolite.huthelper.db.DaoHelper;
import cn.nicolite.huthelper.db.dao.ConfigureDao;
import cn.nicolite.huthelper.db.dao.DaoSession;
import cn.nicolite.huthelper.listener.ActivityLifeCycleListener;
import cn.nicolite.huthelper.model.bean.Configure;

/**
 * Presenter基类  所有Persenter都要继承此类
 * Created by nicolite on 17-10-13.
 */

public class BasePresenter<V, T> implements ActivityLifeCycleListener {

    protected final String TAG = getClass().getSimpleName();

    protected Reference<V> viewRef;
    protected V view;
    protected Reference<T> activityRef;
    protected T activity;

    public BasePresenter(V view, T activity) {
        attachView(view);
        attachActivity(activity);
        setListener(activity);
    }

    /**
     * 获取daoSession
     */
    protected DaoSession getDaoSession() {
        return DaoHelper.getDaoHelper(MApplication.AppContext).getDaoSession();
    }

    /**
     * 获取配置
     */
    protected List<Configure> getConfigureList() {
        ConfigureDao configureDao = getDaoSession().getConfigureDao();
        return configureDao.queryBuilder().where(ConfigureDao.Properties.UserId.eq(getLoginUser())).list();
    }

    /**
     * 获取当前登录用户
     */
    protected String getLoginUser() {
        SharedPreferences preferences = MApplication.AppContext.getSharedPreferences("login_user", Context.MODE_PRIVATE);
        return preferences.getString("userId", null);
    }

    /**
     * 设置生命周期监听
     *
     * @param activity
     */
    private void setListener(T activity) {
        if (getActivity() != null) {
            if (activity instanceof BaseActivity) {
                ((BaseActivity) getActivity()).setOnLifeCycleListener(this);
            }
        }
    }

    /**
     * 绑定View
     *
     * @param view
     */
    private void attachView(V view) {
        viewRef = new WeakReference<V>(view);
        this.view = viewRef.get();
    }

    /**
     * 绑定Activity
     *
     * @param activity
     */
    private void attachActivity(T activity) {
        activityRef = new WeakReference<T>(activity);
        this.activity = activityRef.get();
    }

    /**
     * 解除View绑定
     */
    private void detachView() {
        if (isViewAttached()) {
            viewRef.clear();
            viewRef = null;
        }
    }

    /**
     * 解除Activity绑定
     */
    private void detachActivity() {
        if (isActivityAttached()) {
            activityRef.clear();
            activityRef = null;
        }
    }

    /**
     * 获取View
     *
     * @return
     */
    public V getView() {
        if (viewRef == null) {
            return null;
        }
        return viewRef.get();
    }

    /**
     * 获取Activity
     *
     * @return
     */
    public T getActivity() {
        if (activityRef == null) {
            return null;
        }
        return activityRef.get();
    }

    /**
     * 判断是否已经绑定View
     *
     * @return
     */
    public boolean isViewAttached() {
        return viewRef != null && viewRef.get() != null;
    }

    /**
     * 判定是否已经绑定Activity
     *
     * @return
     */
    public boolean isActivityAttached() {
        return activityRef != null && activityRef.get() != null;
    }


    @Override
    public void onCreate(Bundle saveInstanceState) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {
        detachView();
        detachActivity();
    }

    @Override
    public void onRestart() {

    }
}
