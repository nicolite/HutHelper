package cn.nicolite.huthelper.presenter;

import java.util.List;

import cn.nicolite.huthelper.base.BasePresenter;
import cn.nicolite.huthelper.db.dao.ExpLessonDao;
import cn.nicolite.huthelper.model.bean.ExpLesson;
import cn.nicolite.huthelper.model.bean.HttpResult;
import cn.nicolite.huthelper.network.APIUtils;
import cn.nicolite.huthelper.network.exception.ExceptionEngine;
import cn.nicolite.huthelper.utils.ListUtils;
import cn.nicolite.huthelper.view.activity.ExpLessonActivity;
import cn.nicolite.huthelper.view.iview.IExplessonView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * ExplessonPresenter
 * Created by nicolite on 17-11-4.
 */

public class ExplessonPresenter extends BasePresenter<IExplessonView, ExpLessonActivity> {
    public ExplessonPresenter(IExplessonView view, ExpLessonActivity activity) {
        super(view, activity);
    }

    public void showExplesson(final boolean isManual) {

        final ExpLessonDao expLessonDao = getDaoSession().getExpLessonDao();
        final List<ExpLesson> list = expLessonDao
                .queryBuilder()
                .where(ExpLessonDao.Properties.UserId.eq(userId))
                .list();

        if (!ListUtils.isEmpty(list)) {
            if (getView() != null && !isManual ) {
                getView().showExpLesson(list);
                return;
            }
        }

        APIUtils.INSTANCE
                .getExpLessonAPI()
                .getExpLesson(configure.getStudentKH(), configure.getAppRememberCode())
                .compose(getActivity().<HttpResult<List<ExpLesson>>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<ExpLesson>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (ListUtils.isEmpty(list) || isManual) {
                            if (getView() != null) {
                                getView().showLoading();
                            }
                        }
                    }

                    @Override
                    public void onNext(HttpResult<List<ExpLesson>> listHttpResult) {
                        if (getView() != null) {
                            if (listHttpResult != null && listHttpResult.getMsg().equals("ok")) {
                                getView().closeLoading();

                                List<ExpLesson> expLessonList = listHttpResult.getData();
                                if (!ListUtils.isEmpty(expLessonList)) {

                                    getView().showExpLesson(expLessonList);

                                    if (!ListUtils.isEmpty(list)) {
                                        for (ExpLesson ex : list) {
                                            expLessonDao.delete(ex);
                                        }
                                    }

                                    for (ExpLesson ex : expLessonList) {
                                        ex.setUserId(userId);
                                        expLessonDao.insert(ex);
                                    }
                                }
                                return;
                            }
                            getView().showMessage("暂时没有实验课表！");
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().closeLoading();
                            if (!ListUtils.isEmpty(list)) {
                                getView().showExpLesson(list);
                            }
                            getView().showMessage("获取失败，" + ExceptionEngine.handleException(e).getMsg());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }
}
