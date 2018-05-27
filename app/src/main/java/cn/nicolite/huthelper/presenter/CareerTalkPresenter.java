package cn.nicolite.huthelper.presenter;

import android.text.TextUtils;

import java.util.List;

import cn.nicolite.huthelper.base.BasePresenter;
import cn.nicolite.huthelper.model.bean.CareerTalk;
import cn.nicolite.huthelper.model.bean.CareerTalkResult;
import cn.nicolite.huthelper.network.APIUtils;
import cn.nicolite.huthelper.network.exception.ExceptionEngine;
import cn.nicolite.huthelper.view.activity.CareerTalkActivity;
import cn.nicolite.huthelper.view.iview.ICareerTalkView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * CareerTalkPresenter
 * Created by nicolite on 17-11-5.
 */

public class CareerTalkPresenter extends BasePresenter<ICareerTalkView, CareerTalkActivity> {
    public CareerTalkPresenter(ICareerTalkView view, CareerTalkActivity activity) {
        super(view, activity);
    }

    public void showCareerTalkList(final boolean isManual, boolean isAll) {
        if (isAll) {
            loadMoreAll(1, isManual, false);
        } else {
            loadMoreHUT(1, isManual, false);
        }

    }

    public void loadMore(int page, boolean isAll) {
        if (isAll) {
            loadMoreAll(page, true, true);
        } else {
            loadMoreHUT(page, true, true);
        }
    }

    public void loadMoreAll(int page, final boolean isManual, final boolean isLoadMore) {
        APIUtils.INSTANCE
                .getCareerTalkAPI()
                .getCareerTalkList("after", "cs", page)
                .compose(getActivity().<CareerTalkResult<List<CareerTalk>>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CareerTalkResult<List<CareerTalk>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (!isManual) {
                            if (getView() != null) {
                                getView().showLoading();
                            }
                        }
                    }

                    @Override
                    public void onNext(CareerTalkResult<List<CareerTalk>> listCareerTalkResult) {
                        if (getView() != null) {
                            getView().closeLoading();
                            if (!TextUtils.isEmpty(listCareerTalkResult.getStatus())
                                    && listCareerTalkResult.getStatus().equals("success")) {
                                if (isLoadMore) {
                                    getView().loadMore(listCareerTalkResult.getData());
                                    return;
                                }
                                getView().showCareerTalkList(listCareerTalkResult.getData());
                            } else {
                                getView().showMessage("未请求到数据！");
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

    public void loadMoreHUT(int page, final boolean isManual, final boolean isLoadMore) {
        APIUtils.INSTANCE
                .getCareerTalkAPI()
                .getCareerTalkList(218, "after", "cs", page)
                .compose(getActivity().<CareerTalkResult<List<CareerTalk>>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CareerTalkResult<List<CareerTalk>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (!isManual) {
                            if (getView() == null) {
                                return;
                            }
                            getView().showLoading();
                        }
                    }

                    @Override
                    public void onNext(CareerTalkResult<List<CareerTalk>> listCareerTalkResult) {
                        if (getView() != null) {
                            getView().closeLoading();
                            if (!TextUtils.isEmpty(listCareerTalkResult.getStatus())
                                    && listCareerTalkResult.getStatus().equals("success")) {
                                if (isLoadMore) {
                                    getView().loadMore(listCareerTalkResult.getData());
                                    return;
                                }
                                getView().showCareerTalkList(listCareerTalkResult.getData());
                            } else {
                                getView().showMessage("未请求到数据！");
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
