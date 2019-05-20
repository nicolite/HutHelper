package cn.nicolite.huthelper.view.presenter

import cn.nicolite.huthelper.base.BasePresenter
import cn.nicolite.huthelper.model.bean.HttpResult
import cn.nicolite.huthelper.model.bean.User
import cn.nicolite.huthelper.network.APIUtils
import cn.nicolite.huthelper.network.exception.ExceptionEngine
import cn.nicolite.huthelper.view.fragment.UserListFragment
import cn.nicolite.huthelper.view.iview.IUserListView
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by nicolite on 17-11-11.
 */

class UserListPresenter(view: IUserListView, activity: UserListFragment) : BasePresenter<IUserListView, UserListFragment>(view, activity) {

    fun showUsers(name: String) {

        APIUtils
                .getUserAPI()
                .students
                .compose(activity.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<HttpResult<List<User>>> {
                    override fun onSubscribe(d: Disposable) {
                        getView()?.showLoading()
                    }

                    override fun onNext(listHttpResult: HttpResult<List<User>>) {
                        getView()?.closeLoading()
                        if (listHttpResult.code == 200) {
                            getView()?.showUsers(listHttpResult.data)
                        } else {
                            getView()?.loadFailure()
                        }
                    }

                    override fun onError(e: Throwable) {
                        getView()?.closeLoading()
                        getView()?.loadFailure()
                        getView()?.showMessage(ExceptionEngine.handleException(e).msg)
                    }

                    override fun onComplete() {

                    }
                })
    }
}
