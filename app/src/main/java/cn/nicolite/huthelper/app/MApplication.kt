package cn.nicolite.huthelper.app

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import cn.nicolite.mvp.utils.LogUtils
import com.messy.swipebackhelper.BuildConfig
import com.messy.swipebackhelper.SwipeBackHelper
import com.squareup.leakcanary.LeakCanary

/**
 * Created by nicolite on 17-9-5.
 */

class MApplication : Application() {
    companion object {
        lateinit var AppContext: Context
        lateinit var application: MApplication
    }

    override fun onCreate() {
        super.onCreate()
        AppContext = applicationContext
        application = this

        //初始化Log工具
        LogUtils.debug = BuildConfig.DEBUG
        //初始化滑动返回库
        SwipeBackHelper.init(this)
        if (!LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(this)
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        //初始化多dex配置
        MultiDex.install(this)
    }

}
