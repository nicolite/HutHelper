package cn.nicolite.huthelper.network.observer;

import io.reactivex.Observer;

/**
 * //TODO
 * Retrofit网络请求监听类Observable(监听者)
 * 备注:
 * 1.重写onSubscribe，添加请求标识
 * 2.重写onError，封装错误/异常处理，移除请求
 * 3.重写onNext，移除请求
 * 4.重写cancel，取消请求
 * Created by nicolite on 17-10-17.
 */

public abstract class HttpRxObserver<T> implements Observer<T> {

}
