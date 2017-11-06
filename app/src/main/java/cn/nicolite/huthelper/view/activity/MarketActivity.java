package cn.nicolite.huthelper.view.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.activity.BaseActivity;
import cn.nicolite.huthelper.view.adapter.TabAdapter;
import cn.nicolite.huthelper.view.fragment.MarketFragment;
import cn.nicolite.huthelper.view.widget.LoadingDialog;

/**
 * 二手市场页面
 * Created by nicolite on 17-11-6.
 */

public class MarketActivity extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.rootView)
    LinearLayout rootView;
    private MarketFragment marketFragmentSold;
    private MarketFragment marketFragmentBuy;
    private MarketFragment marketFragmentAll;
    private LoadingDialog loadingDialog;

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
        return R.layout.activity_market;
    }

    @Override
    protected void doBusiness() {
        toolbarTitle.setText("二手市场");
        viewpager.setAdapter(new TabAdapter(getSupportFragmentManager(), getTitleList(), getFragmentList()));
        tab.setupWithViewPager(viewpager);
        viewpager.setOffscreenPageLimit(2);
    }

    @OnClick({R.id.toolbar_back, R.id.toolbar_menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.toolbar_menu:
                break;
        }
    }

    private List<Fragment> getFragmentList() {
        List<Fragment> fragmentList = new ArrayList<>();

        if (marketFragmentAll == null) {
            marketFragmentAll = MarketFragment.newInstance(MarketFragment.ALL);
        }
        if (marketFragmentSold == null) {
            marketFragmentSold = MarketFragment.newInstance(MarketFragment.SOLD);
        }
        if (marketFragmentBuy == null) {
            marketFragmentBuy = MarketFragment.newInstance(MarketFragment.BUY);
        }
        fragmentList.add(marketFragmentAll);
        fragmentList.add(marketFragmentSold);
        fragmentList.add(marketFragmentBuy);
        return fragmentList;
    }

    private List<String> getTitleList() {
        List<String> titleList = new ArrayList<>();
        titleList.add("全部");
        titleList.add("出售");
        titleList.add("求购");
        return titleList;
    }
}
