package cn.nicolite.huthelper.network

import cn.nicolite.huthelper.exception.handleAPIException
import com.trello.rxlifecycle2.LifecycleProvider
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by nicolite on 2018/7/8.
 * email nicolite@nicolite.cn
 */

/**
 * RxJava网络请求，带生命周期绑定
 */
fun <T, E> sendRxJavaRequest(observable: Observable<T>, lifecycleProvider: LifecycleProvider<E>, rxRequestListener: RxRequestListener<T>): Disposable {
    return observable.compose(lifecycleProvider.bindToLifecycle())
            .subscribeOn(Schedulers.io())
            .map {
                rxRequestListener.onMap(it)
                return@map it
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                rxRequestListener.onResponse(it)
            }, {
                rxRequestListener.onFailure(handleAPIException(it))
            })
}

/**
 * RxJava网络请求
 */
fun <T> sendRxJavaRequest(observable: Observable<T>, rxRequestListener: RxRequestListener<T>): Disposable {
    return observable.subscribeOn(Schedulers.io())
            .map {
                rxRequestListener.onMap(it)
                return@map it
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                rxRequestListener.onResponse(it)
            }, {
                rxRequestListener.onFailure(handleAPIException(it))
            })
}

