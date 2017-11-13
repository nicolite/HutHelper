package cn.nicolite.huthelper.view.activity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.OnClick;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.activity.BaseActivity;
import cn.nicolite.huthelper.model.bean.CareerTalkItem;
import cn.nicolite.huthelper.presenter.CareerTalkItemPresenter;
import cn.nicolite.huthelper.utils.SnackbarUtils;
import cn.nicolite.huthelper.utils.ToastUtil;
import cn.nicolite.huthelper.view.iview.ICareerTalkItemView;
import cn.nicolite.huthelper.view.widget.LoadingDialog;

/**
 * 宣讲会详情页面
 * Created by nicolite on 17-11-5.
 */

public class CareerTalkItemActivity extends BaseActivity implements ICareerTalkItemView {


    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    FrameLayout toolbar;
    @BindView(R.id.iv_careertalk_logo)
    ImageView ivCareertalkLogo;
    @BindView(R.id.careertalk_item_title)
    TextView careertalkItemTitle;
    @BindView(R.id.careertalk_item_from)
    TextView careertalkItemFrom;
    @BindView(R.id.careertalk_item_holdtime)
    TextView careertalkItemHoldtime;
    @BindView(R.id.careertalk_item_address)
    TextView careertalkItemAddress;
    @BindView(R.id.careertalk_item_webview)
    WebView webView;
    @BindView(R.id.rootView)
    LinearLayout rootView;

    private LoadingDialog loadingDialog;
    private CareerTalkItemPresenter careerTalkItemPresenter;
    private int id;

    @Override
    protected void initConfig(Bundle savedInstanceState) {
        setImmersiveStatusBar(true);
        setDeepColorStatusBar(true);
        setSlideExit(true);
    }

    @Override
    protected void initBundleData(Bundle bundle) {
        if (bundle != null){
            id = bundle.getInt("id", -1);
            if (id == -1){
                ToastUtil.showToastShort("获取参数异常！");
                finish();
            }
        }else {
            ToastUtil.showToastShort("获取参数异常！");
            finish();
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_careertalkitem;
    }

    @Override
    protected void doBusiness() {
        toolbarTitle.setText("宣讲详情");
        careerTalkItemPresenter = new CareerTalkItemPresenter(this, this);
        careerTalkItemPresenter.showContent(id);
    }

    @Override
    public void showLoading() {
        loadingDialog = new LoadingDialog(context)
                .setLoadingText("加载中...");
        loadingDialog.show();
    }

    @Override
    public void closeLoading() {
        if (loadingDialog != null){
            loadingDialog.dismiss();
        }
    }

    @Override
    public void showMessage(String msg) {
        SnackbarUtils.showShortSnackbar(rootView, msg);
    }

    @Override
    public void showContent(CareerTalkItem careerTalkItem) {
        careertalkItemTitle.setText(careerTalkItem.getTitle());
        careertalkItemAddress.setText(String.valueOf("地点:" + careerTalkItem.getAddress()));
        careertalkItemFrom.setText(String.valueOf("来源:" + careerTalkItem.getWeb()));
        careertalkItemHoldtime.setText(String.valueOf("时间:" + careerTalkItem.getHoldtime()));
        Glide
                .with(CareerTalkItemActivity.this)
                .load(careerTalkItem.getLogoUrl())
                .skipMemoryCache(true)
                .crossFade()
                .into(ivCareertalkLogo);

        WebSettings settings = webView.getSettings();
        settings.setSupportZoom(true);
        settings.setJavaScriptEnabled(true);
        settings.setDisplayZoomControls(false);
        settings.setBuiltInZoomControls(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                webView.loadUrl(s);
                return true;
            }
        });

        webView.setVerticalScrollbarOverlay(false);
        webView.loadDataWithBaseURL(null, careerTalkItem.getContent(), "text/html", "utf-8", null);

    }

    @OnClick(R.id.toolbar_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //防止webview内存泄漏
        if (webView != null){
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            webView.clearCache(true);
            webView.clearFormData();
            rootView.removeView(webView);
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }
    }
}
