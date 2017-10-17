package cn.nicolite.huthelper.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import cn.nicolite.huthelper.services.InitializeService;

/**
 * Created by nicolite on 17-9-5.
 */

public class MApplication extends Application {
    public static  Context AppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        AppContext = getApplicationContext();
        InitializeService.start(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //初始化多dex配置
        MultiDex.install(this);
    }


}
