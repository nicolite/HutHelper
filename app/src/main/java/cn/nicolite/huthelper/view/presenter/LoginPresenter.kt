package cn.nicolite.huthelper.view.presenter


import android.content.Context
import cn.nicolite.huthelper.base.BasePresenter
import cn.nicolite.huthelper.db.dao.ConfigureDao
import cn.nicolite.huthelper.db.dao.UserDao
import cn.nicolite.huthelper.model.bean.Configure
import cn.nicolite.huthelper.model.bean.HttpResult
import cn.nicolite.huthelper.model.bean.User
import cn.nicolite.huthelper.network.APIUtils
import cn.nicolite.huthelper.network.exception.ExceptionEngine
import cn.nicolite.huthelper.utils.ListUtils
import cn.nicolite.huthelper.view.activity.LoginActivity
import cn.nicolite.huthelper.view.iview.ILoginView
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Login Presenter
 * Created by nicolite on 17-10-17.
 */

class LoginPresenter(view: ILoginView, activity: LoginActivity) :
        BasePresenter<ILoginView, LoginActivity>(view, activity) {
    fun login(username: String, password: String) {
        APIUtils
                .getLoginAPI()
                .login()
                .compose(activity.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<HttpResult<User>> {
                    override fun onSubscribe(@NonNull d: Disposable) {
                        getView()?.showLoading()
                    }

                    override fun onNext(@NonNull userHttpResult: HttpResult<User>) {
                        getView()?.closeLoading()
                        when {
                            userHttpResult.code == 200 -> {
                                //保存当前登录用户的userId，用作标识当前登录的用户
                                val editor = activity.getSharedPreferences("login_user", Context.MODE_PRIVATE).edit()
                                editor.putString("userId", userHttpResult.data.user_id)
                                editor.apply()

                                val userDao = daoSession.userDao
                                val userList = userDao.queryBuilder().where(UserDao.Properties.User_id.eq(userHttpResult.data.user_id)).list()
                                if (ListUtils.isEmpty(userList)) {
                                    userDao.insert(userHttpResult.data)
                                } else {
                                    userDao.update(userList[0])
                                }

                                val configureDao = daoSession.configureDao
                                val configureList = configureDao.queryBuilder().where(ConfigureDao.Properties.UserId.eq(userHttpResult.data.user_id)).list()

                                if (ListUtils.isEmpty(configureList)) {
                                    val configure = Configure()
                                    configure.appRememberCode = userHttpResult.remember_code_app
                                    configure.userId = userHttpResult.data.user_id
                                    configure.studentKH = userHttpResult.data.studentKH
                                    configureDao.insert(configure)
                                } else {
                                    val newConfigure = configureList[0]
                                    newConfigure.appRememberCode = userHttpResult.remember_code_app
                                    newConfigure.userId = userHttpResult.data.user_id
                                    newConfigure.studentKH = userHttpResult.data.studentKH
                                    configureDao.update(newConfigure)
                                }
                                getView()?.onSuccess()
                            }
                            userHttpResult.code == 304 -> {
                                getView()?.showMessage("登录失败，密码错误!")
                            }
                            else -> {
                                getView()?.showMessage("登录失败，" + userHttpResult.msg + "，" + userHttpResult.code)
                            }
                        }
                    }

                    override fun onError(@NonNull e: Throwable) {
                        getView()?.closeLoading()
                        getView()?.showMessage("登录失败，" + ExceptionEngine.handleException(e).msg)
                    }

                    override fun onComplete() {
                    }
                })
    }
}