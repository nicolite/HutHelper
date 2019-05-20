package cn.nicolite.huthelper.view.presenter

import cn.nicolite.huthelper.base.BasePresenter
import cn.nicolite.huthelper.db.dao.ExpLessonDao
import cn.nicolite.huthelper.model.bean.ExpLesson
import cn.nicolite.huthelper.model.bean.HttpResult
import cn.nicolite.huthelper.network.APIUtils
import cn.nicolite.huthelper.network.exception.ExceptionEngine
import cn.nicolite.huthelper.utils.ListUtils
import cn.nicolite.huthelper.view.activity.ExpLessonActivity
import cn.nicolite.huthelper.view.iview.IExplessonView
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * ExplessonPresenter
 * Created by nicolite on 17-11-4.
 */

class ExplessonPresenter(view: IExplessonView, activity: ExpLessonActivity) :
        BasePresenter<IExplessonView, ExpLessonActivity>(view, activity) {

    fun showExplesson(isManual: Boolean) {

        val expLessonDao = getDaoSession().expLessonDao
        val list = expLessonDao
                .queryBuilder()
                .where(ExpLessonDao.Properties.UserId.eq(userId))
                .list()

        if (!ListUtils.isEmpty(list) && !isManual) {
            if (getView() != null) {
                getView()!!.showExpLesson(list)
                return
            }
        }

        APIUtils
                .getExpLessonAPI()
                .expLesson
                .compose(activity.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<HttpResult<List<ExpLesson>>> {
                    override fun onSubscribe(d: Disposable) {
                        if (ListUtils.isEmpty(list) || isManual) {
                            getView()?.showLoading()
                        }
                    }

                    override fun onNext(listHttpResult: HttpResult<List<ExpLesson>>) {
                        if (listHttpResult.msg == "ok") {
                            getView()?.closeLoading()

                            val expLessonList = listHttpResult.data
                            if (!ListUtils.isEmpty(expLessonList)) {
                                getView()?.showExpLesson(expLessonList)
                                if (!ListUtils.isEmpty(list)) {
                                    for (ex in list) {
                                        expLessonDao.delete(ex)
                                    }
                                }

                                for (ex in expLessonList) {
                                    ex.userId = userId
                                    expLessonDao.insert(ex)
                                }
                            }
                            return
                        }
                        getView()?.showMessage("暂时没有实验课表！")
                    }

                    override fun onError(e: Throwable) {
                        getView()?.closeLoading()
                        if (!ListUtils.isEmpty(list)) {
                            getView()?.showExpLesson(list)
                        }
                        getView()?.showMessage("获取失败，" + ExceptionEngine.handleException(e).msg)
                    }

                    override fun onComplete() {

                    }
                })


    }
}
