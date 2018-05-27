package cn.nicolite.huthelper.presenter;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.nicolite.huthelper.base.BasePresenter;
import cn.nicolite.huthelper.db.dao.GradeDao;
import cn.nicolite.huthelper.model.bean.Grade;
import cn.nicolite.huthelper.model.bean.HttpResult;
import cn.nicolite.huthelper.network.APIUtils;
import cn.nicolite.huthelper.network.exception.ExceptionEngine;
import cn.nicolite.huthelper.utils.ListUtils;
import cn.nicolite.huthelper.view.activity.GradeListActivity;
import cn.nicolite.huthelper.view.iview.IGradeListView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nicolite on 17-11-13.
 */

public class GradeListPresenter extends BasePresenter<IGradeListView, GradeListActivity> {
    public GradeListPresenter(IGradeListView view, GradeListActivity activity) {
        super(view, activity);
    }

    public void showGradeList() {

        final GradeDao gradeDao = daoSession.getGradeDao();

        final List<Grade> gradeList = gradeDao.queryBuilder()
                .where(GradeDao.Properties.UserId.eq(userId))
                .list();

        if (getView() != null) {
            if (!ListUtils.isEmpty(gradeList)) {
                getView().showGradeList(gradeList);
            } else {
                getView().showLoading();
            }
        }

        APIUtils.INSTANCE
                .getGradeAPI()
                .getGradeList(configure.getStudentKH(), configure.getAppRememberCode())
                .compose(getActivity().<HttpResult<List<Grade>>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .map(new Function<HttpResult<List<Grade>>, List<Grade>>() {
                    @Override
                    public List<Grade> apply(HttpResult<List<Grade>> listHttpResult) throws Exception {
                        List<Grade> list = new ArrayList<>();

                        if (listHttpResult.getCode() != 200) {
                            return list;
                        }

                        list = listHttpResult.getData();

                        Collections.sort(list, new Comparator<Grade>() {
                            @Override
                            public int compare(Grade grade, Grade t1) {
                                int temp1 = Integer.parseInt(grade.getXn().split("-")[0] + grade.getXq());
                                int temp2 = Integer.parseInt(t1.getXn().split("-")[0] + t1.getXq());
                                return temp2 - temp1;
                            }
                        });

                        if (!ListUtils.isEmpty(gradeList)) {
                            for (Grade grade : gradeList) {
                                gradeDao.delete(grade);
                            }
                        }

                        for (Grade grade : list) {
                            grade.setUserId(userId);
                            gradeDao.insert(grade);
                        }
                        return list;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Grade>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Grade> gradeList) {
                        if (getView() != null) {
                            getView().closeLoading();
                            if (!ListUtils.isEmpty(gradeList)) {
                                getView().showGradeList(gradeList);
                            } else {
                                getView().showMessage("没有找到你的成绩");
                            }

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().closeLoading();
                            getView().showMessage("获取数据错误，" + ExceptionEngine.handleException(e).getMsg());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void changeGradeList(final List<Grade> gradeList, final String xuenian, final String xueqi) {
        if (getView() != null) {
            if (TextUtils.isEmpty(xuenian) || TextUtils.isEmpty(xueqi)) {
                getView().changeGradeList(gradeList);
                return;
            }

            List<Grade> list = new ArrayList<>();
            for (Grade grade : gradeList) {
                if (grade.getXn().equals(xuenian) && grade.getXq().equals(xueqi)) {
                    list.add(grade);
                }
            }
            getView().changeGradeList(list);
        }
    }
}