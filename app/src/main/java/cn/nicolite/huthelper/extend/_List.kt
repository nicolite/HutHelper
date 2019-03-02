package cn.nicolite.huthelper.extend

/**
 * Created by nicolite on 2018/6/18.
 * email nicolite@nicolite.cn
 */

fun <E> Collection<E>?.isEmpty(): Boolean {
    return this == null || isEmpty()
}

fun <E> Collection<E>?.isNotEmpty(): Boolean {
    return !isEmpty()
}

