package cn.nicolite.huthelper.swipebackhelper

import android.view.MotionEvent
import cn.nicolite.huthelper.view.activity.MainActivity
import cn.nicolite.huthelper.view.activity.SplashActivity
import cn.nicolite.mvp.kBase.KBaseActivity
import com.messy.swipebackhelper.SwipeBackHelper

/*
* 滑动返回的Activity
* 实现了微信滑动返回的联动效果
* 适配了全面屏
* */
abstract class SwipeBackActivity : KBaseActivity() {

    private val swipeBackHelper = SwipeBackHelper().apply { isFullScreen = true/*开启全面屏适配*/ }

    /*
    * 滑动返回
    * */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        return if (skipActivities() || !swipeBackHelper.progressTouchEvent(ev))
            super.dispatchTouchEvent(ev)
        else
            false
    }

    /*
    * 需要跳过的页面
    * */
    private fun skipActivities(): Boolean {
        return this.javaClass == SplashActivity::class.java || this.javaClass == MainActivity::class.java
    }
}