package cn.nicolite.huthelper.view.presenter

import cn.nicolite.huthelper.base.BasePresenter
import cn.nicolite.huthelper.db.dao.ExamDao
import cn.nicolite.huthelper.model.bean.Exam
import cn.nicolite.huthelper.network.APIUtils
import cn.nicolite.huthelper.network.exception.ExceptionEngine
import cn.nicolite.huthelper.utils.ListUtils
import cn.nicolite.huthelper.view.activity.ExamActivity
import cn.nicolite.huthelper.view.iview.IExamView
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * ExamPresenter
 * Created by nicolite on 17-11-1.
 */

class ExamPresenter(view: IExamView, activity: ExamActivity) :
        BasePresenter<IExamView, ExamActivity>(view, activity) {

    fun showExam(isManual: Boolean) {
        val examDao = getDaoSession().examDao
        val list = examDao.queryBuilder().where(ExamDao.Properties.UserId.eq(userId)).list()
        if (ListUtils.isEmpty(list) || isManual) {
            getView()?.showLoading()
        }
        if (!ListUtils.isEmpty(list)) {
            getView()?.showExam(list)
        }
        APIUtils.getExamAPI()
                .examData
                .compose(activity.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .map { examResult ->
                    val examList = ArrayList<Exam>()
                    if (examResult.status == "success") {
                        val exam1 = examResult.res.exam
                        val cxexam = examResult.res.cxexam

                        if (!ListUtils.isEmpty(exam1)) {
                            examList.addAll(exam1)
                        }

                        if (!ListUtils.isEmpty(cxexam)) {
                            examList.addAll(cxexam)
                        }

                        if (!ListUtils.isEmpty(list)) {
                            for (exame in list) {
                                examDao.delete(exame)
                            }
                        }

                        for (exam in examList) {
                            exam.userId = userId
                            examDao.insert(exam)
                        }

                    } else {
                        if (!ListUtils.isEmpty(list)) {
                            examList.addAll(list)
                        }
                    }
                    examList
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<List<Exam>> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(exams: List<Exam>) {
                        getView()?.closeLoading()
                        if (ListUtils.isEmpty(exams)) {
                            getView()?.showMessage("没有找到你的考试计划！")
                            return
                        }
                        getView()?.showExam(exams)
                    }

                    override fun onError(e: Throwable) {
                        getView()?.closeLoading()
                        if (!ListUtils.isEmpty(list)) {
                            getView()?.showExam(list)
                        }
                        getView()?.showMessage("加载失败，" + ExceptionEngine.handleException(e).msg)
                    }

                    override fun onComplete() {

                    }
                })
    }
}
