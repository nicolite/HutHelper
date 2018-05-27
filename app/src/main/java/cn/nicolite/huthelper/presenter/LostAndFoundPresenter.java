package cn.nicolite.huthelper.presenter;

import android.text.TextUtils;

import java.util.List;

import cn.nicolite.huthelper.base.BasePresenter;
import cn.nicolite.huthelper.model.bean.HttpPageResult;
import cn.nicolite.huthelper.model.bean.LostAndFound;
import cn.nicolite.huthelper.network.APIUtils;
import cn.nicolite.huthelper.network.exception.ExceptionEngine;
import cn.nicolite.huthelper.utils.ListUtils;
import cn.nicolite.huthelper.view.fragment.LostAndFoundFragment;
import cn.nicolite.huthelper.view.iview.ILostAndFoundView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nicolite on 17-11-12.
 */

public class LostAndFoundPresenter extends BasePresenter<ILostAndFoundView, LostAndFoundFragment> {
    public LostAndFoundPresenter(ILostAndFoundView view, LostAndFoundFragment activity) {
        super(view, activity);
    }


    public void showLostAndFoundList(int type, boolean isManual) {
        switch (type) {
            case LostAndFoundFragment.ALL:
                loadMoreAll(1, isManual, false);
                break;
            case LostAndFoundFragment.FOUND:
                loadMoreFound(1, isManual, false);
                break;
            case LostAndFoundFragment.LOST:
                loadMoreLost(1, isManual, false);
                break;
        }
    }

    public void loadMore(int page, int type) {
        switch (type) {
            case LostAndFoundFragment.ALL:
                loadMoreAll(page, true, true);
                break;
            case LostAndFoundFragment.FOUND:
                loadMoreFound(page, true, true);
                break;
            case LostAndFoundFragment.LOST:
                loadMoreLost(page, true, true);
                break;
        }
    }

    public void loadMoreAll(int page, boolean isManual, boolean isLoadMore) {
        loadLostAndFoundList(page, 0, isManual, isLoadMore);
    }

    public void loadMoreFound(int page, boolean isManual, boolean isLoadMore) {
        loadLostAndFoundList(page, 1, isManual, isLoadMore);
    }

    public void loadMoreLost(int page, boolean isManual, boolean isLoadMore) {
        loadLostAndFoundList(page, 2, isManual, isLoadMore);
    }

    public void loadLostAndFoundList(final int page, int type, final boolean isManual, final boolean isLoadMore) {
        APIUtils.INSTANCE
                .getLostAndFoundAPI()
                .getLostAndFoundList(page, type)
                .compose(getActivity().<HttpPageResult<List<LostAndFound>>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpPageResult<List<LostAndFound>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (getView() != null && !isManual) {
                            getView().showLoading();
                        }
                    }

                    @Override
                    public void onNext(HttpPageResult<List<LostAndFound>> listGoodsResult) {
                        if (getView() != null) {
                            getView().closeLoading();
                            if (listGoodsResult.getCode() == 200) {
                                if (isLoadMore) {
                                    if (page <= listGoodsResult.getPageination()) {
                                        getView().showLoadMoreList(listGoodsResult.getData());
                                    } else {
                                        getView().noMoreData();
                                    }
                                } else {
                                    if (ListUtils.isEmpty(listGoodsResult.getData())) {
                                        getView().showMessage("暂时没有相关内容！");
                                    }
                                    getView().showLostAndFoundList(listGoodsResult.getData());
                                }
                            } else {

                                getView().showMessage("获取数据失败，" + listGoodsResult.getCode());

                                if (isLoadMore) {
                                    getView().loadMoreFailure();
                                } else {
                                    getView().loadFailure();
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().closeLoading();
                            getView().showMessage(ExceptionEngine.handleException(e).getMsg());
                            if (isLoadMore) {
                                getView().loadMoreFailure();
                            } else {
                                getView().loadFailure();
                            }
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void searchLostAndFound(String searchText, final int page, final boolean isLoadMore) {
        if (TextUtils.isEmpty(searchText)) {
            searchText = "";
        }

        APIUtils.INSTANCE
                .getLostAndFoundAPI()
                .searchLostAndFound(configure.getStudentKH(), configure.getAppRememberCode(), page, searchText)
                .compose(getActivity().<HttpPageResult<List<LostAndFound>>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpPageResult<List<LostAndFound>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (getView() != null) {
                            getView().showLoading();
                        }
                    }

                    @Override
                    public void onNext(HttpPageResult<List<LostAndFound>> listGoodsResult) {
                        if (getView() != null) {
                            getView().closeLoading();
                            if (listGoodsResult.getCode() == 200) {
                                if (isLoadMore) {
                                    if (page <= listGoodsResult.getPageination()) {
                                        getView().showLoadMoreList(listGoodsResult.getData());
                                    } else {
                                        getView().noMoreData();
                                    }
                                } else {
                                    if (ListUtils.isEmpty(listGoodsResult.getData())) {
                                        getView().showMessage("暂时没有相关内容！");
                                    }
                                    getView().showLostAndFoundList(listGoodsResult.getData());
                                }
                            } else {

                                getView().showMessage("获取数据失败，" + listGoodsResult.getCode());
                                if (isLoadMore) {
                                    getView().loadMoreFailure();
                                } else {
                                    getView().loadFailure();
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().closeLoading();
                            if (isLoadMore) {
                                getView().loadMoreFailure();
                            } else {
                                getView().loadFailure();
                            }
                            getView().showMessage(ExceptionEngine.handleException(e).getMsg());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void showLostAndFoundByUserId(final int page, String userId, final boolean isLoadMore) {

        APIUtils.INSTANCE
                .getLostAndFoundAPI()
                .getLostAndFoundListByUserId(configure.getStudentKH(), configure.getAppRememberCode(), page, userId)
                .compose(getActivity().<HttpPageResult<List<LostAndFound>>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpPageResult<List<LostAndFound>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (getView() != null) {
                            getView().showLoading();
                        }
                    }

                    @Override
                    public void onNext(HttpPageResult<List<LostAndFound>> listGoodsResult) {
                        if (getView() != null) {
                            getView().closeLoading();
                            if (listGoodsResult.getCode() == 200) {
                                if (isLoadMore) {
                                    if (page <= listGoodsResult.getPageination()) {
                                        getView().showLoadMoreList(listGoodsResult.getData());
                                    } else {
                                        getView().noMoreData();
                                    }
                                } else {
                                    getView().showLostAndFoundList(listGoodsResult.getData());
                                    if (ListUtils.isEmpty(listGoodsResult.getData())) {
                                        getView().showMessage("暂时没有相关内容！");
                                    }
                                }
                            } else {
                                getView().showMessage("获取数据失败，" + listGoodsResult.getCode());
                                if (isLoadMore) {
                                    getView().loadMoreFailure();
                                } else {
                                    getView().loadFailure();
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().closeLoading();
                            if (isLoadMore) {
                                getView().loadMoreFailure();
                            } else {
                                getView().loadFailure();
                            }
                            getView().showMessage(ExceptionEngine.handleException(e).getMsg());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
