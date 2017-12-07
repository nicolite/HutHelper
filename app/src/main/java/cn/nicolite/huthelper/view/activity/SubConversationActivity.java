package cn.nicolite.huthelper.view.activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.activity.BaseActivity;

/**
 * Created by nicolite on 17-10-24.
 */

public class SubConversationActivity extends BaseActivity {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.rootView)
    CoordinatorLayout rootView;

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
        return R.layout.activity_sub_conversation;
    }

    @Override
    protected void doBusiness() {

    }


    @OnClick(R.id.toolbar_back)
    public void onViewClicked() {
        finish();
    }
}
