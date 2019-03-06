package cn.nicolite.huthelper.kbase

import cn.nicolite.mvp.kBase.KBaseView

/**
 * Created by nicolite on 2019/3/6.
 * email nicolite@nicolite.cn
 */
interface IBaseView : KBaseView {
    override fun showLoading() {
    }

    override fun closeLoading() {
    }

    override fun showMessage(msg: String) {
    }
}