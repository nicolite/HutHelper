package cn.nicolite.huthelper.view.activity;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URISyntaxException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.BaseActivity;
import cn.nicolite.huthelper.injection.JsInject;
import cn.nicolite.huthelper.model.Constants;
import cn.nicolite.huthelper.network.exception.ExceptionEngine;
import cn.nicolite.huthelper.utils.CommUtil;
import cn.nicolite.huthelper.utils.LogUtils;
import cn.nicolite.huthelper.utils.SnackbarUtils;
import cn.nicolite.huthelper.utils.ToastUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 通用webView
 * Created by nicolite on 17-10-17.
 */

public class WebViewActivity extends BaseActivity {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.rootView)
    LinearLayout rootView;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    private int type;
    private String url;
    private String title;
    private WebSettings settings;
    private View mErrorView;
    private boolean mIsErrorPage;

    public static final int TYPE_CHANGE_PWD = 561;
    public static final int TYPE_HELP = 334;
    public static final int TYPE_PERMISSION = 545;
    public static final int TYPE_LIBRARY = 278;
    public static final int TYPE_HOMEWORK = 576;
    public static final int TYPE_NOTICE = 174;
    public static final int TYPE_FRESHMAN_GUIDE = 519;
    public static final int TYPE_BLOG = 434;
    public static final int TYPE_OPEN_SOURCE = 886;

    @Override
    protected void initConfig(Bundle savedInstanceState) {
        setImmersiveStatusBar(true);
        setDeepColorStatusBar(true);
        setSlideExit(true);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
    }

    @Override
    protected void initBundleData(Bundle bundle) {
        type = bundle.getInt("type", -1);
        url = bundle.getString("url", null);
        title = bundle.getString("title", null);
        if (type == -1 || TextUtils.isEmpty(url) || TextUtils.isEmpty(title)) {
            ToastUtil.showToastShort("获取数据失败！");
            finish();
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.acitivity_webview;
    }

    @Override
    protected void doBusiness() {
        toolbarTitle.setText("");
        initWebView();
        loadHtml(type, title, url);
    }

    private void initWebView() {

        settings = webView.getSettings();
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);// 使页面支持缩放
        settings.setSupportZoom(true); //支持缩放
        settings.setDisplayZoomControls(false);// 缩放按钮
        settings.setLoadsImagesAutomatically(true);//支持自动加载图片
        settings.setDomStorageEnabled(true); //开启DOM
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);//关闭webview中缓存
        settings.setAppCacheEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setUseWideViewPort(true);  //将图片调整到适合webview的大小
        settings.setNeedInitialFocus(true); ////当webview调用requestFocus时为webview设置节点
        settings.setAllowFileAccess(true);// 允许访问文件
        settings.setSupportZoom(true);// 支持缩放
        settings.setPluginState(WebSettings.PluginState.ON);
        webView.setFocusable(true);
        webView.setFocusableInTouchMode(true);
        webView.addJavascriptInterface(new JsInject(this), "imageListener");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                if (s.startsWith("http") || s.startsWith("https")) {
                    webView.loadUrl(s);
                } else if (s.startsWith("intent")) {
                    try {
                        Intent intent = Intent.parseUri(s, Intent.URI_INTENT_SCHEME);
                        intent.addCategory("android.intent.category.BROWSABLE");
                        intent.setComponent(null);
                        intent.setSelector(null);
                        List<ResolveInfo> resolves = context.getPackageManager().queryIntentActivities(intent, 0);
                        if (resolves.size() > 0) {
                            startActivityIfNeeded(intent, -1);
                        }
                        return true;
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }

                } else {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(s));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }

                return super.shouldOverrideUrlLoading(webView, s);
            }

            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);

            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                showErrorPage();
            }
        });


        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                if (progressBar != null) {
                    if (i == 100) {
                        if (type != TYPE_CHANGE_PWD) {
                            addImgClickListener();
                        }
                        progressBar.setVisibility(View.GONE);
                    } else {
                        progressBar.setProgress(i);
                    }
                }

            }
        });

    }

    private void loadHtml(int type, String title, String url) {
        LogUtils.d(TAG, "webView " + url);
        switch (type) {
            case TYPE_CHANGE_PWD:
                toolbarTitle.setText(title);
                webView.loadUrl(url);
                break;
            case TYPE_HELP:
                toolbarTitle.setText(title);
                loadContent(url);
                break;
            case TYPE_PERMISSION:
                toolbarTitle.setText(title);
                loadContent(url);
                break;
            case TYPE_LIBRARY:
                toolbarTitle.setText(title);
                webView.loadUrl(url);
                break;
            case TYPE_HOMEWORK:
                toolbarTitle.setText(title);
                webView.loadUrl(url);
                break;
            case TYPE_FRESHMAN_GUIDE:
                toolbarTitle.setText(title);
                loadContent(url);
                break;
            case TYPE_NOTICE:
                toolbarTitle.setText(title);
                webView.loadUrl(url);
                break;
            case TYPE_BLOG:
                toolbarTitle.setText(title);
                webView.loadUrl(url);
                break;
            case TYPE_OPEN_SOURCE:
                toolbarTitle.setText(title);
                webView.loadUrl(url);
                break;
            default:
                SnackbarUtils.showShortSnackbar(rootView, "未知类型");
        }
    }

    private void addImgClickListener() {
        if (webView != null) {
            webView.loadUrl("javascript:(function(){" +
                    "var objs = document.getElementsByTagName(\"img\"); " +
                    "for(var i=0;i<objs.length;i++){"
                    + "objs[i].onclick=function(){ "
                    + "window.imageListener.showImage(this.src);" +
                    "}" +
                    "}" +
                    "})()");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        settings.setJavaScriptEnabled(true);
        webView.resumeTimers();
        webView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        settings.setJavaScriptEnabled(false);
        webView.onPause();
        webView.pauseTimers();
    }

    @Override
    protected void onStop() {
        super.onStop();
        settings.setJavaScriptEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            //webView.loadDataWithBaseURL(null, "about:blank", "text/html", "utf-8", null); //会导致空指针
            webView.clearHistory();
            webView.clearCache(true);
            webView.clearFormData();
            webView.removeAllViews();
            rootView.removeView(webView);
            webView.destroy();
            webView = null;
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @OnClick(R.id.toolbar_back)
    public void onViewClicked() {
        finish();
    }

    protected void initErrorPage() {
        if (mErrorView == null) {
            mErrorView = View.inflate(this, R.layout.online_error, null);
            ImageButton button = (ImageButton) mErrorView.findViewById(R.id.btn_refer);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (!CommUtil.isOnline(getApplicationContext())) {
                        SnackbarUtils.showShortSnackbar(rootView, "网络不可用！");
                    }
                    hideErrorPage();
                    webView.reload();
                }
            });
            mErrorView.setOnClickListener(null);
        }
    }

    /**
     * 显示自定义错误提示页面，用一个View覆盖在WebView
     */
    protected void showErrorPage() {
        if (webView == null) return;
        LinearLayout webParentView = (LinearLayout) webView.getParent();
        if (webParentView != null) {
            initErrorPage();
            while (webParentView.getChildCount() > 1) {
                webParentView.removeViewAt(1);
            }
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            webParentView.addView(mErrorView, 1, lp);
            mIsErrorPage = true;
        }

    }

    protected void hideErrorPage() {
        LinearLayout webParentView = (LinearLayout) mErrorView.getParent();

        mIsErrorPage = false;
        while (webParentView.getChildCount() > 1) {
            webParentView.removeViewAt(1);
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        webParentView.addView(webView, 1, lp);
    }


    public void loadContent(final String url) {
        settings.setSupportZoom(true);
        settings.setDisplayZoomControls(false);
        settings.setBuiltInZoomControls(true);

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                Document document = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko)Chrome/50.0.2661.102 Safari/537.36")
                        .get();
                Element head = document.head();
                Element body = document.body();

                Elements content = document.getElementsByClass("content");
                Elements p = content.select("p");
                p.first().remove();
                Elements span = content.select("span");

                for (Element element : p) {
                    element.removeAttr("style");
                }

                for (Element element : span) {
                    element.removeAttr("style");
                }

                content.select("br").remove();
                Elements img = content.select("img[src]");
                for (Element element : img) {
                    String src = element.attr("abs:src");
                    src = src.replace("http://172.16.10.210", Constants.ARTICLE_BASE_URL);
                    src = src.replace("http://love.zengheng.top:8888", Constants.ARTICLE_BASE_URL);

                    element.attr("src", src);
                    element.removeAttr("_src");
                    element.removeAttr("style");
                }

                for (Element element : content.select("a")) {
                    if (element.text().equals("<< 返回首页")) {
                        element.remove();
                    }
                }

                for (Element element : content.select("pre")) {
                    element.removeAttr("style");
                }

                Elements a = content.select("a");
                for (Element element : a) {
                    if (element.attr("href").startsWith("http://shang.qq.com/wpa/qunwpa?idkey=")) {
                        element.remove();
                    }
                }

                Elements h1 = content.select("h1");

                for (Element element : h1) {
                    element.removeAttr("style");
                }

                Elements h2 = content.select("h2");
                for (Element element : h2) {
                    element.removeAttr("style");
                }

                Elements h3 = content.select("h3");
                for (Element element : h3) {
                    element.removeAttr("style");
                }

                Elements h4 = content.select("h4");
                for (Element element : h4) {
                    element.removeAttr("style");
                }

                Elements h5 = content.select("h5");
                for (Element element : h5) {
                    element.removeAttr("style");
                }

                Elements h6 = content.select("h6");
                for (Element element : h6) {
                    element.removeAttr("style");
                }

                if (type == TYPE_FRESHMAN_GUIDE && title.equals("开篇")) {
                    for (Element element : p) {
                        if (element.text().equals("目录直达链接（持续更新）：")
                                || element.text().equals("新生攻略手册--转专业篇不可抗力下架")
                                || element.text().equals("新生攻略手册--素拓篇不可抗力下架")
                                || element.text().contains("严禁抄袭!")
                                ) {
                            element.remove();
                        }
                    }
                    a.remove();
                }
                //head.empty();
                //head.append("<meta charset=\"utf-8\">" +
                //        "<script src=\"http://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js\""+
                //        " type=\"text/javascript\"></script>");
                body.empty();
                body.append(content.toString());
                String html = document.toString()
                        .replace("::selection{background:#f4645f;}", "")
                        .replace("::-moz-selection { background:#f4645f; }", "img{width: 100%; height: 100%; object-fit: contain}")
                        .replace("background: #333;", "margin: 0px 15px 0px 15px;")
                        .replace("<p><span>&nbsp;</span></p>", "")
                        .replace("<p><strong><span>&nbsp;</span></strong></p>", "");

                e.onNext(html);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(String s) {
                        if (TextUtils.isEmpty(s)) {
                            SnackbarUtils.showShortSnackbar(rootView, "获取数据失败！");
                            return;
                        }
                        if (webView != null) {
                            webView.loadDataWithBaseURL(null, s, "text/html", "UTF-8", null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        SnackbarUtils.showShortSnackbar(rootView, ExceptionEngine.handleException(e).getMsg());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
}
