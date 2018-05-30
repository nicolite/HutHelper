package cn.nicolite.huthelper.view.activity

import android.os.Bundle
import android.text.TextUtils
import cn.nicolite.huthelper.R
import cn.nicolite.huthelper.kBase.BaseActivity
import cn.nicolite.huthelper.utils.ListUtils

/**
 * 闪屏页
 * Created by nicolite on 17-10-17.
 */

class SplashActivity : BaseActivity() {

    private val bgs = intArrayOf(R.drawable.start_1, R.drawable.start_2, R.drawable.start_3, R.drawable.start_4, R.drawable.start_5)

    fun isLogin(): Boolean {
        //对1.3.8以前的版本做兼容，需要重新登录, 版本迭代完成后可删除
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
}
