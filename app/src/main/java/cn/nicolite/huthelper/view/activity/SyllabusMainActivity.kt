package cn.nicolite.huthelper.view.activity

import android.text.TextUtils
import cn.nicolite.huthelper.kBase.BaseActivity
import cn.nicolite.huthelper.utils.ListUtils

/**
 * Created by nicolite on 2018/5/25.
 * email nicolite@nicolite.cn
 * 桌面课程表入口，用于跳转到课程表
 */
class SyllabusMainActivity : BaseActivity() {

    override fun doBusiness() {
        super.doBusiness()
        if (ListUtils.isEmpty(configureList) || TextUtils.isEmpty(loginUserId) || loginUserId == "*") {
            startActivity(LoginActivity::class.java)
        } else {
            startActivity(SyllabusActivity::class.java)
        }
        finish()
    }

}