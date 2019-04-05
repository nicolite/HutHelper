package cn.nicolite.huthelper.kbase

import cn.nicolite.huthelper.utils.SlideUtils
import cn.nicolite.mvp.kBase.KBaseActivity

/**
 * Created by nicolite on 2019/3/3.
 * email nicolite@nicolite.cn
 */
abstract class BaseActivity : KBaseActivity() {
    /**
     * 是否设置滑动退出
     * 注意：需要在主题中设置<item name="android:windowIsTranslucent">true</item>，否则将显示异常
     */
    fun setSlideExit() {
        SlideUtils.setSlideExit(this)
    }

}