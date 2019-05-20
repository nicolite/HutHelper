package cn.nicolite.huthelper.view.customView

import android.content.Context
import android.util.AttributeSet
import android.widget.GridView


class PictureGridView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : GridView(context, attrs, defStyle) {

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        super.onMeasure(widthSpec, MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE shr 2,
                MeasureSpec.AT_MOST))
    }
}
