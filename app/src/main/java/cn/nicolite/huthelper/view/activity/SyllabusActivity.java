package cn.nicolite.huthelper.view.activity;

import android.os.Bundle;

import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.activity.BaseActivity;

/**
 * 课程表页面
 * Created by nicolite on 17-11-13.
 */

public class SyllabusActivity extends BaseActivity {
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
        return R.layout.activity_syllabus;
    }

    @Override
    protected void doBusiness() {

    }
}
