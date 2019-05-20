package cn.nicolite.huthelper.view.customView

import android.content.Context
import android.util.AttributeSet
import android.widget.GridView

/**
 * Created by nicolite on 17-12-5.
 * 自适应高度的GridView
 */

class MyGridView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : GridView(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE shr 2, MeasureSpec.AT_MOST)
        super.onMeasure(widthMeasureSpec, expandSpec)
    }
}
