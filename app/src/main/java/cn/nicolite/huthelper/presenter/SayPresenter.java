package cn.nicolite.huthelper.presenter;

import android.text.TextUtils;

import java.util.List;

import cn.nicolite.huthelper.base.presenter.BasePresenter;
import cn.nicolite.huthelper.model.bean.Configure;
import cn.nicolite.huthelper.model.bean.HttpPageResult;
import cn.nicolite.huthelper.model.bean.HttpResult;
import cn.nicolite.huthelper.model.bean.Say;
import cn.nicolite.huthelper.model.bean.SayLikedCache;
import cn.nicolite.huthelper.model.bean.User;
import cn.nicolite.huthelper.network.api.APIUtils;
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

    public void deleteSay(String sayId) {
        if (TextUtils.isEmpty(userId)) {
            if (getView() != null) {
                getView().showMessage("获取当前登录用户失败，请重新登录！");
            }
            return;
        }

        List<Configure> configureList = getConfigureList();
        if (ListUtils.isEmpty(configureList)) {
            if (getView() != null) {
                getView().showMessage("获取当前登录用户失败，请重新登录！");
            }
            return;
        }

        Configure configure = configureList.get(0);
        User user = configure.getUser();

        APIUtils
                .getSayAPI()
                .deleteSay(user.getStudentKH(), configure.getAppRememberCode(), sayId)
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

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void likeSay(String sayId) {
        if (TextUtils.isEmpty(userId)) {
            if (getView() != null) {
                getView().showMessage("获取当前登录用户失败，请重新登录！");
            }
            return;
        }

        List<Configure> configureList = getConfigureList();
        if (ListUtils.isEmpty(configureList)) {
            if (getView() != null) {
                getView().showMessage("获取当前登录用户失败，请重新登录！");
            }
            return;
        }

        Configure configure = configureList.get(0);
        User user = configure.getUser();

        APIUtils
                .getSayAPI()
                .likeSay(user.getStudentKH(), configure.getAppRememberCode(), sayId)
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

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void addComment(String comment, String sayId) {
        APIUtils
                .getSayAPI()
                .createComment(comment, sayId)
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

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void loadSayList(final int page, final boolean isManual, final boolean isLoadMore) {

        if (TextUtils.isEmpty(userId)) {
            if (getView() != null) {
                getView().showMessage("获取当前登录用户失败，请重新登录！");
            }
            return;
        }

        List<Configure> configureList = getConfigureList();
        if (ListUtils.isEmpty(configureList)) {
            if (getView() != null) {
                getView().showMessage("获取当前登录用户失败，请重新登录！");
            }
            return;
        }

        Configure configure = configureList.get(0);
        User user = configure.getUser();

        loadLikedSay(user.getStudentKH(), configure.getAppRememberCode(), new Observer<HttpResult<List<String>>>() {
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
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                if (getView() != null) {
                    getView().closeLoading();
                    getView().showMessage("加载失败，" + ExceptionEngine.handleException(e).getMsg());
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void loadSayList(final int page, final boolean isLoadMore) {

        APIUtils
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
                                getView().showSayList(listHttpPageResult.getData());
                            } else {
                                getView().closeLoading();
                                getView().showMessage("加载失败!");
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().closeLoading();
                            getView().showMessage("加载失败，" + ExceptionEngine.handleException(e).getMsg());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void loadLikedSay(String studentKh, String appCode, Observer<HttpResult<List<String>>> observer) {
        APIUtils
                .getSayAPI()
                .getLikedSay(studentKh, appCode)
                .compose(getActivity().<HttpResult<List<String>>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void loadLikedSay() {
        if (TextUtils.isEmpty(userId)) {
            if (getView() != null) {
                getView().showMessage("获取当前登录用户失败，请重新登录！");
            }
            return;
        }

        List<Configure> configureList = getConfigureList();
        if (ListUtils.isEmpty(configureList)) {
            if (getView() != null) {
                getView().showMessage("获取当前登录用户失败，请重新登录！");
            }
            return;
        }

        Configure configure = configureList.get(0);
        User user = configure.getUser();

        loadLikedSay(user.getStudentKH(), configure.getAppRememberCode(), new Observer<HttpResult<List<String>>>() {

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
        if (TextUtils.isEmpty(userId)) {
            if (getView() != null) {
                getView().showMessage("获取当前登录用户失败，请重新登录！");
            }
            return;
        }

        List<Configure> configureList = getConfigureList();
        if (ListUtils.isEmpty(configureList)) {
            if (getView() != null) {
                getView().showMessage("获取当前登录用户失败，请重新登录！");
            }
            return;
        }
        final Configure configure = configureList.get(0);
        final User user = configure.getUser();

        loadLikedSay(user.getStudentKH(), configure.getAppRememberCode(), new Observer<HttpResult<List<String>>>() {
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
                        loadSayListByUserId(user.getStudentKH(), configure.getAppRememberCode(), userId,
                                page, isLoadMore);
                    } else {
                        getView().closeLoading();
                        getView().showMessage("加载失败!");
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                if (getView() != null) {
                    getView().closeLoading();
                    getView().showMessage("加载失败，" + ExceptionEngine.handleException(e).getMsg());
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void loadSayListByUserId(String studentKH, String appCode, String userId, final int page, final boolean isLoadMore) {

        APIUtils
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
                                getView().showSayList(listHttpPageResult.getData());
                            } else {
                                getView().showMessage("加载失败!");
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().closeLoading();
                            getView().showMessage("加载失败，" + ExceptionEngine.handleException(e).getMsg());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

}
