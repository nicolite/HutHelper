package cn.nicolite.huthelper.extend

import android.text.TextUtils

/**
 * Created by nicolite on 2018/7/11.
 * email nicolite@nicolite.cn
 */
fun String.isDigitsOnly(): Boolean {
    return TextUtils.isDigitsOnly(this)
}

fun String.isNotDigitsOnly(): Boolean {
    return !isDigitsOnly()
}