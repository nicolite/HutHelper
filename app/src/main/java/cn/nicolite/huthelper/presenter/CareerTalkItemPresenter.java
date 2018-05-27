package cn.nicolite.huthelper.presenter;

import android.text.TextUtils;

import cn.nicolite.huthelper.base.BasePresenter;
import cn.nicolite.huthelper.model.bean.CareerTalkItem;
import cn.nicolite.huthelper.model.bean.CareerTalkResult;
import cn.nicolite.huthelper.network.APIUtils;
import cn.nicolite.huthelper.network.exception.ExceptionEngine;
import cn.nicolite.huthelper.view.activity.CareerTalkItemActivity;
import cn.nicolite.huthelper.view.iview.ICareerTalkItemView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * CareerTalkItemPresenter
 * Created by nicolite on 17-11-5.
 */

public class CareerTalkItemPresenter extends BasePresenter<ICareerTalkItemView, CareerTalkItemActivity> {
    public CareerTalkItemPresenter(ICareerTalkItemView view, CareerTalkItemActivity activity) {
        super(view, activity);
    }

    public void showContent(int id) {
        APIUtils.INSTANCE
                .getCareerTalkAPI()
                .getCareerTalk(id)
                .compose(getActivity().<CareerTalkResult<CareerTalkItem>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CareerTalkResult<CareerTalkItem>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (getView() != null) {
                            getView().showLoading();
                        }
                    }

                    @Override
                    public void onNext(CareerTalkResult<CareerTalkItem> careerTalkItemCareerTalkResult) {
                        if (getView() != null) {
                            getView().closeLoading();
                            if (!TextUtils.isEmpty(careerTalkItemCareerTalkResult.getStatus())
                                    && careerTalkItemCareerTalkResult.getStatus().equals("success")) {
                                getView().showContent(careerTalkItemCareerTalkResult.getData());
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
