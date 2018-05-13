package cn.nicolite.huthelper.network.observer;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import cn.nicolite.huthelper.network.function.HttpResultFunction;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Retrofit网络请求监听类Observable(被监听者)
 * Created by nicolite on 17-10-17.
 */

public class HttpRxObservable {

    /**
     * 获取被监听者
     * 备注:网络请求Observable构建
     * 无管理生命周期,容易导致内存溢出
     * @param observable
     * @return Observable
     */
    public static<T> Observable<T> getObservable(Observable<T> observable){
        return observable
                .onErrorResumeNext(new HttpResultFunction<T>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取被监听者
     * 备注:网络请求Observable构建
     * 传入LifecycleProvider自动管理生命周期,避免内存溢出
     * 备注:需要继承RxActivity.../RxFragment...
     * @param observable
     * @param lifecycleProvider
     * @return
     */
    public static<T> Observable getObservable(Observable<T> observable,
                                              LifecycleProvider<T> lifecycleProvider){
        if (lifecycleProvider != null){
            return observable
                    .onErrorResumeNext(new HttpResultFunction<T>())
                    .compose(lifecycleProvider.bindToLifecycle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }else {
            return getObservable(observable);
        }
    }

    /**
     * 获取被监听者
     * 备注:网络请求Observable构建
     * 传入LifecycleProvider<ActivityEvent>手动管理生命周期,避免内存溢出
     * 备注:需要继承RxActivity,RxAppCompatActivity,RxFragmentActivity
     * @param observable
     * @param lifecycleProvider
     * @param activityEvent
     * @return
     */

    public static<T> Observable getObservable(Observable<T> observable,
                                              LifecycleProvider<ActivityEvent> lifecycleProvider,
                                              ActivityEvent activityEvent){
        if (lifecycleProvider != null){
            return observable
                    .onErrorResumeNext(new HttpResultFunction<T>())
                    .compose(lifecycleProvider.bindUntilEvent(activityEvent))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }else {
            return getObservable(observable);
        }
    }

    public static<T> Observable getObservable(Observable<T> observable,
                                              LifecycleProvider<FragmentEvent> lifecycleProvider,
                                              FragmentEvent fragmentEvent){
        if (lifecycleProvider != null){
            return observable
                    .onErrorResumeNext(new HttpResultFunction<T>())
                    .compose(lifecycleProvider.bindUntilEvent(fragmentEvent))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }else {
            return getObservable(observable);
        }
    }


}
