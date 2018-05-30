package cn.nicolite.huthelper.view.activity

import android.content.Intent
import android.os.Bundle
import cn.nicolite.huthelper.R
import cn.nicolite.huthelper.kBase.BaseActivity
import cn.nicolite.huthelper.kBase.BaseFragment
import cn.nicolite.huthelper.utils.SnackbarUtils
import cn.nicolite.huthelper.view.fragment.AboutFragment
import cn.nicolite.huthelper.view.fragment.CalendarFragment
import cn.nicolite.huthelper.view.fragment.FreshmanGuideFragment
import cn.nicolite.huthelper.view.fragment.UserInfoFragment
import kotlinx.android.synthetic.main.activity_container.*
import kotlinx.android.synthetic.main.toolbar_nomenu.*

/**
 * Created by nicolite on 2018/5/30.
 * email nicolite@nicolite.cn
 */
class ContainerActivity : BaseActivity() {
    private var type = TYPE_NONE
    private var title = ""
    private var extra = ""
    private var fragment: BaseFragment? = null //需要调用onActivityResult的，将实例赋值给该变量

    companion object {
        const val TYPE_NONE = 0
        const val TYPE_ABOUT = 1
        const val TYPE_CALENDAR = 2
        const val TYPE_FRESHMAN_GUIDE = 3
        const val TYPE_USER_LIST = 4
    }

    override fun initConfig(savedInstanceState: Bundle?) {
        super.initConfig(savedInstanceState)
        setImmersiveStatusBar()
        setDeepColorStatusBar()
        setSlideExit()
    }

    override fun setLayoutId(): Int {
        return R.layout.activity_container
    }

    override fun initBundleData(bundle: Bundle?) {
        super.initBundleData(bundle)
        if (bundle != null) {
            type = bundle.getInt("type", TYPE_NONE)
            title = bundle.getString("title", "")
            extra = bundle.getString("extra", "")
        } else {
            finish()
        }
    }

    override fun doBusiness() {
        super.doBusiness()
        toolbar_title.text = title
        toolbar_back.setOnClickListener { finish() }
        startFragment(type)
    }

    private fun startFragment(type: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        when (type) {
            TYPE_NONE -> {
                SnackbarUtils.showShortSnackbar(rootView, "未匹配到任何页面")
            }
            TYPE_ABOUT -> {
                transaction.replace(R.id.container, AboutFragment())
            }
            TYPE_CALENDAR -> {
                transaction.replace(R.id.container, CalendarFragment())
            }
            TYPE_FRESHMAN_GUIDE -> {
                transaction.replace(R.id.container, FreshmanGuideFragment())
            }
            TYPE_USER_LIST -> {
                val userInfoFragment = UserInfoFragment()
                fragment = userInfoFragment
                transaction.replace(R.id.container, userInfoFragment)
            }
        }
        transaction.commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        fragment?.onActivityResult(requestCode, resultCode, data)
    }

}