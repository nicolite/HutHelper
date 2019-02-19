package com.messy.swipebackhelper

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View

class ShadowView(context: Context) : View(context) {
    private var drawable: Drawable

    init {
        val colors = intArrayOf(0x00000000, 0x17000000, 0x43000000)
        drawable = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawable.setBounds(0, 0, measuredWidth, measuredHeight)
        drawable.draw(canvas)
    }

}