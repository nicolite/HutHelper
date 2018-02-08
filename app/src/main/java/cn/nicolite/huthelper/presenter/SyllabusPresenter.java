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

    public void showSyllabus(boolean isForceRefresh) {

        if (TextUtils.isEmpty(userId)) {
            if (getView() != null) {
                getView().showMessage("获取用户信息失败！");
            }
            return;
        }

        //不是强制刷新， 如果数据库中已经有数据，不进行数据请求
        if (!isForceRefresh) {
            List<Lesson> list = daoSession.getLessonDao().queryBuilder()
                    .where(LessonDao.Properties.UserId.eq(userId))
                    .list();
            if (!ListUtils.isEmpty(list)) {
                if (getView() != null) {
                    getView().showSyllabus(list);
                }
                return;
            }
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

                        //合并相同时间相同的课程
                        List<Lesson> newLessonList = new ArrayList<>();
                        int size = lessonList.size();
                        for (int i = 0; i < size; i++) {
                            Lesson lesson1 = lessonList.get(i);
                            for (int j = 0; j < size; j++) {
                                Lesson lesson2 = lessonList.get(j);
                                if (lesson1.getXqj().equals(lesson2.getXqj())
                                        && lesson1.getDjj().equals(lesson2.getDjj())
                                        && lesson1.getName().equals(lesson2.getName())
                                        && lesson1.getRoom().equals(lesson2.getRoom())
                                        && lesson1.getTeacher().equals(lesson2.getTeacher())
                                        && !lesson1.getZs().equals(lesson2.getZs())) {
                                    lesson1.setZs(lesson1.getZs() + "," + lesson2.getZs());
                                    lessonList.remove(lesson2);
                                    size = lessonList.size();
                                }
                            }
                            newLessonList.add(lesson1);
                        }


                        //写入数据库
                        LessonDao lessonDao = daoSession.getLessonDao();

                        if (!ListUtils.isEmpty(newLessonList)) {

                            List<Lesson> list1 = lessonDao.queryBuilder()
                                    .where(LessonDao.Properties.UserId.eq(userId), LessonDao.Properties.AddByUser.eq(false))
                                    .list();

                            for (Lesson oldLesson : list1) {
                                lessonDao.delete(oldLesson);
                            }

                            for (Lesson newLesson : newLessonList) {
                                lessonDao.insert(newLesson);
                            }
                        }

                        return newLessonList;
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
