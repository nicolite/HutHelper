package cn.nicolite.huthelper.view.customView

import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView
import cn.nicolite.huthelper.R

/**
 * Created by nicolite on 2018/5/20.
 * email nicolite@nicolite.cn
 * 能设置miniHeight，maxHeight，miniWidth，maxWidth的WebView
 */
class MMWebView : WebView {
    private var miniHeight = 0
    private var maxHeight = 0
    private var miniWidth = 0
    private var maxWidth = 0

    constructor(context: Context) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initStyleAble(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initStyleAble(context, attrs)
    }

    fun setMiniHeight(miniHeight: Int) {
        this.miniHeight = miniHeight
    }

    fun setMaxHeight(maxHeight: Int) {
        this.maxHeight = maxHeight
    }

    fun setMiniWidth(miniWidth: Int) {
        this.miniWidth = miniWidth
    }

    fun setMaxWidth(maxWidth: Int) {
        this.maxWidth = maxWidth
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var height = measuredHeight
        var width = measuredWidth

        if (miniHeight > 0 && maxHeight > 0) {
            if (miniHeight < maxHeight) {
                if (maxHeight < measuredHeight) {
                    height = maxHeight
                } else if (miniHeight > measuredHeight) {
                    height = miniHeight
                }
            } else if (maxHeight < measuredHeight) {
                height = maxHeight
            }
        } else if (miniHeight > 0) {
            if (miniHeight > measuredHeight) {
                height = miniHeight
            }
        } else if (maxHeight > 0) {
            if (maxHeight < measuredHeight) {
                height = maxHeight
            }
        }

        if (miniWidth > 0 && maxWidth > 0) {
            if (miniWidth < maxWidth) {
                if (maxWidth < measuredWidth) {
                    width = maxWidth
                } else if (miniWidth > measuredWidth) {
                    width = miniWidth
                }
            } else if (miniWidth > measuredWidth) {
                width = maxWidth
            }
        } else if (miniWidth > 0) {
            width = miniWidth
        } else if (maxWidth > 0) {
            width = maxWidth
        }

        setMeasuredDimension(width, height)
    }

    private fun initStyleAble(context: Context?, attrs: AttributeSet?) {
        context?.let {
            val typedArray = it.obtainStyledAttributes(attrs, R.styleable.MMWebView)
            miniHeight = typedArray.getDimensionPixelSize(R.styleable.MMWebView_minHeight, 0)
            maxHeight = typedArray.getDimensionPixelSize(R.styleable.MMWebView_maxHeight, 0)
            miniWidth = typedArray.getDimensionPixelSize(R.styleable.MMWebView_minWidth, 0)
            maxWidth = typedArray.getDimensionPixelSize(R.styleable.MMWebView_maxWidth, 0)
            typedArray.recycle()
        }
    }
}