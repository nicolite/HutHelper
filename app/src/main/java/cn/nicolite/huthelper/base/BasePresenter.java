package cn.nicolite.huthelper.base;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.List;

import cn.nicolite.huthelper.base.BaseActivity;
import cn.nicolite.huthelper.base.BaseFragment;
import cn.nicolite.huthelper.db.DaoUtils;
import cn.nicolite.huthelper.db.dao.DaoSession;
import cn.nicolite.huthelper.listener.ActivityLifeCycleListener;
import cn.nicolite.huthelper.listener.FragmentLifeCycleListener;
import cn.nicolite.huthelper.model.bean.Configure;
import cn.nicolite.huthelper.utils.ListUtils;

/**
 * Presenter基类  所有Persenter都要继承此类
 * Created by nicolite on 17-10-13.
 */

public abstract class BasePresenter<V, T> implements ActivityLifeCycleListener, FragmentLifeCycleListener {

    protected final String TAG = getClass().getSimpleName();

    protected Reference<V> viewRef;
    protected Reference<T> activityRef;
    protected DaoSession daoSession;
    protected String userId;
    protected Configure configure;

    public BasePresenter(V view, T activity) {
        attachView(view);
        attachActivity(activity);
        setListener(activity);
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
     * 设置生命周期监听
     *
     * @param activity
     */
    private void setListener(T activity) {
        if (getActivity() != null) {
            if (activity instanceof BaseActivity) {
                ((BaseActivity) getActivity()).setOnLifeCycleListener(this);
            } else if (activity instanceof BaseFragment) {
                ((BaseFragment) getActivity()).setOnLifeCycleListener(this);
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
    }

    /**
     * 绑定Activity
     *
     * @param activity
     */
    private void attachActivity(T activity) {
        activityRef = new WeakReference<T>(activity);
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
        if (getActivity() instanceof BaseActivity) {
            detachView();
            detachActivity();
        }
    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onAttach(Context context) {

    }

    @Override
    public void onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onDestroyView() {
        if (getActivity() instanceof BaseFragment) {
            detachView();
            detachActivity();
        }
    }

    @Override
    public void onDetach() {

    }
}
