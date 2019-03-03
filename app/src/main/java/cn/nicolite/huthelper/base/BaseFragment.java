package cn.nicolite.huthelper.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.nicolite.huthelper.db.DaoUtils;
import cn.nicolite.huthelper.db.dao.DaoSession;
import cn.nicolite.huthelper.model.bean.Configure;
import cn.nicolite.mvp.kBase.KBaseFragment;

/**
 * Fragment基类，所有Fragment都要继承此类
 * Created by nicolite on 17-10-13.
 */

public abstract class BaseFragment extends KBaseFragment {
    protected final String TAG = getClass().getSimpleName();
    protected Unbinder unbinder;
    protected Context context;
    protected Activity activity;
    protected DaoSession daoSession;
    protected String userId;

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

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view != null) {
            unbinder = ButterKnife.bind(this, view);
        }
        context = getContext();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        activity = getActivity();
        daoSession = getDaoSession();
        userId = getLoginUser();
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        context = null;
        activity = null;
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    /**
     * 包含回调和数据的页面跳转
     *
     * @param clazz
     * @param bundle
     * @param requestCode
     */
    public void startActivityForResult(Class<?> clazz, int requestCode, Bundle bundle, Bundle options) {
        Intent intent = new Intent();
        intent.setClass(context, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode, options);
    }

}
