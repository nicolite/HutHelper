package cn.nicolite.huthelper.network

import cn.nicolite.huthelper.exception.APIException


/**
 * Created by nicolite on 2018/8/5.
 * email nicolite@nicolite.cn
 */
interface RxRequestListener<T> {
    fun onMap(data: T)
    fun onResponse(data: T)
    fun onFailure(exception: APIException)
}

interface HttpRequestListener<T> : RxRequestListener<T> {
    override fun onMap(data: T) {}
}
