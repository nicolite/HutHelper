package cn.nicolite.huthelper.view.activity;

import android.graphics.Bitmap;
import android.graphics.PixelFormat;
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

import butterknife.BindView;
import butterknife.OnClick;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.activity.BaseActivity;
import cn.nicolite.huthelper.injection.JsInject;
import cn.nicolite.huthelper.utils.CommUtil;
import cn.nicolite.huthelper.utils.SnackbarUtils;
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
    private WebSettings settings;
    private View mErrorView;
    private boolean mIsErrorPage;

    public static final int TYPE_CHANGE_PWD = 561;
    public static final int TYPE_HELP = 334;
    public static final int TYPE_PERMISSION = 545;
    public static final int TYPE_LIBRARY = 278;
    public static final int TYPE_HOMEWORK = 576;
    public static final int TYPE_FRESHMAN_HELPER = 685;
    public static final int TYPE_NOTICE = 174;
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
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);

            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                showErrorPage();
            }

        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);

                if (progressBar == null){
                    return;
                }

                    if (i == 100) {
                        if (type != TYPE_CHANGE_PWD){
                            addImgClickListener();
                        }
                        progressBar.setVisibility(View.GONE);
                    } else {
                        progressBar.setProgress(i);
                    }
            }
        });
    }

    private void loadHtml(int type, String title, String url) {
        switch (type) {
            case TYPE_CHANGE_PWD:
                toolbarTitle.setText(title);
                webView.loadUrl(url);
                break;
            case TYPE_HELP:
                toolbarTitle.setText(title);
                webView.loadUrl(url);
                break;
            case TYPE_PERMISSION:
                toolbarTitle.setText(title);
                webView.loadUrl(url);
                break;
            case TYPE_LIBRARY:
                toolbarTitle.setText(title);
                webView.loadUrl(url);
                break;
            case TYPE_HOMEWORK:
                toolbarTitle.setText(title);
                webView.loadUrl(url);
                break;
            case TYPE_FRESHMAN_HELPER:
                toolbarTitle.setText(title);
                webView.loadUrl(url);
                break;
            case TYPE_NOTICE:
                toolbarTitle.setText(title);
                webView.loadUrl(url);
                break;
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
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null); //会导致空指针
            webView.clearHistory();
            webView.clearCache(true);
            webView.clearFormData();
            rootView.removeView(webView);
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack();
        }else {
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
        if (webView == null){
            return;
        }
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

}
