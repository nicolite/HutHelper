package cn.nicolite.huthelper.view.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.activity.BaseActivity;
import cn.nicolite.huthelper.model.bean.GradeRank;
import cn.nicolite.huthelper.model.bean.GradeRankResult;
import cn.nicolite.huthelper.presenter.GradeRankPresenter;
import cn.nicolite.huthelper.utils.SnackbarUtils;
import cn.nicolite.huthelper.view.adapter.GradeRankAdapter;
import cn.nicolite.huthelper.view.iview.IGradeRankView;
import cn.nicolite.huthelper.view.widget.CirclePie;
import cn.nicolite.huthelper.view.widget.LoadingDialog;

/**
 * 成绩排名页面
 * Created by nicolite on 17-11-12.
 */

public class GradeRankActivity extends BaseActivity implements IGradeRankView {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.pie_grade_xf)
    CirclePie pieGradeXf;
    @BindView(R.id.tv_grade_avgjd)
    TextView tvGradeAvgjd;
    @BindView(R.id.tv_grade_nopassnum)
    TextView tvGradeNopassnum;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.rootView)
    LinearLayout rootView;
    @BindView(R.id.tv_grade_avggrade)
    TextView tvGradeAvggrade;
    @BindViews({R.id.radio_grade_xq, R.id.radio_grade_xn})
    List<RadioButton> segmentedControls;

    private GradeRankPresenter gradeRankPresenter;
    private LoadingDialog loadingDialog;
    private List<GradeRank> xnRank = new ArrayList<>();
    private List<GradeRank> xqRank = new ArrayList<>();
    private GradeRankAdapter adapter;

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
        gradeRankPresenter = new GradeRankPresenter(this, this);
        gradeRankPresenter.showRank();

    }

    @OnClick({R.id.toolbar_back, R.id.toolbar_refresh, R.id.btn_grade_showall})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.toolbar_refresh:
                gradeRankPresenter.showRank();
                break;
            case R.id.btn_grade_showall:
                startActivity(GradeListActivity.class);
                break;
        }
    }

    //radio监听
    @OnCheckedChanged({R.id.radio_grade_xq, R.id.radio_grade_xn})
    public void setRadioGroupSegmentedControl(RadioButton radioButton, boolean isChecked) {
        if (isChecked) {
            viewpager.setCurrentItem(segmentedControls.indexOf(radioButton));
        }
    }

    @Override
    public void showLoading() {
        loadingDialog = new LoadingDialog(context)
                .setLoadingText("查询中...");
        loadingDialog.show();
    }

    @Override
    public void closeLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void showMessage(String msg) {
        SnackbarUtils.showShortSnackbar(rootView, msg);
    }

    @Override
    public void showRank(GradeRankResult gradeRankResult) {
        tvGradeAvgjd.setText(String.valueOf("综合绩点  " + gradeRankResult.getZhjd()));
        tvGradeNopassnum.setText(String.valueOf("总挂科数  " + gradeRankResult.getGks()));
        tvGradeAvggrade.setText(String.valueOf("总平均分   " + gradeRankResult.getPjf()));
        pieGradeXf.setCurrNum(Float.parseFloat(gradeRankResult.getZxf()), Float.parseFloat(gradeRankResult.getWdxf()));
        xnRank.clear();
        xnRank.addAll(gradeRankResult.getRank().getXnrank());
        Collections.sort(xnRank, new Comparator<GradeRank>() {
            @Override
            public int compare(GradeRank gradeRank, GradeRank t1) {
                int temp1 = Integer.parseInt(gradeRank.getXn().split("-")[0]);
                int temp2 = Integer.parseInt(t1.getXn().split("-")[0]);
                return temp1 - temp2;
            }
        });

        xqRank.clear();
        xqRank.addAll(gradeRankResult.getRank().getXqrank());

        Collections.sort(xqRank, new Comparator<GradeRank>() {
            @Override
            public int compare(GradeRank gradeRank, GradeRank t1) {
                int temp1 = Integer.parseInt(gradeRank.getXn().split("-")[0] + gradeRank.getXq());
                int temp2 = Integer.parseInt(t1.getXn().split("-")[0] + t1.getXq());
                return temp1 - temp2;
            }
        });

        adapter = new GradeRankAdapter(context, xnRank, xqRank);
        viewpager.setAdapter(adapter);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                segmentedControls.get(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewpager.setCurrentItem(0);
        segmentedControls.get(0).setChecked(true);
    }

}
