package cn.nicolite.huthelper.view.presenter

import android.text.TextUtils
import cn.nicolite.huthelper.base.BasePresenter
import cn.nicolite.huthelper.model.bean.Electric
import cn.nicolite.huthelper.model.bean.Vote
import cn.nicolite.huthelper.network.APIUtils
import cn.nicolite.huthelper.network.exception.ExceptionEngine
import cn.nicolite.huthelper.utils.ListUtils
import cn.nicolite.huthelper.view.activity.ElectricActivity
import cn.nicolite.huthelper.view.customView.CommonDialog
import cn.nicolite.huthelper.view.iview.IElectricView
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by nicolite on 17-10-31.
 */

class ElectricPresenter(view: IElectricView, activity: ElectricActivity) : BasePresenter<IElectricView, ElectricActivity>(view, activity) {

    fun showLouHao() {
        if (TextUtils.isEmpty(userId)) {
            getView()?.showMessage("获取当前登录用户失败，请重新登录！")
            return
        }
        val configureList = configureList
        if (ListUtils.isEmpty(configureList)) {
            getView()?.showMessage("获取用户信息失败！")
            return
        }
        val configure = configureList[0]
        if (!TextUtils.isEmpty(configure.lou) && !TextUtils.isEmpty(configure.hao)) {
            getView()?.showLouHao(configure.lou, configure.hao)
        }

    }

    fun showElectricData(lou: String, hao: String) {
        if (TextUtils.isEmpty(lou) || TextUtils.isEmpty(hao)) {
            getView()?.showMessage("宿舍楼栋和宿舍号不能为空")
            return
        }
        configure.lou = lou
        configure.hao = hao
        configure.update()
        APIUtils
                .getElectricAPI().electric
                .compose(activity.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Electric> {
                    override fun onSubscribe(d: Disposable) {
                        getView()?.showLoading()
                    }

                    override fun onNext(electric: Electric) {
                        getView()?.closeLoading()
                        if (electric.code == 200) {
                            getView()?.showElectric(electric)
                        } else {
                            getView()?.showMessage("获取电费数据失败！ " + electric.code)
                        }

                    }

                    override fun onError(e: Throwable) {
                        getView()?.closeLoading()
                        getView()?.showMessage(ExceptionEngine.handleException(e).msg)
                    }

                    override fun onComplete() {

                    }
                })
    }

    fun showWeather() {
        getView()?.showWeather(configure.city, configure.tmp, configure.content)
    }

    fun showVoteSummary() {
        APIUtils
                .getVoteAPI()
                .airConditionerData
                .compose(activity.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Vote> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(vote: Vote) {
                        if (!TextUtils.isEmpty(vote.msg) && vote.msg == "令牌错误") {
                            getView()?.showMessage(vote.msg + "，请重新登录！")
                            return
                        }
                        if (vote.isCode) {
                            getView()?.showVoteSummary(vote.data.yes, vote.data.no, vote.opt)
                        } else {
                            getView()?.showMessage("获取投票数据失败！　" + vote.msg)
                        }
                    }

                    override fun onError(e: Throwable) {
                        getView()?.showMessage("获取投票数据失败，请检查网络！")
                    }

                    override fun onComplete() {

                    }
                })
    }

    fun vote(opt: String) {
        APIUtils
                .getVoteAPI()
                .setAirConditionerData()
                .compose(activity.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Vote> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(vote: Vote) {
                        if (!TextUtils.isEmpty(vote.msg) && vote.msg == "令牌错误") {
                            getView()?.showMessage(vote.msg + "，请重新登录！")
                            return
                        }
                        if (vote.isCode) {
                            getView()?.showMessage("投票成功！")
                            getView()?.showVoteSummary(vote.data.yes, vote.data.no, vote.opt)
                        } else {
                            getView()?.showMessage("投票失败，你已经投过了！")
                        }
                    }

                    override fun onError(e: Throwable) {
                        getView()?.showMessage("投票失败! " + ExceptionEngine.handleException(e).msg)
                    }

                    override fun onComplete() {

                    }
                })

    }


    fun showVoteDialog(opt: String) {
        val commonDialog = CommonDialog(activity)
        commonDialog
                .setMessage("确认提交？")
                .setPositiveButton("提交") {
                    commonDialog.dismiss()
                    vote(opt)
                }
                .setNegativeButton("不投了", null)
                .show()
    }

    companion object {
        val YES = "1"
        val NO = "2"
    }

}
