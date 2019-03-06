package cn.nicolite.huthelper.view

import cn.nicolite.huthelper.kbase.IBaseView

/**
 * Created by nicolite on 2019/3/6.
 * email nicolite@nicolite.cn
 */
interface ISingleView<T> : IBaseView {
    fun showData(data: T)
    fun loadFailure(msg: String = "")
}

interface IListView<T> : IBaseView {
    fun showList(list: List<T>)
    fun loadFailure(msg: String = "")
}

interface IListMoreView<T> : IBaseView {
    fun showList(list: List<T>, isLoadMore: Boolean = false)
    fun loadFailure(msg: String = "", isLoadMore: Boolean = false)
    fun noMore()
}