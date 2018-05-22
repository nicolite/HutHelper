package cn.nicolite.huthelper.app

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import cn.nicolite.huthelper.BuildConfig
import cn.nicolite.huthelper.model.Constants
import cn.nicolite.huthelper.view.activity.MainActivity
import com.squareup.leakcanary.LeakCanary
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta
import com.tencent.stat.StatConfig
import com.tencent.stat.StatCrashReporter
import com.tencent.stat.StatService
import com.tencent.stat.common.StatConstants

/**
 * Created by nicolite on 2018/5/22.
 * email nicolite@nicolite.cn
 */
class MApplication : Application() {
    companion object {
        lateinit var appContext: Context
        lateinit var application: MApplication
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
        application = this

        if (!LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(this)
        }

        StatConfig.init(this)
        StatService.startStatService(this, Constants.MTA_APPKEY, StatConstants.VERSION)
        StatCrashReporter.getStatCrashReporter(this).javaCrashHandlerStatus = true

        Beta.canShowUpgradeActs.add(MainActivity::class.java)
        Bugly.init(this, Constants.BUGLY_APPID, BuildConfig.LOG_DEBUG)
        Bugly.setIsDevelopmentDevice(this, BuildConfig.LOG_DEBUG)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}