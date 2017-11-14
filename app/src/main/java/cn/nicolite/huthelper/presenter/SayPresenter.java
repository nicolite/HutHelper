package cn.nicolite.huthelper.presenter;

import java.util.List;

import cn.nicolite.huthelper.base.presenter.BasePresenter;
import cn.nicolite.huthelper.model.bean.HttpPageResult;
import cn.nicolite.huthelper.model.bean.Say;
import cn.nicolite.huthelper.network.api.APIUtils;
import cn.nicolite.huthelper.network.exception.ExceptionEngine;
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

    public void loadMore(int type, int page){

    }

    public void deleteSay() {

    }

    public void likeSay() {

    }

    public void addComment() {

    }

    public void loadSayList(final int page, final boolean isManual, final boolean isLoadMore) {
        APIUtils
                .getSayAPI()
                .getSayList(page)
                .compose(getActivity().<HttpPageResult<List<Say>>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpPageResult<List<Say>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (getView() != null && !isManual) {
                            getView().showLoading();
                        }
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
