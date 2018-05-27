package cn.nicolite.huthelper.presenter;

import java.util.List;

import cn.nicolite.huthelper.base.BasePresenter;
import cn.nicolite.huthelper.model.bean.HttpPageResult;
import cn.nicolite.huthelper.model.bean.HttpResult;
import cn.nicolite.huthelper.model.bean.Say;
import cn.nicolite.huthelper.model.bean.SayLikedCache;
import cn.nicolite.huthelper.model.bean.User;
import cn.nicolite.huthelper.network.APIUtils;
import cn.nicolite.huthelper.network.exception.ExceptionEngine;
import cn.nicolite.huthelper.utils.ListUtils;
import cn.nicolite.huthelper.view.fragment.SayFragment;
import cn.nicolite.huthelper.view.iview.ISayView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nicolite on 17-11-14.
 */

public class SayPresenter extends BasePresenter<ISayView, SayFragment> {

    public SayPresenter(ISayView view, SayFragment activity) {
        super(view, activity);
    }

    public void showSayList(int type, boolean isManual) {
        switch (type) {
            case SayFragment.ALLSAY:
                loadSayList(1, isManual, false);
                break;
            case SayFragment.MYSAY:
                break;
        }
    }

    public void loadMore(int type, int page) {
        switch (type) {
            case SayFragment.ALLSAY:
                loadSayList(page, true, true);
                break;
            case SayFragment.MYSAY:
                break;
        }
    }

