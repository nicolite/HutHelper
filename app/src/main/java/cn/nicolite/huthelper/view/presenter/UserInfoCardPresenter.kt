package cn.nicolite.huthelper.view.presenter

import cn.nicolite.huthelper.base.BasePresenter
import cn.nicolite.huthelper.model.bean.HttpResult
import cn.nicolite.huthelper.model.bean.User
import cn.nicolite.huthelper.network.APIUtils
import cn.nicolite.huthelper.view.activity.UserInfoCardActivity
import cn.nicolite.huthelper.view.iview.IUserInfoCardView
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by nicolite on 17-11-12.
 */

class UserInfoCardPresenter(view: IUserInfoCardView, activity: UserInfoCardActivity) :
        BasePresenter<IUserInfoCardView, UserInfoCardActivity>(view, activity) {

    fun showInfo(userId: String) {
        APIUtils
                .getUserAPI()
                .userInfo
                .compose(activity.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<HttpResult<User>> {
                    override fun onSubscribe(d: Disposable) {
                        getView()?.showLoading()
                    }

                    override fun onNext(userHttpResult: HttpResult<User>) {
                        getView()?.closeLoading()
                        if (userHttpResult.code == 200) {
                            getView()?.showInfo(userHttpResult.data)
                        } else {
                            getView()?.showMessage("获取信息失败，" + userHttpResult.code)
                        }

                    }

                    override fun onError(e: Throwable) {
                        getView()?.closeLoading()
                    }

                    override fun onComplete() {

                    }
                })
    }
}
