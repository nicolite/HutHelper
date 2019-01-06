package cn.nicolite.huthelper.view.customView

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * Created by nicolite on 2018/7/21.
 * email nicolite@nicolite.cn
 * 防止多点触摸数组溢出造成crash的ViewPager
 */
class MultipleTouchViewPager(context: Context, attrs: AttributeSet? = null) : ViewPager(context, attrs) {
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return try {
            super.onInterceptTouchEvent(ev)
        } catch (exception: IllegalArgumentException) {
            exception.printStackTrace()
            false
        }
    }
}