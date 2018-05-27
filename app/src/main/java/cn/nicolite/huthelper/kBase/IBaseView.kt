package cn.nicolite.huthelper.kBase

/**
 * Created by nicolite on 2018/5/20.
 * email nicolite@nicolite.cn
 * kotlin View 基类
 */
interface IBaseView {
    fun showLoading()
    fun closeLoading()
    fun showMessage(msg: String)
}