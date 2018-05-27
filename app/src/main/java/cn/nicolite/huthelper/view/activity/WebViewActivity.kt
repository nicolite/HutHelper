package cn.nicolite.huthelper.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageButton
import android.widget.LinearLayout
import cn.nicolite.huthelper.R
import cn.nicolite.huthelper.injection.JsInject
import cn.nicolite.huthelper.kBase.BaseActivity
import cn.nicolite.huthelper.model.Constants
import cn.nicolite.huthelper.network.exception.ExceptionEngine
import cn.nicolite.huthelper.utils.CommUtil
import cn.nicolite.huthelper.utils.LogUtils
import cn.nicolite.huthelper.utils.SnackbarUtils
import cn.nicolite.huthelper.utils.ToastUtils
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_webview.*
import kotlinx.android.synthetic.main.toolbar_nomenu.*
import org.jsoup.Jsoup
import java.net.URISyntaxException

/**
 * Created by nicolite on 17-12-17.
 */

class WebViewActivity : BaseActivity() {

    private var type: Int = 0
    private var url: String = ""
    private var title: String = ""
    private lateinit var settings: WebSettings
    private lateinit var mErrorView: View
    private var mIsErrorPage: Boolean = false

    companion object {
        const val TYPE_CHANGE_PWD = 561
        const val TYPE_HELP = 334
        const val TYPE_PERMISSION = 545
        const val TYPE_LIBRARY = 278
        const val TYPE_HOMEWORK = 576
        const val TYPE_NOTICE = 174
        const val TYPE_FRESHMAN_GUIDE = 519
        const val TYPE_BLOG = 434
        const val TYPE_OPEN_SOURCE = 886
    }

    override fun initConfig(savedInstanceState: Bundle?) {
        setImmersiveStatusBar()
        setDeepColorStatusBar()
        setSlideExit()
        setPixelFormat()
    }

    override fun initBundleData(bundle: Bundle?) {
        if (bundle != null) {
            type = bundle.getInt("type", -1)
            url = bundle.getString("url", "")
            title = bundle.getString("title", "")
            if (type == -1 || TextUtils.isEmpty(url) || TextUtils.isEmpty(title)) {
                ToastUtils.showToastShort("获取数据失败！")
                finish()
            }
        }
    }

    override fun setLayoutId(): Int {
        return R.layout.activity_webview
    }

    override fun doBusiness() {
        toolbar_title.text = ""
        toolbar.setOnClickListener { finish() }

        initWebView()
        loadHtml(type, title, url)
    }

