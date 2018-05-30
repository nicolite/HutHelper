package cn.nicolite.huthelper.presenter

import cn.nicolite.huthelper.kBase.BasePresenter
import cn.nicolite.huthelper.model.Constants
import cn.nicolite.huthelper.model.bean.FreshmanGuide
import cn.nicolite.huthelper.network.exception.ExceptionEngine
import cn.nicolite.huthelper.view.fragment.FreshmanGuideFragment
import cn.nicolite.huthelper.view.iview.IFreshmanGuideView
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jsoup.Jsoup

/**
 * Created by nicolite on 17-11-13.
 */

class FreshmanGuidePresenter(iView: IFreshmanGuideView, view: FreshmanGuideFragment) : BasePresenter<IFreshmanGuideView, FreshmanGuideFragment>(iView, view) {

    fun showGuideList() {
        Observable.create(ObservableOnSubscribe<List<FreshmanGuide>> { e ->
            val document = Jsoup.connect(Constants.FRESHMANGUIDE_URL)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko)Chrome/50.0.2661.102 Safari/537.36")
                    .get()
            val content = document.getElementsByClass("content")
            content.select("p").first().remove()
            val a = content.select("a")
            val dataList = ArrayList<FreshmanGuide>()
            for (element in a) {
                if (element.text() == "校内网全篇pdf下载" || element.text() == "<< 返回首页") {
                    continue
                }
                val freshmanHelpData = FreshmanGuide()
                freshmanHelpData.title = element.text()
                freshmanHelpData.url = element.attr("abs:href")
                dataList.add(freshmanHelpData)
            }
            e.onNext(dataList)
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<List<FreshmanGuide>> {
                    override fun onSubscribe(d: Disposable) {
                        getIView()?.showLoading()
                    }

                    override fun onNext(freshmanGuides: List<FreshmanGuide>) {
                        getIView()?.closeLoading()
                        getIView()?.showGuideList(freshmanGuides)
                    }

                    override fun onError(e: Throwable) {
                        getIView()?.closeLoading()
                        getIView()?.showMessage(ExceptionEngine.handleException(e).msg)
                    }

                    override fun onComplete() {

                    }
                })
    }

}
