package cn.nicolite.huthelper.view.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import butterknife.BindView;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.BaseActivity;


/**
 * H5播放视频
 * Created by nicolite on 17-7-17.
 */

public class VideoH5PlayerActivity extends BaseActivity {
    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.rootView)
    LinearLayout rootView;
    private String videoUrl;
    private WebSettings settings;

    @Override
    protected void initConfig(Bundle savedInstanceState) {

    }

    @Override
    protected void initBundleData(Bundle bundle) {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_videoview_h5;
    }

    @Override
    protected void doBusiness() {
        videoUrl = getIntent().getExtras().getString("video_path");
        settings = webview.getSettings();
        settings.setLoadWithOverviewMode(true);
        //settings.setBuiltInZoomControls(true);// 使页面支持缩放
        //settings.setSupportZoom(true);
        settings.setLoadsImagesAutomatically(true);//支持自动加载图片
        //settings.setDisplayZoomControls(false);// 缩放按钮
        settings.setDomStorageEnabled(true); //开启DOM
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);//关闭webview中缓存
        //settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setUseWideViewPort(true);  //将图片调整到适合webview的大小
        settings.setNeedInitialFocus(true); ////当webview调用requestFocus时为webview设置节点
        settings.setAllowFileAccess(true);// 允许访问文件
        webview.setBackgroundColor(0); // 设置背景色
        webview.setFocusable(true);
        //webview.setFocusableInTouchMode(true);

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
            }
        });

        webview.setWebChromeClient(new WebChromeClient());

        String html = "<html><head>" +
                "<meta charset=\"utf-8\"/>" +
                "<title></title>" +
                "<style>video::-webkit-media-controls-fullscreen-button {" +
                "            display: none;" +
                "        }</style>" +
                "</head><body>" +
                "<video id=\"video\" src=\"" + videoUrl +
                "\"max-width=\"100%\" max-height=\"100%\" width=\"100%\" height=\"98%\" " +
                "controls=\"controls\" autoplay=\"autoplay\" preload=\"auto\">"
                + "播放失败</video></body></html>";
        webview.loadDataWithBaseURL(null, html, "text/html", "utf8", null);
        webview.setVisibility(View.VISIBLE);
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
        //防止webview内存泄漏
        if (webview != null) {
            webview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webview.clearHistory();
            webview.clearCache(true);
            webview.clearFormData();
            rootView.removeView(webview);
            webview.removeAllViews();
            webview.destroy();
            webview = null;
        }
    }
}