    private fun initWebView() {

        settings = webView.settings

        settings.apply {
            loadWithOverviewMode = true
            builtInZoomControls = true// 使页面支持缩放
            setSupportZoom(true) //支持缩放
            displayZoomControls = false// 缩放按钮
            loadsImagesAutomatically = true//支持自动加载图片
            domStorageEnabled = true //开启DOM
            cacheMode = WebSettings.LOAD_NO_CACHE//关闭webview中缓存
            setAppCacheEnabled(true)
            layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
            defaultTextEncodingName = "UTF-8"
            useWideViewPort = true  //将图片调整到适合webview的大小
            setNeedInitialFocus(true) ////当webview调用requestFocus时为webview设置节点
            allowFileAccess = true// 允许访问文件
            pluginState = WebSettings.PluginState.ON

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
        }

        webView.apply {
            isFocusable = true
            isFocusableInTouchMode = true
            addJavascriptInterface(JsInject(context), "imageListener")
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(webView: WebView, s: String): Boolean {

                    if (s.startsWith("http") || s.startsWith("https")) {
                        webView.loadUrl(s)
                    } else if (s.startsWith("intent")) {
                        try {
                            val intent = Intent.parseUri(s, Intent.URI_INTENT_SCHEME)
                            intent.addCategory("android.intent.category.BROWSABLE")
                            intent.component = null
                            intent.selector = null
                            val resolves = context.packageManager.queryIntentActivities(intent, 0)
                            if (resolves.size > 0) {
                                startActivityIfNeeded(intent, -1)
                            }
                            return true
                        } catch (e: URISyntaxException) {
                            e.printStackTrace()
                        }

                    } else {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(s))
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivity(intent)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                        return true
                    }
                    return super.shouldOverrideUrlLoading(webView, s)
                }

                override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
                    super.onReceivedError(view, errorCode, description, failingUrl)
                    showErrorPage()
                }
            }

            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(webView: WebView, i: Int) {
                    super.onProgressChanged(webView, i)
                    if (progressBar != null) {
                        if (i == 100) {
                            progressBar.visibility = View.GONE
                            if (type != TYPE_CHANGE_PWD) {
                                addImgClickListener()
                            }
                        } else {
                            progressBar.progress = i
                        }
                    }
                }
            }
        }
    }


    private fun addImgClickListener() {
        webView?.loadUrl("""javascript:(function(){
                var objs = document.getElementsByTagName("img");
                for(var i=0;i<objs.length;i++){
                      objs[i].onclick=function(){
                      window.imageListener.showImage(this.src);
                     }
                   }
                })()""")
    }

    private fun changThemeMusicDom() {
        webView?.loadUrl("""javascript:(function(){
                var footer = document.getElementsByTagName("footer");
                var headerc = document.getElementsByTagName("header");
                var containers = document.getElementsByTagName("container");

                footer[0].parentNode.removeChild(footer[0]);
                headerc[0].parentNode.removeChild(headerc[0]);
                containers[0].style.paddingTop = 0;

                var header = document.getElementsByClassName("Wall album-wall");
                header[0].parentNode.removeChild(header[0]);
                })()""")

        webView?.visibility = View.VISIBLE
    }

    private fun loadHtml(type: Int, title: String, url: String) {
        LogUtils.d(TAG, "webView $url")
        when (type) {
            TYPE_CHANGE_PWD -> {
                toolbar_title.text = title
                webView.loadUrl(url)
            }
            TYPE_HELP -> {
                toolbar_title.text = title
                loadContent(url)
            }
            TYPE_PERMISSION -> {
                toolbar_title.text = title
                loadContent(url)
            }
            TYPE_LIBRARY -> {
                toolbar_title.text = title
                webView.loadUrl(url)
            }
            TYPE_HOMEWORK -> {
                toolbar_title.text = title
                webView.loadUrl(url)
            }
            TYPE_FRESHMAN_GUIDE -> {
                toolbar_title.text = title
                loadContent(url)
            }
            TYPE_NOTICE -> {
                toolbar_title.text = title
                webView.loadUrl(url)
            }
            TYPE_BLOG -> {
                toolbar_title.text = title
                webView.loadUrl(url)
            }
            TYPE_OPEN_SOURCE -> {
                toolbar_title.text = title
                webView.loadUrl(url)
            }
            else -> SnackbarUtils.showShortSnackbar(rootView, "未知类型")
        }
    }

    override fun onResume() {
        super.onResume()
        settings.javaScriptEnabled = true
        webView.resumeTimers()
        webView.onResume()
    }

    override fun onPause() {
        super.onPause()
        settings.javaScriptEnabled = false
        webView.onPause()
        webView.pauseTimers()
    }

    override fun onStop() {
        super.onStop()
        settings.javaScriptEnabled = false
    }

    override fun onDestroy() {
        super.onDestroy()
        webView.loadDataWithBaseURL(null, "about:blank", "text/html", "utf-8", null)
        webView.clearHistory()
        webView.clearCache(true)
        webView.clearFormData()
        webView.removeAllViews()
        rootView.removeView(webView)
        webView.destroy()

    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    protected fun initErrorPage() {
        if (mErrorView == null) {
            mErrorView = View.inflate(this, R.layout.online_error, null)
            val button = mErrorView.findViewById(R.id.btn_refer) as ImageButton
            button.setOnClickListener {
                if (!CommUtil.isOnline(applicationContext)) {
                    SnackbarUtils.showShortSnackbar(rootView, "网络不可用！")
                }
                hideErrorPage()
                webView.reload()
            }
            mErrorView.setOnClickListener(null)
        }
    }

    /**
     * 显示自定义错误提示页面，用一个View覆盖在WebView
     */
    protected fun showErrorPage() {
        if (webView == null) return
        val webParentView = webView.parent as LinearLayout
        initErrorPage()
        webParentView.removeAllViews()
//        while (webParentView.childCount > 1) {
//            webParentView.removeViewAt(1)
//        }
        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        webParentView.addView(mErrorView, lp)
        mIsErrorPage = true

    }

    protected fun hideErrorPage() {
        val webParentView = mErrorView.parent as LinearLayout
        webParentView.removeAllViews()
//        while (webParentView.childCount > 1) {
//            webParentView.removeViewAt(1)
//        }
        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        webParentView.addView(webView, lp)
        mIsErrorPage = false
    }

    fun loadContent(url: String) {
        Observable.create(ObservableOnSubscribe<String> { e ->
            val document = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko)Chrome/50.0.2661.102 Safari/537.36")
                    .get()
            val head = document.head()
            val body = document.body()

            val content = document.getElementsByClass("content")
            val p = content.select("p")
            p.first().remove()
            val span = content.select("span")

            for (element in p) {
                element.removeAttr("style")
            }

            for (element in span) {
                element.removeAttr("style")
            }

            content.select("br").remove()
            val img = content.select("img[src]")
            for (element in img) {
                var src = element.attr("abs:src")
                src = src.replace("http://172.16.10.210", Constants.ARTICLE_BASE_URL)
                src = src.replace("http://love.zengheng.top:8888", Constants.ARTICLE_BASE_URL)

                element.attr("src", src)
                element.removeAttr("_src")
                element.removeAttr("style")
            }

            for (element in content.select("a")) {
                if (element.text() == "<< 返回首页") {
                    element.remove()
                }
            }

            for (element in content.select("pre")) {
                element.removeAttr("style")
            }

            val a = content.select("a")
            for (element in a) {
                if (element.attr("href").startsWith("http://shang.qq.com/wpa/qunwpa?idkey=")) {
                    element.remove()
                }
            }

            val h1 = content.select("h1")

            for (element in h1) {
                element.removeAttr("style")
            }

            val h2 = content.select("h2")
            for (element in h2) {
                element.removeAttr("style")
            }

            val h3 = content.select("h3")
            for (element in h3) {
                element.removeAttr("style")
            }

            val h4 = content.select("h4")
            for (element in h4) {
                element.removeAttr("style")
            }

            val h5 = content.select("h5")
            for (element in h5) {
                element.removeAttr("style")
            }

            val h6 = content.select("h6")
            for (element in h6) {
                element.removeAttr("style")
            }

            if (type == TYPE_FRESHMAN_GUIDE && title == "开篇") {
                for (element in p) {
                    if (element.text() == "目录直达链接（持续更新）："
                            || element.text() == "新生攻略手册--转专业篇不可抗力下架"
                            || element.text() == "新生攻略手册--素拓篇不可抗力下架"
                            || element.text().contains("严禁抄袭!")) {
                        element.remove()
                    }
                }
                a.remove()
            }
            //head.empty();
            //head.append("<meta charset=\"utf-8\">" +
            //        "<script src=\"http://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js\""+
            //        " type=\"text/javascript\"></script>");
            body.empty()
            body.append(content.toString())
            val html = document.toString()
                    .replace("::selection{background:#f4645f;}", "")
                    .replace("::-moz-selection { background:#f4645f; }", "img{width: 100%; height: 100%; object-fit: contain}")
                    .replace("background: #333;", "margin: 0px 15px 0px 15px;")
                    .replace("<p><span>&nbsp;</span></p>", "")
                    .replace("<p><strong><span>&nbsp;</span></strong></p>", "")

            e.onNext(html)
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<String> {
                    override fun onSubscribe(d: Disposable) {}

                    override fun onNext(s: String) {
                        if (TextUtils.isEmpty(s)) {
                            SnackbarUtils.showShortSnackbar(rootView, "获取数据失败！")
                            return
                        }
                        if (webView != null) {
                            webView.loadDataWithBaseURL(null, s, "text/html", "UTF-8", null)
                        }
                    }

                    override fun onError(e: Throwable) {
                        SnackbarUtils.showShortSnackbar(rootView, ExceptionEngine.handleException(e).msg)
                    }

                    override fun onComplete() {

                    }
                })

    }
}
