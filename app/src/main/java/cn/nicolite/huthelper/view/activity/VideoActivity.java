package cn.nicolite.huthelper.view.activity;

import android.os.Bundle;

import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.activity.BaseActivity;

/**
 * 视频专栏
 * Created by nicolite on 17-12-2.
 */

public class VideoActivity extends BaseActivity {
    @Override
    protected void initConfig(Bundle savedInstanceState) {
        setImmersiveStatusBar(true);
        setDeepColorStatusBar(true);
        setSlideExit(true);
    }

    @Override
    protected void initBundleData(Bundle bundle) {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_video;
    }

    @Override
    protected void doBusiness() {

    }
}
