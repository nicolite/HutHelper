package cn.nicolite.huthelper.okhttp3

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.ConcurrentHashMap

/*
* 图片加载进度查询单例类
* */
object DownloadSchedule {

    /*
    * task：下载url，即任务的唯一标识
    * bytesRead：已下载的字节
    * contentLength：文件大小
    * */
    data class Config(val task: String, val bytesRead: Long, val contentLength: Long) {
        val isDone get() = bytesRead == contentLength
        val isStart get() = contentLength != -1L
        val progress get() = (bytesRead * 100 / contentLength).toInt()
    }

    private val tasks = ConcurrentHashMap<String, BehaviorSubject<Config>>()

    /*
    * 更新图片下载进度，应只有本模块内调用
    * */
    @JvmStatic
    fun update(task: String, bytesRead: Long, contentLength: Long) {
        if (tasks[task] == null)
            tasks[task] = BehaviorSubject.createDefault(Config(task, bytesRead, contentLength))
        else {
            tasks[task]!!.onNext(Config(task, bytesRead, contentLength))
        }
    }

    /*
    * 通过url查询一个下载
    * */
    @JvmStatic
    fun query(task: String): Observable<Config> {
        if (tasks[task] == null)
            tasks[task] = BehaviorSubject.createDefault(Config(task, 0, -1L))
        return tasks[task]!!.hide()
    }

    /*
    * 移除不再使用的任务
    * */
    @JvmStatic
    fun remove(task: String) {
        tasks.remove(task)
    }
}