package cn.nicolite.huthelper.event

import cn.nicolite.huthelper.utils.LogUtils
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.trello.rxlifecycle2.components.support.RxFragment
import com.trello.rxlifecycle2.components.support.RxFragmentActivity
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

/**
 * Created by nicolite on 2018/8/12.
 * email nicolite@nicolite.cn
 */
object RxBus {
    private val bus = PublishSubject.create<Any>().toSerialized()
    private const val TAG = "RxBus"
    private var disposable: Disposable? = null

    fun post(any: Any) {
        bus.onNext(any)
    }

    fun hasObservers(): Boolean {
        return bus.hasObservers()
    }

    fun <T> toObserver(clazz: Class<T>): Observable<T> {
        return bus.ofType(clazz)
    }

    fun toObserver(): Observable<Any> {
        return bus
    }

    fun <T> subscribe(clazz: Class<T>, onNext: (t: T) -> Unit,
                      onError: (t: Throwable) -> Unit = {
                          it.printStackTrace()
                          LogUtils.d(TAG, "subscribe -> ${it.message}")
                      }) {
        disposable = toObserver(clazz).subscribe(onNext, onError)
    }

    fun <T> subscribe(clazz: Class<T>, observer: Observer<in T>) {
        toObserver(clazz).subscribe(observer)
    }

    fun <T> subscribe(clazz: Class<T>, activity: RxAppCompatActivity, onNext: (t: T) -> Unit,
                      onError: (t: Throwable) -> Unit = {
                          it.printStackTrace()
                          LogUtils.d(TAG, "subscribe -> ${it.message}")
                      }) {
        disposable = toObserver(clazz).compose(activity.bindToLifecycle()).subscribe(onNext, onError)
    }

    fun <T> subscribe(clazz: Class<T>, fragmentActivity: RxFragmentActivity, onNext: (t: T) -> Unit,
                      onError: (t: Throwable) -> Unit = {
                          it.printStackTrace()
                          LogUtils.d(TAG, "subscribe -> ${it.message}")
                      }) {
        disposable = toObserver(clazz).compose(fragmentActivity.bindToLifecycle()).subscribe(onNext, onError)
    }

    fun <T> subscribe(clazz: Class<T>, fragment: RxFragment, onNext: (t: T) -> Unit,
                      onError: (t: Throwable) -> Unit = {
                          it.printStackTrace()
                          LogUtils.d(TAG, "subscribe -> ${it.message}")
                      }) {
        disposable = toObserver(clazz).compose(fragment.bindToLifecycle()).subscribe(onNext, onError)
    }

    fun <T, E> subscribe(clazz: Class<T>, lifecycleProvider: LifecycleProvider<E>, onNext: (t: T) -> Unit,
                         onError: (t: Throwable) -> Unit = {
                             it.printStackTrace()
                             LogUtils.d(TAG, "subscribe -> ${it.message}")
                         }) {
        disposable = toObserver(clazz).compose(lifecycleProvider.bindToLifecycle()).subscribe(onNext, onError)
    }

    fun unsubscribe() {
        disposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
    }

}