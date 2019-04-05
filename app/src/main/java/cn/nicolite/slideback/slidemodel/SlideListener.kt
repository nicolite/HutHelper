package cn.nicolite.slideback.slidemodel


/**
 * This listener interface is for receiving events from the sliding panel such as state changes
 * and slide progress
 */
interface SlideListener {

    /**
     * This is called when the [androidx.customview.widget.ViewDragHelper] calls it's
     * state change callback.
     *
     * @see androidx.customview.widget.ViewDragHelper.STATE_IDLE
     *
     * @see androidx.customview.widget.ViewDragHelper.STATE_DRAGGING
     *
     * @see androidx.customview.widget.ViewDragHelper.STATE_SETTLING
     *
     * @param state  the [androidx.customview.widget.ViewDragHelper] state
     */
    fun onSlideStateChanged(state: Int)

    fun onSlideChange(percent: Float)

    fun onSlideOpened()

    /**
     * @return `true` than event was processed in the callback.
     */
    fun onSlideClosed(): Boolean
}
