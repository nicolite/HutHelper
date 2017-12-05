package cn.nicolite.huthelper.presenter;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import cn.nicolite.huthelper.base.presenter.BasePresenter;
import cn.nicolite.huthelper.db.dao.LessonDao;
import cn.nicolite.huthelper.model.bean.Configure;
import cn.nicolite.huthelper.model.bean.Lesson;
import cn.nicolite.huthelper.model.bean.SyllabusResult;
import cn.nicolite.huthelper.model.bean.User;
import cn.nicolite.huthelper.network.api.APIUtils;
import cn.nicolite.huthelper.network.exception.ExceptionEngine;
import cn.nicolite.huthelper.utils.ListUtils;
import cn.nicolite.huthelper.view.activity.SyllabusActivity;
import cn.nicolite.huthelper.view.iview.ISyllabusView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nicolite on 17-12-4.
 */

public class SyllabusPresenter extends BasePresenter<ISyllabusView, SyllabusActivity> {
    public SyllabusPresenter(ISyllabusView view, SyllabusActivity activity) {
        super(view, activity);
    }

    public void showSyllabus() {
        if (TextUtils.isEmpty(userId)) {
            if (getView() != null) {
                getView().showMessage("获取用户信息失败！");
            }
            return;
        }

        List<Configure> configureList = getConfigureList();

        if (ListUtils.isEmpty(configureList)) {
            if (getView() != null) {
                getView().showMessage("获取用户信息失败！");
            }
            return;
        }

        Configure configure = configureList.get(0);
        User user = configure.getUser();

        if (getView() != null) {
            getView().showLoading();
        }

        APIUtils
                .getSyllabusAPI()
                .getSyllabus(user.getStudentKH(), configure.getAppRememberCode())
                .compose(getActivity().<SyllabusResult>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<SyllabusResult, List<Lesson>>() {
                    @Override
                    public List<Lesson> apply(SyllabusResult syllabusResult) throws Exception {
                        List<Lesson> lessonList = new ArrayList<>();
                        if (syllabusResult.getCode() == 200) {
                            List<SyllabusResult.DataBean> list = syllabusResult.getData();
                            for (SyllabusResult.DataBean dataBean : list) {

                                Lesson lesson = new Lesson();
                                lesson.setName(dataBean.getName());
                                lesson.setRoom(dataBean.getRoom());
                                lesson.setTeacher(dataBean.getTeacher());
                                lesson.setDjj(dataBean.getDjj());
                                lesson.setDsz(dataBean.getDsz());
                                lesson.setXqj(dataBean.getXqj());
                                lesson.setUserId(userId);
                                lesson.setAddByUser(false);

                                List<Integer> zs = dataBean.getZs();
                                StringBuilder stringBuilder = new StringBuilder();

                                for (Integer in : zs) {
                                    stringBuilder.append(in).append(",");
                                }

                                lesson.setZs(stringBuilder.toString());
                                lessonList.add(lesson);

                            }
                        }

                        LessonDao lessonDao = daoSession.getLessonDao();

                        if (!ListUtils.isEmpty(lessonList)) {

                            List<Lesson> list1 = lessonDao.queryBuilder()
                                    .where(LessonDao.Properties.UserId.eq(userId), LessonDao.Properties.AddByUser.eq(false))
                                    .list();

                            for (Lesson oldLesson : list1) {
                                lessonDao.delete(oldLesson);
                            }

                            for (Lesson newLesson : lessonList) {
                                lessonDao.insert(newLesson);
                            }
                        }

                        return lessonList;
                    }
                })
                .subscribe(new Observer<List<Lesson>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Lesson> lessonList) {
                        if (getView() != null) {
                            getView().closeLoading();

                            if (ListUtils.isEmpty(lessonList)) {
                                getView().showMessage("未找到你的课表！");
                            }

                            //将用户自己添加的课程添加进去
                            LessonDao lessonDao = daoSession.getLessonDao();
                            List<Lesson> list = lessonDao.queryBuilder()
                                    .where(LessonDao.Properties.UserId.eq(userId), LessonDao.Properties.AddByUser.eq(true))
                                    .list();
                            lessonList.addAll(list);

                            getView().showSyllabus(lessonList);

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().closeLoading();
                            getView().showMessage(ExceptionEngine.handleException(e).getMsg());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
