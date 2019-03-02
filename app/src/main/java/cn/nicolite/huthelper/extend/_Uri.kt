package cn.nicolite.huthelper.extend

import android.net.Uri
import java.io.File
import java.net.URI
import java.net.URISyntaxException


/**
 * Created by nicolite on 2018/10/17.
 * email nicolite@nicolite.cn
 */

fun Uri.toFile(): File? {
    var file: File? = null
    try {
        file = File(URI(this.toString()))
    } catch (e: URISyntaxException) {
        e.printStackTrace()
    }
    return file
}