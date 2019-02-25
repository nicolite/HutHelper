package cn.nicolite.huthelper.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.messy.swipebackhelper.BuildConfig;
import com.messy.swipebackhelper.SwipeBackHelper;
import com.squareup.leakcanary.LeakCanary;

import cn.nicolite.mvp.utils.LogUtils;

/**
 * Created by nicolite on 17-9-5.
 */

public class MApplication extends Application {
    public static Context AppContext;
    public static MApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        AppContext = getApplicationContext();
        application = this;

        LogUtils.debug = BuildConfig.DEBUG;
        //初始化滑动返回库
        SwipeBackHelper.init(this);
        if (!LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(this);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //初始化多dex配置
        MultiDex.install(this);
    }
}
