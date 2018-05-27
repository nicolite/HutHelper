package cn.nicolite.huthelper.presenter;


import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

import cn.nicolite.huthelper.base.BasePresenter;
import cn.nicolite.huthelper.db.dao.ConfigureDao;
import cn.nicolite.huthelper.db.dao.UserDao;
import cn.nicolite.huthelper.model.bean.Configure;
import cn.nicolite.huthelper.model.bean.HttpResult;
import cn.nicolite.huthelper.model.bean.User;
import cn.nicolite.huthelper.network.APIUtils;
import cn.nicolite.huthelper.network.exception.ExceptionEngine;
import cn.nicolite.huthelper.utils.ListUtils;
import cn.nicolite.huthelper.utils.LogUtils;
import cn.nicolite.huthelper.view.activity.LoginActivity;
import cn.nicolite.huthelper.view.iview.ILoginView;
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
//        String env = EncryptUtils.RSAPublicKeyEncrypt(password, Constants.rsaPublicKey);
//        LogUtils.d(TAG, "xx " + env);
        APIUtils.INSTANCE
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
                                //保存当前登录用户的userId，用作标识当前登录的用户
                                SharedPreferences.Editor editor = getActivity().getSharedPreferences("login_user", Context.MODE_PRIVATE).edit();
                                editor.putString("userId", userHttpResult.getData().getUser_id());
                                editor.apply();

                                LogUtils.d(TAG, "appCode: " + userHttpResult.getRemember_code_app());
                                LogUtils.d(TAG, "studentKH：" + userHttpResult.getData().getStudentKH());
                                UserDao userDao = daoSession.getUserDao();
                                List<User> userList = userDao.queryBuilder().where(UserDao.Properties.User_id.eq(userHttpResult.getData().getUser_id())).list();
                                if (ListUtils.isEmpty(userList)) {
                                    userDao.insert(userHttpResult.getData());
                                } else {
                                    userDao.update(userList.get(0));
                                }

                                ConfigureDao configureDao = daoSession.getConfigureDao();
                                List<Configure> configureList = configureDao.queryBuilder().where(ConfigureDao.Properties.UserId.eq(userHttpResult.getData().getUser_id())).list();

                                if (ListUtils.isEmpty(configureList)) {
                                    Configure configure = new Configure();
                                    configure.setAppRememberCode(userHttpResult.getRemember_code_app());
                                    configure.setUserId(userHttpResult.getData().getUser_id());
                                    configure.setStudentKH(userHttpResult.getData().getStudentKH());
                                    configureDao.insert(configure);
                                } else {
                                    Configure newConfigure = configureList.get(0);
                                    newConfigure.setAppRememberCode(userHttpResult.getRemember_code_app());
                                    newConfigure.setUserId(userHttpResult.getData().getUser_id());
                                    newConfigure.setStudentKH(userHttpResult.getData().getStudentKH());
                                    configureDao.update(newConfigure);
                                }

                                getToken(userHttpResult.getData().getUser_id(), userHttpResult.getData().getUsername());

                            } else if (userHttpResult.getCode() == 304) {
                                getView().closeLoading();
                                getView().showMessage("登录失败，密码错误!");
                            } else {
                                getView().closeLoading();
                                getView().showMessage("登录失败，" + userHttpResult.getMsg() + "，" + userHttpResult.getCode());
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (getView() != null) {
                            getView().closeLoading();
                            getView().showMessage("登录失败，" + ExceptionEngine.handleException(e).getMsg());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getToken(final String userId, String userName) {

        /* APIUtils
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
                        if (getView() != null) {
                            ConfigureDao configureDao = daoSession.getConfigureDao();
                            List<Configure> list = configureDao.queryBuilder().where(ConfigureDao.Properties.UserId.eq(userId)).list();
                            if (!ListUtils.isEmpty(list)) {
                                Configure configure = list.get(0);
                                configure.setToken(token.getToken());
                                configureDao.update(configure);
                            }

                            getView().onSuccess();
                            getView().closeLoading();
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
                }); */

        //移除融云后暂时这样，接入其它IM后删除
        if (getView() != null) {
            ConfigureDao configureDao = daoSession.getConfigureDao();
            List<Configure> list = configureDao.queryBuilder().where(ConfigureDao.Properties.UserId.eq(userId)).list();
            if (!ListUtils.isEmpty(list)) {
                Configure configure = list.get(0);
                configure.setToken("*");
                configureDao.update(configure);
            }

            getView().onSuccess();
            getView().closeLoading();
        }
    }
}