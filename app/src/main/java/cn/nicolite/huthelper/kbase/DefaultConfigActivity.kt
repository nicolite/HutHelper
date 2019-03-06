package cn.nicolite.huthelper.kbase

import android.os.Bundle
import cn.nicolite.huthelper.swipebackhelper.SwipeBackActivity

/**
 * Created by nicolite on 2019/3/6.
 * email nicolite@nicolite.cn
 */
abstract class DefaultConfigActivity : SwipeBackActivity() {
    override fun initConfig(savedInstanceState: Bundle?) {
        super.initConfig(savedInstanceState)
        setImmersiveStatusBar()
        setDeepColorStatusBar()
    }
}