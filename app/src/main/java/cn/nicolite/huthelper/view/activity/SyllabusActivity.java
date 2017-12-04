package cn.nicolite.huthelper.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.activity.BaseActivity;

/**
 * 课程表页面
 * Created by nicolite on 17-11-13.
 */

public class SyllabusActivity extends BaseActivity {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.rootView)
    FrameLayout rootView;

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

    @OnClick({R.id.toolbar_back, R.id.toolbar_title, R.id.toolbar_refresh})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.toolbar_title:
                break;
            case R.id.toolbar_refresh:
                break;
        }
    }
}
