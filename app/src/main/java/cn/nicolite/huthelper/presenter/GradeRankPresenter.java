package cn.nicolite.huthelper.presenter;

import android.text.TextUtils;

import java.util.List;

import cn.nicolite.huthelper.base.presenter.BasePresenter;
import cn.nicolite.huthelper.model.bean.Configure;
import cn.nicolite.huthelper.model.bean.GradeRankResult;
import cn.nicolite.huthelper.model.bean.User;
import cn.nicolite.huthelper.network.api.APIUtils;
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

    public void showRank() {
        if (TextUtils.isEmpty(userId)) {
            getView().showMessage("获取用户信息失败！");
            return;
        }
        List<Configure> configureList = getConfigureList();

        if (ListUtils.isEmpty(configureList)) {
            getView().showMessage("获取用户信息失败！");
            return;
        }

        Configure configure = configureList.get(0);
        User user = configure.getUser();

        APIUtils
                .getGradeAPI()
                .getGradeRank(user.getStudentKH(), configure.getAppRememberCode())
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
                                getView().showRank(gradeRankResult);
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
