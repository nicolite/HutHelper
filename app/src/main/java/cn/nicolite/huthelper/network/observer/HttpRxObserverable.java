package cn.nicolite.huthelper.network.observer;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Retrofit网络请求监听类Observable(被监听者)
 * Created by nicolite on 17-10-17.
 */

public class HttpRxObserverable {

    /**
     * 获取被监听者
     * 备注:网络请求Observable构建
     * 无管理生命周期,容易导致内存溢出
     * @param observable
     * @return Observable
     */
    public static Observable<?> getObservable(Observable<?> observable){
        return observable
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
    public static Observable<?> getObservable(Observable<?> observable,
                                              LifecycleProvider<?> lifecycleProvider){
        if (lifecycleProvider != null){
            return observable
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

    public static Observable<?> getObservable(Observable<?> observable,
                                              LifecycleProvider<ActivityEvent> lifecycleProvider,
                                              ActivityEvent activityEvent){
        if (lifecycleProvider != null){
            return observable
                    .compose(lifecycleProvider.bindUntilEvent(activityEvent))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }else {
            return getObservable(observable);
        }
    }

    public static Observable<?> getObservable(Observable<?> observable,
                                              LifecycleProvider<FragmentEvent> lifecycleProvider,
                                              FragmentEvent fragmentEvent){
        if (lifecycleProvider != null){
            return observable
                    .compose(lifecycleProvider.bindUntilEvent(fragmentEvent))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }else {
            return getObservable(observable);
        }
    }


}
