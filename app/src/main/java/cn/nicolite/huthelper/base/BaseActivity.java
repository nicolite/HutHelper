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
import cn.nicolite.huthelper.swipebackhelper.SwipeBackActivity;

/**
 * Activity 基类 包含生命周期管理
 * 所有Activity都要继承此类
 * Created by nicolite on 17-9-6.
 */

public abstract class BaseActivity extends SwipeBackActivity {
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
     * 是否设置沉浸状态栏
     *
     * @param isSetStatusBar
     */
    public void setImmersiveStatusBar(boolean isSetStatusBar) {
        setImmersiveStatusBar();
    }

    /**
     * 使布局背景填充状态栏
     */
    public void setLayoutNoLimits(boolean isNoLimits) {
        // 布局背景填充状态栏 与键盘监听冲突
        setLayoutNoLimits();
    }

    /**
     * 设置状态栏字体为深色
     * 注意：如果同时设置了沉浸状态栏，如果开启沉浸状态栏，必须在设置沉浸状态栏之后调用
     *
     * @param isDeepColor
     */
    public void setDeepColorStatusBar(boolean isDeepColor) {
        setDeepColorStatusBar();
    }

    /**
     * 是否设置滑动退出
     * 注意：需要在主题中设置<item name="android:windowIsTranslucent">true</item>，否则将显示异常
     *
     * @param isSlideExit
     */
    public void setSlideExit(boolean isSlideExit) {

    }

}
