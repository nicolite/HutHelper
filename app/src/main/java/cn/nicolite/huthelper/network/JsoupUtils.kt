package cn.nicolite.huthelper.network

import cn.nicolite.huthelper.exception.handleAPIException
import cn.nicolite.mvp.utils.LogUtils
import com.trello.rxlifecycle2.LifecycleProvider
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

/**
 * Created by nicolite on 2018/7/8.
 * email nicolite@nicolite.cn
 */

/**
 * Jsoup抓取网页用，采用RxJava异步，绑定生命周期
 *
 */
fun <E> sendJsoupRequest(address: String, lifecycleProvider: LifecycleProvider<E>, rxRequestListener: RxRequestListener<Document>): Disposable {
    return Observable.create(ObservableOnSubscribe<Document> { e ->
        try {
            val document = getDocument(address)
            rxRequestListener.onMap(document)
            e.onNext(document)
        } catch (exception: Exception) {
            e.onError(exception)
        }
    })
            .compose(lifecycleProvider.bindToLifecycle())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                rxRequestListener.onResponse(it)
            }, {
                rxRequestListener.onFailure(handleAPIException(it))
            })
}

/**
 * Jsoup抓取网页用，采用RxJava异步
 *
 */
fun sendJsoupRequest(address: String, rxRequestListener: RxRequestListener<Document>): Disposable {
    return Observable.create(ObservableOnSubscribe<Document> { e ->
        try {
            val document = getDocument(address)
            rxRequestListener.onMap(document)
            e.onNext(document)
        } catch (exception: Exception) {
            e.onError(exception)
        }
    })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                rxRequestListener.onResponse(it)
            }, {
                rxRequestListener.onFailure(handleAPIException(it))
            })
}

fun getDocument(address: String): Document {
    try {
        LogUtils.d("Jsoup", "address -> $address")
        return Jsoup.connect(address)
                .headers(mapOf("Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
                        "Accept-Encoding" to "gzip, deflate",
                        "Accept-Language" to "zh-CN,zh;q=0.9",
                        "Connection" to "keep-alive",
                        "Cache-Control" to " no-cache"))
                .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36")
                .ignoreHttpErrors(true)
                .maxBodySize(0)
                .get()
    } catch (exception: Exception) {
        throw exception
    }
}