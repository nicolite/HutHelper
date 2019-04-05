package cn.nicolite.huthelper.view.activity;

import android.os.Bundle;
import android.widget.TextView;

import butterknife.OnClick;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.BaseActivity;

/**
 * Created by nicolite on 17-10-22.
 */

public class CalendarActivity extends BaseActivity {
    private TextView toolbarTitle;

    @Override
    protected void initConfig(Bundle savedInstanceState) {
        setImmersiveStatusBar();
        setDeepColorStatusBar();
        setSlideExit();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_calendar;
    }

    @Override
    protected void doBusiness() {
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText("校历");
    }

    @OnClick(R.id.toolbar_back)
    public void onViewClicked() {
        finish();
    }
}
