package cn.nicolite.huthelper.view.activity;

import android.os.Bundle;

import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.activity.BaseActivity;

/**
 * Created by nicolite on 17-10-17.
 */

public class WebViewActivity extends BaseActivity {
    @Override
    protected void initConfig(Bundle savedInstanceState) {
        setImmersiveStatusBar(true);
    }

    @Override
    protected void initBundleData(Bundle bundle) {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.acitivity_webview;
    }

    @Override
    protected void doBusiness() {

    }
}
