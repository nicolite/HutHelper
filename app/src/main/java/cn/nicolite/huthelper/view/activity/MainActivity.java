package cn.nicolite.huthelper.view.activity;

import android.os.Bundle;

import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.activity.BaseActivity;

/**
 * 主页
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void initConfig(Bundle savedInstanceState) {
        hideToolBar(true);
        setDeepColorStatusBar(true);
        setImmersiveStatusBar(true);
    }

    @Override
    protected void initBundleData(Bundle bundle) {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void doBusiness() {

    }
}
