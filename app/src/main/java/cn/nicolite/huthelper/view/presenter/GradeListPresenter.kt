package cn.nicolite.huthelper.view.presenter

import android.text.TextUtils
import cn.nicolite.huthelper.base.BasePresenter
import cn.nicolite.huthelper.db.dao.GradeDao
import cn.nicolite.huthelper.model.bean.Grade
import cn.nicolite.huthelper.model.bean.HttpResult
import cn.nicolite.huthelper.network.APIUtils
import cn.nicolite.huthelper.network.exception.ExceptionEngine
import cn.nicolite.huthelper.utils.ListUtils
import cn.nicolite.huthelper.view.activity.GradeListActivity
import cn.nicolite.huthelper.view.iview.IGradeListView
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Created by nicolite on 17-11-13.
 */

class GradeListPresenter(view: IGradeListView, activity: GradeListActivity) : BasePresenter<IGradeListView, GradeListActivity>(view, activity) {

    fun showGradeList() {
        val gradeDao = daoSession.gradeDao
        val gradeList = gradeDao.queryBuilder()
                .where(GradeDao.Properties.UserId.eq(userId))
                .list()
        if (!ListUtils.isEmpty(gradeList)) {
            getView()?.showGradeList(gradeList)
        } else {
            getView()?.showLoading()
        }

        APIUtils.getGradeAPI().gradeList
                .compose(activity.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .map(Function<HttpResult<List<Grade>>, List<Grade>> { listHttpResult ->
                    var list: List<Grade> = ArrayList()
                    if (listHttpResult.code != 200) {
                        return@Function list
                    }
                    list = listHttpResult.data
                    Collections.sort(list) { grade, t1 ->
                        val temp1 = Integer.parseInt(grade.xn.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0] + grade.xq)
                        val temp2 = Integer.parseInt(t1.xn.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0] + t1.xq)
                        temp2 - temp1
                    }
                    if (!ListUtils.isEmpty(gradeList)) {
                        for (grade in gradeList) {
                            gradeDao.delete(grade)
                        }
                    }
                    for (grade in list) {
                        grade.userId = userId
                        gradeDao.insert(grade)
                    }
                    list
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<List<Grade>> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(gradeList: List<Grade>) {
                        getView()?.closeLoading()
                        if (!ListUtils.isEmpty(gradeList)) {
                            getView()?.showGradeList(gradeList)
                        } else {
                            getView()?.showMessage("没有找到你的成绩")
                        }
                    }

                    override fun onError(e: Throwable) {
                        getView()?.closeLoading()
                        getView()?.showMessage("获取数据错误，" + ExceptionEngine.handleException(e).msg)
                    }

                    override fun onComplete() {
                    }
                })

    }

    fun changeGradeList(gradeList: List<Grade>, xuenian: String, xueqi: String) {
        if (TextUtils.isEmpty(xuenian) || TextUtils.isEmpty(xueqi)) {
            getView()?.changeGradeList(gradeList)
            return
        }
        val list = ArrayList<Grade>()
        for (grade in gradeList) {
            if (grade.xn == xuenian && grade.xq == xueqi) {
                list.add(grade)
            }
        }
        getView()?.changeGradeList(list)
    }
}