    public void deleteSay(final int position, final String sayId) {

        if (getView() != null) {
            getView().showMessage("正在删除！");
        }
        APIUtils.INSTANCE
                .getSayAPI()
                .deleteSay(configure.getStudentKH(), configure.getAppRememberCode(), sayId)
                .compose(getActivity().<HttpResult<String>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult<String> stringHttpResult) {
                        if (getView() != null) {
                            getView().deleteSaySuccess(position, sayId);
                            getView().showMessage("删除成功！");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().closeLoading();
                            getView().showMessage("删除失败，" + ExceptionEngine.handleException(e).getMsg());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void deleteComment(final int sayPosition, final String commentId, final int commentPosition) {
        APIUtils
                .INSTANCE
                .getSayAPI()
                .deleteComment(configure.getStudentKH(), configure.getAppRememberCode(), commentId)
                .compose(getActivity().<HttpResult<String>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult<String> stringHttpResult) {
                        if (getView() != null) {
                            getView().deleteCommentSuccess(sayPosition, commentId, commentPosition);
                            getView().showMessage("删除成功！");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().closeLoading();
                            getView().showMessage("删除失败，" + ExceptionEngine.handleException(e).getMsg());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void likeSay(String sayId) {

        APIUtils.INSTANCE
                .getSayAPI()
                .likeSay(configure.getStudentKH(), configure.getAppRememberCode(), sayId)
                .compose(getActivity().<HttpResult<String>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult<String> stringHttpResult) {

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

    public void addComment(final String comment, String sayId, final int position) {

        if (getView() != null) {
            getView().showMessage("正在评论...！");
        }


        APIUtils.INSTANCE
                .getSayAPI()
                .createComment(configure.getStudentKH(), configure.getAppRememberCode(), comment, sayId)
                .compose(getActivity().<HttpResult<String>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult<String> stringHttpResult) {
                        if (getView() != null) {
                            if (stringHttpResult.getCode() == 200) {
                                User user = configure.getUser();
                                getView().commentSuccess(comment, position, userId, user.getUsername());
                                getView().showMessage("评论成功！");
                            }
                        } else {
                            getView().showMessage("评论失败！");
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().closeLoading();
                            getView().showMessage("评论失败，" + ExceptionEngine.handleException(e).getMsg());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void loadSayList(final int page, final boolean isManual, final boolean isLoadMore) {

        loadLikedSay(configure.getStudentKH(), configure.getAppRememberCode(), new Observer<HttpResult<List<String>>>() {
            @Override
            public void onSubscribe(Disposable d) {
                if (getView() != null && !isManual) {
                    getView().showLoading();
                }
            }

            @Override
            public void onNext(HttpResult<List<String>> listHttpResult) {
                if (getView() != null) {
                    if (listHttpResult.getMsg().equals("成功获得点赞数据")) {
                        SayLikedCache.setLikedList(listHttpResult.getData());
                        loadSayList(page, isLoadMore);
                    } else {
                        getView().showMessage("加载失败!");
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
                    getView().showMessage("加载失败，" + ExceptionEngine.handleException(e).getMsg());
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

    private void loadSayList(final int page, final boolean isLoadMore) {

        APIUtils.INSTANCE
                .getSayAPI()
                .getSayList(page)
                .compose(getActivity().<HttpPageResult<List<Say>>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpPageResult<List<Say>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpPageResult<List<Say>> listHttpPageResult) {
                        if (getView() != null) {
                            getView().closeLoading();
                            if (listHttpPageResult.getCode() == 200) {
                                if (isLoadMore) {
                                    if (page <= listHttpPageResult.getPageination()) {
                                        getView().loadMore(listHttpPageResult.getData());
                                    } else {
                                        getView().noMoreData();
                                    }
                                    return;
                                }
                                if (ListUtils.isEmpty(listHttpPageResult.getData())) {
                                    getView().showMessage("暂时没有相关内容！");
                                }
                                getView().showSayList(listHttpPageResult.getData());
                            } else {
                                getView().closeLoading();
                                getView().showMessage("加载失败!");
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
                            getView().showMessage("加载失败，" + ExceptionEngine.handleException(e).getMsg());
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

    public void loadLikedSay(String studentKh, String appCode, Observer<HttpResult<List<String>>> observer) {
        APIUtils.INSTANCE
                .getSayAPI()
                .getLikedSay(studentKh, appCode)
                .compose(getActivity().<HttpResult<List<String>>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void loadLikedSay() {

        loadLikedSay(configure.getStudentKH(), configure.getAppRememberCode(), new Observer<HttpResult<List<String>>>() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HttpResult<List<String>> listHttpResult) {
                if (listHttpResult.getMsg().equals("成功获得点赞数据")) {
                    SayLikedCache.setLikedList(listHttpResult.getData());
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }

    public void loadSayListByUserId(final String userId, final int page, final boolean isManual, final boolean isLoadMore) {

        loadLikedSay(configure.getStudentKH(), configure.getAppRememberCode(), new Observer<HttpResult<List<String>>>() {
            @Override
            public void onSubscribe(Disposable d) {
                if (getView() != null && !isManual) {
                    getView().showLoading();
                }
            }

            @Override
            public void onNext(HttpResult<List<String>> listHttpResult) {
                if (getView() != null) {
                    if (listHttpResult.getMsg().equals("成功获得点赞数据")) {
                        SayLikedCache.setLikedList(listHttpResult.getData());
                        loadSayListByUserId(configure.getStudentKH(), configure.getAppRememberCode(), userId,
                                page, isLoadMore);
                    } else {
                        getView().closeLoading();
                        getView().showMessage("加载失败!");
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
                    getView().showMessage("加载失败，" + ExceptionEngine.handleException(e).getMsg());
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

    private void loadSayListByUserId(String studentKH, String appCode, String userId, final int page, final boolean isLoadMore) {

        APIUtils.INSTANCE
                .getSayAPI()
                .getSayListByuserId(studentKH, appCode, page, userId)
                .compose(getActivity().<HttpPageResult<List<Say>>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpPageResult<List<Say>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpPageResult<List<Say>> listHttpPageResult) {
                        if (getView() != null) {
                            getView().closeLoading();
                            if (listHttpPageResult.getCode() == 200) {
                                if (isLoadMore) {
                                    if (page <= listHttpPageResult.getPageination()) {
                                        getView().loadMore(listHttpPageResult.getData());
                                    } else {
                                        getView().noMoreData();
                                    }
                                    return;
                                }
                                if (ListUtils.isEmpty(listHttpPageResult.getData())) {
                                    getView().showMessage("暂时没有相关内容！");
                                }
                                getView().showSayList(listHttpPageResult.getData());
                            } else {
                                getView().showMessage("加载失败!");
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
                            getView().showMessage("加载失败，" + ExceptionEngine.handleException(e).getMsg());
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

}
