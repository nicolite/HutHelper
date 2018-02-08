package cn.nicolite.huthelper.network.function;

import cn.nicolite.huthelper.network.exception.ExceptionEngine;
import cn.nicolite.huthelper.utils.LogUtils;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Http错误/异常处理
 * Created by nicolite on 17-10-17.
 */

public class HttpResultFunction<T> implements Function<Throwable, Observable<T>> {
    private static final String TAG = "HttpResultFunction";
    @Override
    public Observable<T> apply(@NonNull Throwable throwable) throws Exception {
        LogUtils.d(TAG, TAG + " " + throwable.toString());
        return Observable.error(ExceptionEngine.handleException(throwable));
    }
}
