package cn.nicolite.huthelper.view.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.BaseActivity;
import cn.nicolite.huthelper.model.Constants;
import cn.nicolite.huthelper.model.bean.Configure;
import cn.nicolite.huthelper.model.bean.User;
import cn.nicolite.huthelper.presenter.SearchPresenter;
import cn.nicolite.huthelper.utils.DensityUtils;
import cn.nicolite.huthelper.utils.ListUtils;
import cn.nicolite.huthelper.utils.ToastUtil;
import cn.nicolite.huthelper.view.adapter.TabAdapter;
import cn.nicolite.huthelper.view.fragment.MarketFragment;

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
    FrameLayout rootView;
    @BindView(R.id.toolbar_menu)
    ImageView toolbarMenu;
    private MarketFragment marketFragmentAll;
    private MarketFragment marketFragmentSold;
    private MarketFragment marketFragmentBuy;

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

    @OnClick({R.id.toolbar_back, R.id.toolbar_menu, R.id.toolbar_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.toolbar_menu:
                showMenuWindows(toolbarMenu);
                break;
            case R.id.toolbar_search:
                Bundle bundle = new Bundle();
                bundle.putInt("type", SearchPresenter.TYPE_MARKET_SEARCH);
                startActivity(SearchActivity.class, bundle);
                break;
        }
    }

    private List<Fragment> getFragmentList() {
        List<Fragment> fragmentList = new ArrayList<>();

        if (marketFragmentAll == null) {
            marketFragmentAll = MarketFragment.newInstance(MarketFragment.ALL, null);
        }
        if (marketFragmentSold == null) {
            marketFragmentSold = MarketFragment.newInstance(MarketFragment.SOLD, null);
        }
        if (marketFragmentBuy == null) {
            marketFragmentBuy = MarketFragment.newInstance(MarketFragment.BUY, null);
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

    protected PopupWindow weekListWindow;

    private void showMenuWindows(View parent) {
        if (TextUtils.isEmpty(userId)) {
            ToastUtil.showToastShort("获取用户信息失败！");
            return;
        }

        List<Configure> configureList = getConfigureList();

        if (ListUtils.isEmpty(configureList)) {
            ToastUtil.showToastShort("获取用户信息失败！");
            return;
        }
        final User user = configureList.get(0).getUser();

        if (weekListWindow == null) {
            View popupWindowLayout = LayoutInflater.from(context).inflate(R.layout.popup_list_choose, rootView, false);

            weekListWindow = new PopupWindow(popupWindowLayout,
                    DensityUtils.dp2px(context, 170),
                    DensityUtils.dp2px(context, 115));

            TextView tvMime = (TextView) popupWindowLayout.findViewById(R.id.tv_popmenu_mime);
            TextView tvAdd = (TextView) popupWindowLayout.findViewById(R.id.tv_popmenu_add);
            tvAdd.setText("发布商品");
            tvMime.setText("我的发布");
            tvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    weekListWindow.dismiss();
                    startActivityForResult(CreateGoodsActivity.class, Constants.REQUEST);
                }
            });
            tvMime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    weekListWindow.dismiss();
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", SearchPresenter.TYPE_MARKET_MYGOODS);
                    bundle.putString("searchText", user.getUser_id());
                    bundle.putString("extras", user.getUsername());
                    startActivity(SearchResultActivity.class, bundle);
                }
            });

        }

        rootView.setForeground(getResources().getDrawable(R.color.bg_black_shadow));

        weekListWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                rootView.setForeground(getResources().getDrawable(R.color.transparent));
            }
        });
        weekListWindow.setFocusable(true);
        //设置点击外部可消失
        weekListWindow.setOutsideTouchable(true);
        weekListWindow.setBackgroundDrawable(new BitmapDrawable());
        weekListWindow.showAsDropDown(parent, -DensityUtils.dp2px(context, 115), 20);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (marketFragmentAll != null) {
            marketFragmentAll.onActivityResult(requestCode, resultCode, data);
        }

        if (marketFragmentSold != null) {
            marketFragmentSold.onActivityResult(requestCode, resultCode, data);
        }

        if (marketFragmentBuy != null) {
            marketFragmentBuy.onActivityResult(requestCode, resultCode, data);
        }
    }
}
