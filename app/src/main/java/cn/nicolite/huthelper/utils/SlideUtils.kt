package cn.nicolite.huthelper.utils

import android.app.Activity
import android.graphics.Color
import cn.nicolite.slideback.Slide
import cn.nicolite.slideback.slidemodel.SlidrConfig
import cn.nicolite.slideback.slidemodel.SlideListener
import cn.nicolite.slideback.slidemodel.SlidePosition

/**
 * Created by nicolite on 17-6-23.i
 * Activty活动退出配置
 */


object SlideUtils {
    /**
     * Slidr默认配置
     * @return
     */
    private val slideConfig: SlidrConfig
        get() = SlidrConfig.Builder()
                // .primaryColor(getResources().getColor(R.color.primary)
                // .secondaryColor(getResources().getColor(R.color.secondary) // The % of the screen that counts as the edge, default 18%
                .position(SlidePosition.LEFT)
                .sensitivity(1f)
                .scrimColor(Color.BLACK)
                .scrimStartAlpha(0.8f)
                .scrimEndAlpha(0f)
                .velocityThreshold(2400f)
                .distanceThreshold(0.25f)
                .edge(true)
                .edgeSize(0.18f)
                .listener(object : SlideListener {
                    override fun onSlideStateChanged(state: Int) {
                    }

                    override fun onSlideChange(percent: Float) {
                    }

                    override fun onSlideOpened() {
                    }

                    override fun onSlideClosed(): Boolean {
                        return false
                    }
                })
                .build()

    /**
     * 绑定滑动退出，使用默认配置
     * @param activity
     */
    fun setSlideExit(activity: Activity) {
        Slide.attach(activity, slideConfig)
    }

    /**
     * 绑定滑动退出，自定义配置
     * @param activity
     * @param slideConfig
     */
    fun setSlideExit(activity: Activity, slideConfig: SlidrConfig) {
        Slide.attach(activity, slideConfig)
    }

}