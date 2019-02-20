package cn.nicolite.huthelper.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import cn.nicolite.huthelper.okhttp3.DownloadSchedule
import com.messy.swipebackhelper.removeInParent
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class ImageWrapper(imageView: ImageView) : FrameLayout(imageView.context) {

    //    private var textView: TextView
    private var progressBar: ProgressBar

    init {
        val parent = imageView.parent as ViewGroup
        val params = imageView.layoutParams
        this.layoutParams = ViewGroup.LayoutParams(params.width, params.height)
//        textView = TextView(context).apply {
//            layoutParams = FrameLayout.LayoutParams(params.width, params.height)
//            gravity = Gravity.CENTER
//            background = ColorDrawable(Color.WHITE).apply { alpha = (255 * 0.3f).toInt() }
//        }
        progressBar = ProgressBar(context).apply {
            layoutParams = FrameLayout.LayoutParams(params.width, params.height)
            background = ColorDrawable(Color.WHITE).apply { alpha = (255 * 0.3f).toInt() }
            max = 100
        }
        imageView.removeInParent()
        addView(imageView)
        addView(progressBar)
//        addView(textView)
        parent.addView(this)
    }

    fun bindTask(task: String) {
        bindTask(DownloadSchedule.query(task))
    }

    @SuppressLint("CheckResult")
    fun bindTask(task: Observable<DownloadSchedule.Config>) {
        task.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<DownloadSchedule.Config> {
                    override fun onComplete() {
                        setProgressGone()
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(config: DownloadSchedule.Config) {
                        setProgress(config.progress)
                    }

                    override fun onError(e: Throwable) {
                        error()
                    }

                })

    }

    fun setProgress(progress: Int) {
        progressBar.progress = progress
    }

    fun setProgressGone() {
        progressBar.visibility = View.GONE
    }

    fun error() {
        setProgressGone()
    }
}