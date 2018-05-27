package cn.nicolite.huthelper.presenter;

import android.text.TextUtils;

import java.util.List;

import cn.nicolite.huthelper.base.BasePresenter;
import cn.nicolite.huthelper.model.bean.Goods;
import cn.nicolite.huthelper.model.bean.HttpPageResult;
import cn.nicolite.huthelper.network.APIUtils;
import cn.nicolite.huthelper.network.exception.ExceptionEngine;
import cn.nicolite.huthelper.utils.ListUtils;
import cn.nicolite.huthelper.view.fragment.MarketFragment;
import cn.nicolite.huthelper.view.iview.IMarketView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * MarketPresenter
 * Created by nicolite on 17-11-6.
 */

public class MarketPresenter extends BasePresenter<IMarketView, MarketFragment> {

    public MarketPresenter(IMarketView view, MarketFragment activity) {
        super(view, activity);
    }


    public void showGoodsList(int type, boolean isManual) {
        switch (type) {
            case MarketFragment.ALL:
                loadMoreAll(1, isManual, false);
                break;
            case MarketFragment.SOLD:
                loadMoreSold(1, isManual, false);
                break;
            case MarketFragment.BUY:
                loadMoreBuy(1, isManual, false);
                break;
        }
    }

    public void loadMore(int page, int type) {
        switch (type) {
            case MarketFragment.ALL:
                loadMoreAll(page, true, true);
                break;
            case MarketFragment.SOLD:
                loadMoreSold(page, true, true);
                break;
            case MarketFragment.BUY:
                loadMoreBuy(page, true, true);
                break;

        }
    }

    public void loadMoreAll(int page, boolean isManual, boolean isLoadMore) {
        loadGoodsList(page, "", isManual, isLoadMore);
    }

    public void loadMoreSold(int page, boolean isManual, boolean isLoadMore) {
        loadGoodsList(page, "1", isManual, isLoadMore);
    }

    public void loadMoreBuy(int page, boolean isManual, boolean isLoadMore) {
        loadGoodsList(page, "2", isManual, isLoadMore);
    }


    public void loadGoodsList(final int page, String type, final boolean isManual, final boolean isLoadMore) {

        APIUtils.INSTANCE
                .getMarketAPI()
                .getGoodsList(page, type)
                .compose(getActivity().<HttpPageResult<List<Goods>>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpPageResult<List<Goods>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (getView() != null && !isManual) {
                            getView().showLoading();
                        }
                    }

                    @Override
                    public void onNext(HttpPageResult<List<Goods>> listGoodsResult) {
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
                                    getView().showGoodsList(listGoodsResult.getData());
                                }
                            } else {
                                if (isLoadMore) {
                                    getView().loadMoreFailure();
                                } else {
                                    getView().loadFailure();
                                }

                                getView().showMessage("获取数据失败，" + listGoodsResult.getCode());

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

    public void searchGoods(String searchText, final int page, final boolean isLoadMore) {
        if (TextUtils.isEmpty(searchText)) {
            searchText = "";
        }

        APIUtils.INSTANCE
                .getMarketAPI()
                .searchGoods(configure.getStudentKH(), configure.getAppRememberCode(), page, searchText)
                .compose(getActivity().<HttpPageResult<List<Goods>>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpPageResult<List<Goods>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (getView() != null) {
                            getView().showLoading();
                        }
                    }

                    @Override
                    public void onNext(HttpPageResult<List<Goods>> listGoodsResult) {
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
                                    getView().showGoodsList(listGoodsResult.getData());
                                }
                            } else {
                                if (isLoadMore) {
                                    getView().loadMoreFailure();
                                } else {
                                    getView().loadFailure();
                                }

                                getView().showMessage("获取数据失败，" + listGoodsResult.getCode());

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

    public void showGoodsByUserId(final int page, String userId, final boolean isLoadMore) {

        APIUtils.INSTANCE
                .getMarketAPI()
                .getGoodsListByUserId(configure.getStudentKH(), configure.getAppRememberCode(), page, userId)
                .compose(getActivity().<HttpPageResult<List<Goods>>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpPageResult<List<Goods>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (getView() != null) {
                            getView().showLoading();
                        }
                    }

                    @Override
                    public void onNext(HttpPageResult<List<Goods>> listGoodsResult) {
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
                                    getView().showGoodsList(listGoodsResult.getData());
                                }
                            } else {
                                if (isLoadMore) {
                                    getView().loadMoreFailure();
                                } else {
                                    getView().loadFailure();
                                }

                                getView().showMessage("获取数据失败，" + listGoodsResult.getCode());

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
