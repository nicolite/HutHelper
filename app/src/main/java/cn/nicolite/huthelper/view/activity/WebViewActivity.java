package cn.nicolite.huthelper.view.activity;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URISyntaxException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.BaseActivity;
import cn.nicolite.huthelper.injection.JsInject;
import cn.nicolite.huthelper.utils.LogUtils;
import cn.nicolite.huthelper.utils.ToastUtil;

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
    private String html;
    private WebSettings settings;

    public static final int TYPE_LIBRARY = 278;
    public static final int TYPE_HOMEWORK = 576;
    public static final int TYPE_NOTICE = 174;
    public static final int TYPE_FRESHMAN_STRATEGY = 519;
    public static final int TYPE_BLOG = 434;
    public static final int TYPE_OPEN_SOURCE = 886;

    @Override
    protected void initConfig(Bundle savedInstanceState) {
        setImmersiveStatusBar();
        setDeepColorStatusBar();
        setPixelFormat();
        setSlideExit();
    }

    @Override
    protected void initBundleData(Bundle bundle) {
        super.initBundleData(bundle);
        type = bundle.getInt("type", -1);
        url = bundle.getString("url", "");
        title = bundle.getString("title", "");
        html = bundle.getString("html", "");
        if (type == -1 || TextUtils.isEmpty(title)) {
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
        toolbarTitle.setText(title);
        initWebView();
        loadHtml(type, url);
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
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                if (type != TYPE_LIBRARY) {
                    addImgClickListener();
                }
            }

        });


        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                if (progressBar != null) {
                    if (i == 100) {
                        progressBar.setVisibility(View.GONE);
                    } else {
                        progressBar.setProgress(i);
                    }
                }
            }
        });

    }

    private void loadHtml(int type, String url) {
        LogUtils.d(TAG, "webView " + url);
        switch (type) {
            case TYPE_FRESHMAN_STRATEGY:
                settings.setSupportZoom(true);
                settings.setDisplayZoomControls(false);
                settings.setBuiltInZoomControls(true);
                webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", "");
                break;
            default:
                webView.loadUrl(url);
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
            webView.loadDataWithBaseURL(null, "about:blank", "text/html", "utf-8", null); //会导致空指针
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
}
