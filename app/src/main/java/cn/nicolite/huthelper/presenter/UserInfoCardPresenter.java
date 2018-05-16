package cn.nicolite.huthelper.presenter;

import cn.nicolite.huthelper.base.presenter.BasePresenter;
import cn.nicolite.huthelper.model.bean.HttpResult;
import cn.nicolite.huthelper.model.bean.User;
import cn.nicolite.huthelper.network.APIUtils;
import cn.nicolite.huthelper.view.activity.UserInfoCardActivity;
import cn.nicolite.huthelper.view.iview.IUserInfoCardView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nicolite on 17-11-12.
 */

public class UserInfoCardPresenter extends BasePresenter<IUserInfoCardView, UserInfoCardActivity> {
    public UserInfoCardPresenter(IUserInfoCardView view, UserInfoCardActivity activity) {
        super(view, activity);
    }

    public void showInfo(String userId) {

        APIUtils
                .getUserAPI()
                .getUserInfo()
                .compose(getActivity().<HttpResult<User>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<User>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (getView() != null) {
                            getView().showLoading();
                        }
                    }

                    @Override
                    public void onNext(HttpResult<User> userHttpResult) {
                        if (getView() != null) {
                            getView().closeLoading();
                            if (userHttpResult.getCode() == 200){
                                getView().showInfo(userHttpResult.getData());
                            }else {
                                getView().showMessage("获取信息失败，" + userHttpResult.getCode());
                            }

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().closeLoading();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
