package cn.nicolite.huthelper.view.activity;

import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import butterknife.BindView;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.activity.BaseActivity;
import cn.nicolite.huthelper.injection.JsInject;
import cn.nicolite.huthelper.utils.SnackbarUtils;

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
    private int type;
    private String url;
    private String title;
    private WebSettings settings;

    public static final int TYPE_NEWS = 884;
    public static final int TYPE_NEWS_BANNER = 811;
    public static final int TYPE_TY_VIDEO = 995;
    public static final int TYPE_TY_CARTOON = 840;
    public static final int TYPE_TY_COS = 734;
    public static final int TYPE_VIDEO = 517;

    @Override
    protected void initConfig(Bundle savedInstanceState) {
        setImmersiveStatusBar(true);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
    }

    @Override
    protected void initBundleData(Bundle bundle) {
        type = bundle.getInt("type", -1);
        url = bundle.getString("url", null);
        title = bundle.getString("title", null);
        if (type == -1 || TextUtils.isEmpty(url) || TextUtils.isEmpty(title)) {
            SnackbarUtils.showShortSnackbar(rootView, "获取数据失败！");
            finish();
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.acitivity_webview;
    }

    @Override
    protected void doBusiness() {
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
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);

            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                if (i == 100){
                    addImgClickListener();
                }else {
                    progressBar.setProgress(i);
                }
            }
        });
    }

    private void loadHtml(int type, String title,String url){
        switch (type){
            default:
                SnackbarUtils.showShortSnackbar(rootView, "未知类型");
        }
    }

    private void addImgClickListener() {
        webView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "for(var i=0;i<objs.length;i++){"
                + "objs[i].onclick=function(){ "
                + "window.imageListener.showImage(this.src);" +
                "}" +
                "}" +
                "})()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        settings.setJavaScriptEnabled(true);
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
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            webView.clearCache(true);
            webView.clearFormData();
            webView.removeAllViews();
            rootView.removeView(webView);
            webView.destroy();
            webView = null;
        }
    }
}
