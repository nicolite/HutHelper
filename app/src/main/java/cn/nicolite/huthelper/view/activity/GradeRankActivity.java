package cn.nicolite.huthelper.view.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.activity.BaseActivity;
import cn.nicolite.huthelper.view.adapter.GradeRankAdapter;
import cn.nicolite.huthelper.view.widget.CirclePie;

/**
 * 成绩排名页面
 * Created by nicolite on 17-11-12.
 */

public class GradeRankActivity extends BaseActivity {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.pie_grade_xf)
    CirclePie pieGradeXf;
    @BindView(R.id.tv_grade_avgjd)
    TextView tvGradeAvgjd;
    @BindView(R.id.tv_grade_nopassnum)
    TextView tvGradeNopassnum;
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.rootView)
    LinearLayout rootView;

    @Override
    protected void initConfig(Bundle savedInstanceState) {
        setImmersiveStatusBar(true);
        setSlideExit(true);
    }

    @Override
    protected void initBundleData(Bundle bundle) {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_grade_rank;
    }

    @Override
    protected void doBusiness() {
        toolbarTitle.setText("成绩");
        viewpager.setAdapter(new GradeRankAdapter());
        tab.setupWithViewPager(viewpager);
    }

    @OnClick({R.id.toolbar_back, R.id.toolbar_refresh, R.id.btn_grade_showall})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.toolbar_refresh:
                break;
            case R.id.btn_grade_showall:
                startActivity(GradeListActivity.class);
                break;
        }
    }
}
