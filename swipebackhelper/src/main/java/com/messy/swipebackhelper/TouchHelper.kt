package com.messy.swipebackhelper

import android.animation.Animator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout

class TouchHelper(private val lifecycleHelper: ActivityLifecycleHelper) {

    companion object {
        private const val STATE_IDLE = 0
        private const val STATE_DRAGGING = 1
        private const val STATE_SETTLING = 2
        private const val FULL_EDGE_OFFSET = 100
        private const val EDGE_WIDTH = 50f
        private const val SHADOW_WIDTH = 30
        internal var isFullScreen = false
    }


    private var currentActivity: Activity? = null
    private var previousContentView: ViewGroup? = null
    private var currentContentView: ViewGroup? = null
    private var previousView: View? = null
    private var currentView: View? = null
    private var previousActivity: Activity? = null
    private var shadowView: ShadowView? = null

    private val context: Context get() = currentActivity!!


    private var state = STATE_IDLE
    private var isInit = false
    private var needClearColorCurrent = false
    private var needClearColorPrevious = false

    private var lastDownTime = -1L

    private fun init() {
        if (isInit) return
        isInit = true
        currentActivity = lifecycleHelper.getCurrentActivity()
        previousActivity = lifecycleHelper.getPreviousActivity()
    }

    fun processTouchEventInternal(ev: MotionEvent): Boolean {
        init()
        if (state == STATE_SETTLING) return true
        val x = ev.rawX
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                lastDownTime = ev.downTime
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                return state == STATE_DRAGGING
            }
            MotionEvent.ACTION_MOVE -> {
                if (state == STATE_DRAGGING || lastDownTime == -1L) {
                    if (ev.actionIndex != 0) {
                        return true
                    }
                    sliding(x)
                    return true
                } else if ((ev.downTime - lastDownTime > 100) && (!isFullScreen && x <= EDGE_WIDTH) || (isFullScreen && x <= EDGE_WIDTH + FULL_EDGE_OFFSET)) {
                    state = STATE_DRAGGING
                    startSlide()
                    return true
                }
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                if (state != STATE_DRAGGING) return false
                startAnim(context.displayWidth / x <= 2, x)
                return true
            }
        }
        return false
    }

//    /*
//    * 忽略点击
//    * */
//    private fun checkTime(ev: MotionEvent): Boolean {
//        return ev.downTime - lastDownTime > 100
//    }

    private fun startAnim(isFinished: Boolean, x: Float) {
        needClearColorCurrent = false
        needClearColorPrevious = false
        val width = context.displayWidth
        val animator = ValueAnimator.ofFloat(x, if (isFinished) width.toFloat() else 0f)
        animator.interpolator = DecelerateInterpolator()
        animator.duration = 200
        animator.addUpdateListener {
            sliding(it.animatedValue as Float)
            state = STATE_SETTLING
        }
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                doEndWork(isFinished)
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
                state = STATE_SETTLING
            }

        })
        animator.start()
    }

    private fun doEndWork(isFinished: Boolean) {
        if (previousActivity == null) return
        if (needClearColorPrevious)
            previousView?.background = null
        if (needClearColorCurrent)
            currentView?.background = null
        currentContentView?.removeView(shadowView)
        previousView.removeInParent()
        previousView?.x = 0f
        previousContentView?.addView(previousView)
        if (isFinished) {
            val backView = BackView(context)
            backView.cacheView(previousView!!)
            currentContentView?.addView(backView, 0)
            currentActivity?.finish()
            currentActivity?.overridePendingTransition(0, 0)
        }
        state = STATE_IDLE
        isInit = false
        currentActivity = null
        previousActivity = null
        previousView = null
        currentView = null
        shadowView = null
        lastDownTime = -1
    }


    private fun startSlide() {
        if (previousActivity == null) return
        //try debug
        try {
            previousContentView = previousActivity?.window?.findViewById(Window.ID_ANDROID_CONTENT)
            previousView = previousContentView?.getChildAt(0)
            currentContentView = currentActivity?.window?.findViewById(Window.ID_ANDROID_CONTENT)
            currentView = currentContentView?.getChildAt(0)
        } catch (e: Exception) {

        }
        if (currentView == null || previousView == null) return
        //try end
        previousView.removeInParent()
        currentContentView?.addView(previousView, 0)
        if (shadowView == null)
            shadowView = ShadowView(context)
        val lp = FrameLayout.LayoutParams(SHADOW_WIDTH, FrameLayout.LayoutParams.MATCH_PARENT)
        shadowView.removeInParent()
        currentContentView?.addView(shadowView, 1, lp)
        shadowView?.x = -SHADOW_WIDTH.toFloat()
        //set background
        if (currentView?.background == null) {
            val ta = currentActivity!!.theme.obtainStyledAttributes(intArrayOf(android.R.attr.windowBackground))
            currentView?.setBackgroundColor(ta.getColor(0, 0))
            needClearColorCurrent = true
            ta.recycle()
        }
        if (previousView?.background == null) {
            val ta = previousActivity!!.theme.obtainStyledAttributes(intArrayOf(android.R.attr.windowBackground))
            previousView?.setBackgroundColor(ta.getColor(0, 0))
            needClearColorPrevious = true
            ta.recycle()
        }
    }

    private fun sliding(rawX: Float) {
        currentView?.x = rawX
        if (previousView != null) {
            previousView!!.x = -previousView!!.width / 3 + rawX / 3
        }
        shadowView?.x = -SHADOW_WIDTH + rawX
        shadowView?.alpha = 1 - (rawX / context.displayWidth)
        Log.d("TH", "alpha=${shadowView?.alpha}")
    }
}