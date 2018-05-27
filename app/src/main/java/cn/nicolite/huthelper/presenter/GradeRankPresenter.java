package cn.nicolite.huthelper.presenter;

import java.util.List;

import cn.nicolite.huthelper.base.BasePresenter;
import cn.nicolite.huthelper.db.dao.GradeRankDao;
import cn.nicolite.huthelper.db.dao.GradeSumDao;
import cn.nicolite.huthelper.model.bean.GradeRank;
import cn.nicolite.huthelper.model.bean.GradeRankResult;
import cn.nicolite.huthelper.model.bean.GradeSum;
import cn.nicolite.huthelper.network.APIUtils;
import cn.nicolite.huthelper.network.exception.ExceptionEngine;
import cn.nicolite.huthelper.utils.ListUtils;
import cn.nicolite.huthelper.view.activity.GradeRankActivity;
import cn.nicolite.huthelper.view.iview.IGradeRankView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nicolite on 17-12-2.
 */

public class GradeRankPresenter extends BasePresenter<IGradeRankView, GradeRankActivity> {
    public GradeRankPresenter(IGradeRankView view, GradeRankActivity activity) {
        super(view, activity);
    }

    public void showRank(boolean isForceRefresh) {

        if (!isForceRefresh) {
            List<GradeSum> list = daoSession.getGradeSumDao().queryBuilder()
                    .where(GradeSumDao.Properties.UserId.eq(userId))
                    .list();

            List<GradeRank> xnRank = daoSession.getGradeRankDao().queryBuilder()
                    .where(GradeRankDao.Properties.UserId.eq(userId),
                            GradeRankDao.Properties.IsXq.eq(false))
                    .list();

            List<GradeRank> xqRank = daoSession.getGradeRankDao().queryBuilder()
                    .where(GradeRankDao.Properties.UserId.eq(userId),
                            GradeRankDao.Properties.IsXq.eq(true))
                    .list();

            if (!ListUtils.isEmpty(list) && !ListUtils.isEmpty(xnRank) && !ListUtils.isEmpty(xqRank)) {
                if (getView() != null) {
                    getView().showRank(list.get(0), xnRank, xqRank);
                }
                return;
            }
        }


        APIUtils.INSTANCE
                .getGradeAPI()
                .getGradeRank(configure.getStudentKH(), configure.getAppRememberCode())
                .compose(getActivity().<GradeRankResult>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GradeRankResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (getView() != null) {
                            getView().showLoading();
                        }
                    }

                    @Override
                    public void onNext(GradeRankResult gradeRankResult) {
                        if (getView() != null) {
                            getView().closeLoading();
                            if (gradeRankResult.getCode() == 200) {

                                GradeSumDao gradeSumDao = daoSession.getGradeSumDao();
                                GradeSum gradeSum = new GradeSum();
                                gradeSum.setUserId(userId);
                                gradeSum.setGks(gradeRankResult.getGks());
                                gradeSum.setPjf(gradeRankResult.getPjf());
                                gradeSum.setWdxf(gradeRankResult.getWdxf());
                                gradeSum.setZhjd(gradeRankResult.getZhjd());
                                gradeSum.setZxf(gradeRankResult.getZxf());

                                List<GradeSum> list = gradeSumDao.queryBuilder()
                                        .where(GradeSumDao.Properties.UserId.eq(userId))
                                        .list();
                                if (!ListUtils.isEmpty(list)) {
                                    gradeSum.setId(list.get(0).getId());
                                    gradeSumDao.update(gradeSum);
                                } else {
                                    gradeSumDao.insert(gradeSum);
                                }

                                List<GradeRank> xnRank = gradeRankResult.getRank().getXnrank();
                                List<GradeRank> xqRank = gradeRankResult.getRank().getXqrank();
                                GradeRankDao gradeRankDao = daoSession.getGradeRankDao();

                                List<GradeRank> list1 = gradeRankDao.queryBuilder()
                                        .where(GradeRankDao.Properties.UserId.eq(userId))
                                        .list();

                                if (!ListUtils.isEmpty(list1)) {
                                    for (GradeRank rank : list1) {
                                        gradeRankDao.delete(rank);
                                    }
                                }

                                for (GradeRank rank : xnRank) {
                                    rank.setUserId(userId);
                                    rank.setIsXq(false);
                                    gradeRankDao.insert(rank);
                                }

                                for (GradeRank rank : xqRank) {
                                    rank.setUserId(userId);
                                    rank.setIsXq(true);
                                    gradeRankDao.insert(rank);
                                }

                                getView().showRank(gradeSum, xnRank, xqRank);
                            } else {
                                getView().showMessage("获取数据失败，" + gradeRankResult.getCode());
                            }
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
