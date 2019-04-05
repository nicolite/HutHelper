package cn.nicolite.huthelper.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import org.jetbrains.annotations.Nullable;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.nicolite.huthelper.db.DaoUtils;
import cn.nicolite.huthelper.db.dao.DaoSession;
import cn.nicolite.huthelper.model.bean.Configure;
import cn.nicolite.huthelper.utils.SlideUtils;
import cn.nicolite.mvp.kBase.KBaseActivity;

/**
 * Activity 基类 包含生命周期管理
 * 所有Activity都要继承此类
 * Created by nicolite on 17-9-6.
 */

public abstract class BaseActivity extends KBaseActivity {
    protected final String TAG = getClass().getSimpleName();
    protected Context context;
    protected Activity activity;
    protected Unbinder unbinder;
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        context = null;
        activity = null;
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    protected void initBundleData(@Nullable Bundle bundle) {
        super.initBundleData(bundle);
        unbinder = ButterKnife.bind(this);
        context = mContext;
        activity = mActivity;
        daoSession = getDaoSession();
        userId = getLoginUser();
    }

    /**
     * 是否设置滑动退出
     * 注意：需要在主题中设置<item name="android:windowIsTranslucent">true</item>，否则将显示异常
     */
    public void setSlideExit() {
        SlideUtils.INSTANCE.setSlideExit(this);
    }

}
