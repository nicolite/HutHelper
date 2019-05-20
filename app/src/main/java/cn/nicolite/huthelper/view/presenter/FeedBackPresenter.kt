package cn.nicolite.huthelper.view.presenter

import android.content.pm.PackageManager
import android.os.Build
import android.text.TextUtils
import cn.nicolite.huthelper.base.BasePresenter
import cn.nicolite.huthelper.network.APIUtils
import cn.nicolite.huthelper.network.exception.ExceptionEngine
import cn.nicolite.huthelper.view.activity.FeedBackActivity
import cn.nicolite.huthelper.view.iview.IFeedBackView
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody

/**
 * Created by nicolite on 17-10-24.
 */

class FeedBackPresenter(view: IFeedBackView, activity: FeedBackActivity) : BasePresenter<IFeedBackView, FeedBackActivity>(view, activity) {

    fun feeBack(content: String, contact: String) {
        val user = configure.user
        when {
            TextUtils.isEmpty(content) -> getView()?.showMessage("反馈意见不能为空！")
            contact.length > 200 -> getView()?.showMessage("字数超过限制！")
            TextUtils.isEmpty(contact) -> getView()?.showMessage("联系方式不能为空！")
            else -> {
                var version = ""
                var model = ""
                try {
                    val pm = activity.packageManager
                    val pi = pm.getPackageInfo(activity.packageName, PackageManager.GET_ACTIVITIES)
                    version = "版本：Android " + pi.versionName + "（" + pi.versionCode + "）"
                    model = "机型：" + Build.MANUFACTURER + Build.MODEL + "（Android " + Build.VERSION.RELEASE + "）"
                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()
                }

                val from = "来源：" + user.trueName + " " + user.class_name + " " + user.studentKH

                val finalContent = "$from<br/>$version<br/>$model<br/>内容：$content"

                APIUtils
                        .getFeedBackAPI()
                        .feedBack()
                        .compose(activity.bindToLifecycle())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<ResponseBody> {
                            override fun onSubscribe(@NonNull d: Disposable) {
                                getView()?.showLoading()
                            }

                            override fun onNext(@NonNull responseBody: ResponseBody) {
                                getView()?.closeLoading()
                                getView()?.onSuccess()
                            }

                            override fun onError(@NonNull e: Throwable) {
                                getView()?.closeLoading()
                                getView()?.showMessage(ExceptionEngine.handleException(e).msg)
                            }

                            override fun onComplete() {

                            }
                        })
            }
        }
    }
}
