package com.messy.swipebackhelper

import android.content.Context
import android.graphics.Canvas
import android.view.View

class BackView(context: Context) : View(context) {
    private var view: View? = null

    fun cacheView(view: View) {
        this.view = view
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        view!!.draw(canvas)
        view = null
    }
}