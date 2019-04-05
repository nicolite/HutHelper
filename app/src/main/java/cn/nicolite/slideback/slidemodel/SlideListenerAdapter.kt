package cn.nicolite.slideback.slidemodel

class SlideListenerAdapter : SlideListener {

    override fun onSlideStateChanged(state: Int) {}

    override fun onSlideChange(percent: Float) {}

    override fun onSlideOpened() {}

    override fun onSlideClosed(): Boolean {
        return false
    }
}
