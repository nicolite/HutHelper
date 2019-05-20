package cn.nicolite.huthelper.view.customView

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet

/**
 * 可以禁止滑动的LinearLayoutManager
 * Created by nicolite on 17-11-15.
 */

class NoScrollLinearLayoutManager : LinearLayoutManager {

    constructor(context: Context) : super(context)

    constructor(context: Context, orientation: Int, reverseLayout: Boolean) : super(context, orientation, reverseLayout) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}

    override fun canScrollVertically(): Boolean {
        return false
    }

    override fun canScrollHorizontally(): Boolean {
        return false
    }
}
