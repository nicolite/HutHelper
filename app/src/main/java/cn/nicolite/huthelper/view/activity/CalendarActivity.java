package cn.nicolite.huthelper.view.activity;

import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.activity.BaseActivity;

/**
 * Created by nicolite on 17-10-22.
 */

public class CalendarActivity extends BaseActivity {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;

    @Override
    protected void initConfig(Bundle savedInstanceState) {

    }

    @Override
    protected void initBundleData(Bundle bundle) {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_calendar;
    }

    @Override
    protected void doBusiness() {
        toolbarTitle.setText("校历");
    }

    @OnClick(R.id.toolbar_back)
    public void onViewClicked() {
        finish();
    }
}
