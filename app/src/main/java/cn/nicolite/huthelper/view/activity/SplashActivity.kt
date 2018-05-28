package cn.nicolite.huthelper.view.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import cn.nicolite.huthelper.R
import cn.nicolite.huthelper.kBase.BaseActivity
import cn.nicolite.huthelper.utils.ListUtils
import cn.nicolite.huthelper.utils.LogUtils
import com.qq.e.ads.splash.SplashAD
import com.qq.e.ads.splash.SplashADListener
import com.qq.e.comm.util.AdError
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * 闪屏页
 * Created by nicolite on 17-10-17.
 */

class SplashActivity : BaseActivity(), SplashADListener {

    private var canJump = false
    private var splashAD: SplashAD? = null

    private val bgs = intArrayOf(R.drawable.start_1, R.drawable.start_2, R.drawable.start_3, R.drawable.start_4, R.drawable.start_5)


    //对1.3.8以前的版本做兼容，需要重新登录, 版本迭代完成后可删除
    fun isLogin(): Boolean {
        if (!ListUtils.isEmpty(configureList)) {
            if (TextUtils.isEmpty(configureList[0].studentKH)) {
                return false
            }
        }
        return loginUserId != "*" && !ListUtils.isEmpty(configureList)
    }

    override fun initConfig(savedInstanceState: Bundle?) {
        setImmersiveStatusBar()
    }


    override fun setLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun doBusiness() {
        startMainActivity()
    }

    private fun startMainActivity() {
        if (isLogin()) {
            startActivity(MainActivity::class.java)
        } else {
            startActivity(LoginActivity::class.java)
        }
        finish()
    }

    override fun onADDismissed() {
        LogUtils.d(TAG, "$$TAG  -> onADDismissed()")
        nextStep()
    }

    override fun onADPresent() {
        LogUtils.d(TAG, "$$TAG  -> onADPresent()")
        splashHolder.visibility = View.INVISIBLE // 广告展示后一定要把预设的开屏图片隐藏起来
    }

    override fun onNoAD(p0: AdError?) {
        LogUtils.d(TAG, "$TAG -> onNoAD()")
        startMainActivity()
    }

    override fun onADClicked() {
        LogUtils.d(TAG, "$TAG -> onADClicked()")
    }

    override fun onADTick(p0: Long) {
        LogUtils.d(TAG, "$TAG -> onADTick() -> ${p0}ms")
        val round = Math.round(p0 / 1660f)
        if (round > 0)
            skipView.text = "跳过 $round"
        else
            skipView.text = "跳过"
    }

    private fun nextStep() {
        if (canJump) {
            startMainActivity()
        } else {
            canJump = true
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME || super.onKeyDown(keyCode, event)
    }

    override fun onResume() {
        if (canJump) {
            nextStep()
        }
        canJump = true

        super.onResume()
    }

    override fun onPause() {
        canJump = false
        super.onPause()
    }

    override fun onDestroy() {
        splashAD = null
        super.onDestroy()
    }
}
