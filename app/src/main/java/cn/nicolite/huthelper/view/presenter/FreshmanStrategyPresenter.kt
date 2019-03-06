package cn.nicolite.huthelper.view.presenter

import cn.nicolite.huthelper.exception.APIException
import cn.nicolite.huthelper.kbase.BasePresenter
import cn.nicolite.huthelper.model.dao.FreshmanStrategy
import cn.nicolite.huthelper.model.wrapper.RestResult
import cn.nicolite.huthelper.network.APIModel
import cn.nicolite.huthelper.network.HttpRequestListener
import cn.nicolite.huthelper.network.sendRxJavaRequest
import cn.nicolite.huthelper.view.IListView
import cn.nicolite.huthelper.view.activity.FreshmanStrategyActivity

/**
 * Created by nicolite on 2019/3/6.
 * email nicolite@nicolite.cn
 */
class FreshmanStrategyPresenter(iView: IListView<FreshmanStrategy>, View: FreshmanStrategyActivity) :
        BasePresenter<IListView<FreshmanStrategy>, FreshmanStrategyActivity>(iView, View) {

    fun loadFreshmanStrategyList() {
        getView()?.let { view ->
            sendRxJavaRequest(APIModel.freshmanStrategyAPI.getFreshmanStrategyList(),
                    view, object : HttpRequestListener<RestResult<List<FreshmanStrategy>>> {
                override fun onResponse(responseResult: RestResult<List<FreshmanStrategy>>) {
                    if (responseResult.code == 200) {
                        getIView()?.showList(responseResult.data.sortedBy { it.position })
                    } else {
                        getIView()?.loadFailure("获取数据失败 code: ${responseResult.code} msg: ${responseResult.msg}")
                    }
                }

                override fun onFailure(exception: APIException) {
                    getIView()?.loadFailure("获取数据失败 $exception")
                }
            })
        }
    }

}