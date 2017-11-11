package cn.nicolite.huthelper.view.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.activity.BaseActivity;
import cn.nicolite.huthelper.presenter.SearchPresenter;
import cn.nicolite.huthelper.utils.ToastUtil;
import cn.nicolite.huthelper.view.fragment.MarketFragment;
import cn.nicolite.huthelper.view.fragment.UserListFragment;

/**
 * Created by nicolite on 17-11-11.
 */

public class SearchResultActivity extends BaseActivity {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.fragment_content)
    FrameLayout fragmentContent;
    @BindView(R.id.rootView)
    LinearLayout rootView;

    private int type;
    private String searchText;

    @Override
    protected void initConfig(Bundle savedInstanceState) {
        setImmersiveStatusBar(true);
        setDeepColorStatusBar(true);
        setSlideExit(true);
    }

    @Override
    protected void initBundleData(Bundle bundle) {
        if (bundle != null) {
            type = bundle.getInt("type", -1);
            searchText = bundle.getString("searchText", "");
            if (type == -1) {
                ToastUtil.showToastShort("获取类型异常！");
                finish();
            }
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_search_result;
    }

    @Override
    protected void doBusiness() {
        if (TextUtils.isEmpty(searchText)) {
            toolbarTitle.setText("搜索结果");
        } else {
            toolbarTitle.setText(searchText);
        }
        loadFragment(type);
    }

    @OnClick(R.id.toolbar_back)
    public void onViewClicked() {
        finish();
    }

    private void loadFragment(int type) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (type) {
            case SearchPresenter.TYPE_MARKET:
                transaction.replace(R.id.fragment_content,
                        MarketFragment.newInstance(MarketFragment.SEARCH, searchText));
                break;
            case SearchPresenter.TYPE_LOST:
                break;
            case SearchPresenter.TYPE_USER:
                transaction.replace(R.id.fragment_content, UserListFragment.newInstance(searchText));
                break;
            default:
                ToastUtil.showToastShort("未知类型！");
        }
        transaction.commit();
    }
}
