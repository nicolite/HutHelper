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
import cn.nicolite.huthelper.base.activity.BaseActivity;
import cn.nicolite.huthelper.model.bean.Configure;
import cn.nicolite.huthelper.model.bean.User;
import cn.nicolite.huthelper.presenter.SearchPresenter;
import cn.nicolite.huthelper.utils.DensityUtils;
import cn.nicolite.huthelper.utils.ListUtils;
import cn.nicolite.huthelper.utils.ToastUtil;
import cn.nicolite.huthelper.view.adapter.TabAdapter;
import cn.nicolite.huthelper.view.fragment.LostAndFoundFragment;

/**
 * 失物招领页面
 * Created by nicolite on 17-11-12.
 */

public class LostAndFoundActivity extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_menu)
    ImageView toolbarMenu;
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.rootView)
    FrameLayout rootView;
    private LostAndFoundFragment lostAndFoundFragmentAll;
    private LostAndFoundFragment lostAndFoundFragmentFound;
    private LostAndFoundFragment lostAndFoundFragmentLost;

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
        return R.layout.activity_lost_and_found;
    }

    @Override
    protected void doBusiness() {
        toolbarTitle.setText("失物招领");
        viewpager.setAdapter(new TabAdapter(getSupportFragmentManager(), getTitleList(), getFragmentList()));
        tab.setupWithViewPager(viewpager);
        viewpager.setOffscreenPageLimit(2);
    }

    private List<Fragment> getFragmentList() {
        List<Fragment> fragmentList = new ArrayList<>();
        if (lostAndFoundFragmentAll == null) {
            lostAndFoundFragmentAll = LostAndFoundFragment.newInstance(LostAndFoundFragment.ALL, null);
        }
        if (lostAndFoundFragmentFound == null) {
            lostAndFoundFragmentFound = LostAndFoundFragment.newInstance(LostAndFoundFragment.FOUND, null);
        }
        if (lostAndFoundFragmentLost == null) {
            lostAndFoundFragmentLost = LostAndFoundFragment.newInstance(LostAndFoundFragment.LOST, null);
        }
        fragmentList.add(lostAndFoundFragmentAll);
        fragmentList.add(lostAndFoundFragmentFound);
        fragmentList.add(lostAndFoundFragmentLost);
        return fragmentList;
    }

    private List<String> getTitleList() {
        List<String> titleList = new ArrayList<>();
        titleList.add("全部");
        titleList.add("招领");
        titleList.add("寻物");
        return titleList;
    }


    @OnClick({R.id.toolbar_back, R.id.toolbar_search, R.id.toolbar_menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.toolbar_search:
                Bundle bundle = new Bundle();
                bundle.putInt("type", SearchPresenter.TYPE_LOSTANDFOUND_SERACH);
                startActivity(SearchActivity.class, bundle);
                break;
            case R.id.toolbar_menu:
                showMenuWindows(toolbarMenu);
                break;
        }
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
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupWindowLayout = layoutInflater.inflate(R.layout.popup_list_choose, null);

            weekListWindow = new PopupWindow(popupWindowLayout,
                    DensityUtils.dp2px(context, 170),
                    DensityUtils.dp2px(context, 115));

            TextView tvMime = (TextView) popupWindowLayout.findViewById(R.id.tv_popmenu_mime);
            TextView tvAdd = (TextView) popupWindowLayout.findViewById(R.id.tv_popmenu_add);
            tvAdd.setText("添加失物");
            tvMime.setText("我的发布");
            tvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    weekListWindow.dismiss();
                    startActivity(CreateLostAndFoundActivity.class);
                }
            });
            tvMime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    weekListWindow.dismiss();
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", SearchPresenter.TYPE_MYLOSTANDFOUND);
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
        if (lostAndFoundFragmentAll != null) {
            lostAndFoundFragmentAll.onActivityResult(requestCode, resultCode, data);
        }

        if (lostAndFoundFragmentFound != null) {
            lostAndFoundFragmentFound.onActivityResult(requestCode, resultCode, data);
        }

        if (lostAndFoundFragmentLost != null) {
            lostAndFoundFragmentLost.onActivityResult(requestCode, resultCode, data);
        }
    }
}
