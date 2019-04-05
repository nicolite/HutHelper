package cn.nicolite.huthelper.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.BaseActivity;
import cn.nicolite.huthelper.model.Constants;
import cn.nicolite.huthelper.model.bean.Configure;
import cn.nicolite.huthelper.model.bean.User;
import cn.nicolite.huthelper.view.presenter.SearchPresenter;
import cn.nicolite.huthelper.utils.DensityUtils;
import cn.nicolite.huthelper.utils.ListUtils;
import cn.nicolite.huthelper.utils.ToastUtil;
import cn.nicolite.huthelper.view.fragment.SayFragment;

/**
 * Created by nicolite on 17-11-13.
 */

public class SayActivity extends BaseActivity {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.rootView)
    FrameLayout rootView;
    @BindView(R.id.toolbar_menu)
    ImageView toolBarMenu;
    @BindView(R.id.fragment_content)
    FrameLayout fragmentContent;
    private SayFragment fragment;

    @Override
    protected void initConfig(Bundle savedInstanceState) {
        setImmersiveStatusBar();
        setDeepColorStatusBar();
        setSlideExit();
    }


    @Override
    protected int setLayoutId() {
        return R.layout.activity_say;
    }

    @Override
    protected void doBusiness() {
        toolbarTitle.setText("说说");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragment = SayFragment.newInstance(SayFragment.ALLSAY, null);
        transaction.replace(R.id.fragment_content, fragment);
        transaction.commit();
    }

    @OnClick({R.id.toolbar_back, R.id.toolbar_menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.toolbar_menu:
                showMenuWindow(toolBarMenu);
                break;
        }
    }

    protected PopupWindow menuListWindow;

    private void showMenuWindow(View parent) {
        if (menuListWindow == null) {
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

            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popupWindowLayout = layoutInflater.inflate(R.layout.popup_list_choose, null);

            TextView tvMime = (TextView) popupWindowLayout.findViewById(R.id.tv_popmenu_mime);
            TextView tvAdd = (TextView) popupWindowLayout.findViewById(R.id.tv_popmenu_add);
            tvAdd.setText("发布说说");
            tvMime.setText("我的发布");
            tvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    menuListWindow.dismiss();
                    startActivityForResult(CreateSayActivity.class, Constants.REQUEST);
                }
            });

            tvMime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    menuListWindow.dismiss();
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", SearchPresenter.TYPE_MYSAY);
                    bundle.putString("searchText", user.getUser_id());
                    bundle.putString("extras", user.getUsername());
                    startActivity(SearchResultActivity.class, bundle);

                }
            });
            menuListWindow = new PopupWindow(popupWindowLayout, DensityUtils.dp2px(SayActivity.this, 170),
                    DensityUtils.dp2px(SayActivity.this, 115));
        }

        rootView.setForeground(getResources().getDrawable(R.color.bg_black_shadow));


        menuListWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                rootView.setForeground(getResources().getDrawable(R.color.transparent));
            }
        });

        menuListWindow.setFocusable(true);
        //设置点击外部可消失
        menuListWindow.setOutsideTouchable(true);
        menuListWindow.setBackgroundDrawable(new BitmapDrawable());
        menuListWindow.showAsDropDown(parent, -DensityUtils.dp2px(SayActivity.this, 115), 20);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
