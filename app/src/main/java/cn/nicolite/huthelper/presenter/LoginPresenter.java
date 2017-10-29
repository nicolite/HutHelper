package cn.nicolite.huthelper.presenter;


import cn.nicolite.huthelper.base.presenter.BasePresenter;
import cn.nicolite.huthelper.model.bean.Configure;
import cn.nicolite.huthelper.model.bean.Configure_;
import cn.nicolite.huthelper.model.bean.HttpResult;
import cn.nicolite.huthelper.model.bean.Token;
import cn.nicolite.huthelper.model.bean.User;
import cn.nicolite.huthelper.network.api.APIUtils;
import cn.nicolite.huthelper.network.exception.ExceptionEngine;
import cn.nicolite.huthelper.view.activity.LoginActivity;
import cn.nicolite.huthelper.view.iview.ILoginView;
import io.objectbox.Box;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Login Presenter
 * Created by nicolite on 17-10-17.
 */

public class LoginPresenter extends BasePresenter<ILoginView, LoginActivity> {

    public LoginPresenter(ILoginView view, LoginActivity activity) {
        super(view, activity);
    }

    public void login(final String username, String password) {
        boxHelper.getUserBox().removeAll();
        boxHelper.getConfigureBox().removeAll();
        APIUtils
                .getLoginAPI()
                .login(username, password)
                .compose(getActivity().<HttpResult<User>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<User>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        if (getView() != null) {
                            getView().showLoading();
                        }

                    }

                    @Override
                    public void onNext(@NonNull HttpResult<User> userHttpResult) {
                        if (getView() != null) {
                           // getView().closeLoading();
                            if (userHttpResult.getCode() == 200) {

                                boxHelper.getUserBox().remove(1);
                                boxHelper.getConfigureBox().remove(1);

                                boxHelper.getUserBox().put(userHttpResult.getData());

                                Configure configure = new Configure();
                                configure.setAppRememberCode(userHttpResult.getRemember_code_app());
                                configure.setStudentKH(userHttpResult.getData().getStudentKH());
                                configure.setUserId(userHttpResult.getData().getUser_id());
                                boxHelper.getConfigureBox().put(configure);

                                getToken(userHttpResult.getData().getUser_id(), userHttpResult.getData().getUsername());

                            } else {
                                getView().showMessage(userHttpResult.getMsg());
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
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

    public void getToken(final String userId, String userName){
        APIUtils
                .getMessageAPI()
                .getToken(userId, userName)
                .compose(getActivity().<Token>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Token>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        //getView().showLoading();
                    }

                    @Override
                    public void onNext(@NonNull Token token) {
                        getView().closeLoading();
                        getView().onSuccess();
                        Box<Configure> configureBox = boxHelper.getConfigureBox();
                        Configure first = configureBox.query().equal(Configure_.userId, userId).build().findFirst();
                        if (first != null){
                            first.setToken(token.getToken());
                            configureBox.put(first);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getView().closeLoading();
                        getView().showMessage(ExceptionEngine.handleException(e).getMsg());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}