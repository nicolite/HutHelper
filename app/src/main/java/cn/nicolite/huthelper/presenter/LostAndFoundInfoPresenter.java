package cn.nicolite.huthelper.presenter;

import cn.nicolite.huthelper.base.BasePresenter;
import cn.nicolite.huthelper.model.bean.HttpResult;
import cn.nicolite.huthelper.network.APIUtils;
import cn.nicolite.huthelper.network.exception.ExceptionEngine;
import cn.nicolite.huthelper.view.activity.LostAndFoundInfoActivity;
import cn.nicolite.huthelper.view.iview.ILostAndFoundInfoView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nicolite on 17-11-12.
 */

public class LostAndFoundInfoPresenter extends BasePresenter<ILostAndFoundInfoView, LostAndFoundInfoActivity> {
    public LostAndFoundInfoPresenter(ILostAndFoundInfoView view, LostAndFoundInfoActivity activity) {
        super(view, activity);
    }

    public void deleteLostAndFound(String lostId) {

        if (getView() != null) {
            getView().showMessage("正在删除！");
        }

        APIUtils.INSTANCE
                .getLostAndFoundAPI()
                .deleteLostAndFound(configure.getStudentKH(), configure.getAppRememberCode(), lostId)
                .compose(getActivity().<HttpResult<String>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (getView() != null) {
                            getView().showLoading();
                        }
                    }

                    @Override
                    public void onNext(HttpResult<String> stringHttpResult) {
                        if (getView() != null) {
                            getView().closeLoading();
                            if (stringHttpResult.getCode() == 200) {
                                getView().showMessage("删除成功！");
                                getView().deleteSuccess();
                            } else {
                                getView().showMessage("删除失败，" + stringHttpResult.getCode());
                            }
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
}
