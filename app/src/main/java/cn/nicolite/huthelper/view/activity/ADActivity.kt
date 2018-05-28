package cn.nicolite.huthelper.view.activity

import android.os.Bundle
import cn.nicolite.huthelper.R
import cn.nicolite.huthelper.kBase.BaseActivity
import cn.nicolite.huthelper.view.iview.IADView
import kotlinx.android.synthetic.main.activity_ad.*
import kotlinx.android.synthetic.main.toolbar_nomenu.*

/**
 * Created by nicolite on 2018/5/27.
 * email nicolite@nicolite.cn
 * 广告页面，用于放广告
 */
class ADActivity : BaseActivity(), IADView {

    override fun initConfig(savedInstanceState: Bundle?) {
        super.initConfig(savedInstanceState)
        setImmersiveStatusBar()
        setDeepColorStatusBar()
        setSlideExit()
    }

    override fun setLayoutId(): Int {
        return R.layout.activity_ad
    }

    override fun doBusiness() {
        super.doBusiness()
        toolbar_title.text = ""
        toolbar_back.setOnClickListener { finish() }
        lRecyclerView.apply {
            setOnRefreshListener {

            }
        }
    }

    override fun showAD() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadADFailure() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}