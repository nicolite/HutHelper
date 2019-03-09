package cn.nicolite.huthelper.network

import cn.nicolite.huthelper.exception.handleAPIException
import com.trello.rxlifecycle2.LifecycleProvider
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import java.io.IOException

/**
 * Created by nicolite on 17-6-25.
 * 网络连接方法
 */

/**
 * 采用OkHttp请求
 *
 * @param address  地址
 * @param httpRequestListener 回调
 */
fun sendOkHttpRequest(address: String, httpRequestListener: HttpRequestListener<Response?>) {
    val client = OkHttpClient()
    val request = Request.Builder()
            .url(address)
            .build()
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call?, e: IOException?) {
            if (e != null) {
                httpRequestListener.onFailure(handleAPIException(e))
            }
        }

        override fun onResponse(call: Call?, response: Response?) {
            httpRequestListener.onResponse(response)
        }
    })
}

/**
 * 使用retrofit进行网络请求，异步
 */
fun <T> sendRetrofitRequest(call: retrofit2.Call<T>, httpRequestListener: HttpRequestListener<retrofit2.Response<T>?>) {
    call.enqueue(object : retrofit2.Callback<T> {
        override fun onFailure(call: retrofit2.Call<T>?, t: Throwable?) {
            if (t != null) {
                httpRequestListener.onFailure(handleAPIException(t))
            }
        }

        override fun onResponse(call: retrofit2.Call<T>?, response: retrofit2.Response<T>?) {
            httpRequestListener.onResponse(response)
        }
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