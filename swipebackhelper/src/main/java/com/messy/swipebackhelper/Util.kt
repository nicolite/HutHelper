package com.messy.swipebackhelper

import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager

val metrics by lazy { DisplayMetrics() }

inline val Context.windowManager: WindowManager
    get() = getSystemService(Context.WINDOW_SERVICE) as WindowManager

val Context.displayWidth: Int
    get() {
        windowManager.defaultDisplay.getMetrics(metrics)
        return metrics.widthPixels
    }

inline fun View?.removeInParent() {
    (this?.parent as? ViewGroup)?.removeView(this)
}