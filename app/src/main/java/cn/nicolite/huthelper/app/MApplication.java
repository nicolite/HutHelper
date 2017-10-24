package cn.nicolite.huthelper.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import cn.nicolite.huthelper.BuildConfig;
import cn.nicolite.huthelper.model.bean.MyObjectBox;
import cn.nicolite.huthelper.services.InitializeService;
import io.objectbox.BoxStore;
import io.objectbox.android.AndroidObjectBrowser;


/**
 * Created by nicolite on 17-9-5.
 */

public class MApplication extends Application {
    public static  Context AppContext;
    private BoxStore boxStore;
    public static  MApplication application;
    @Override
    public void onCreate() {
        super.onCreate();
        AppContext = getApplicationContext();
        application = this;

        boxStore = MyObjectBox.builder().androidContext(this).build();

        if (BuildConfig.LOG_DEBUG){
            new AndroidObjectBrowser(boxStore).start(this);
        }

        InitializeService.start(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //初始化多dex配置
        MultiDex.install(this);
    }

    public BoxStore getBoxStore() {
        return boxStore;
    }
}
