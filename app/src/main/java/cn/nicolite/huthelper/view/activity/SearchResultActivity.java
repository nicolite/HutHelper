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
import cn.nicolite.huthelper.view.fragment.LostAndFoundFragment;
import cn.nicolite.huthelper.view.fragment.MarketFragment;
import cn.nicolite.huthelper.view.fragment.SayFragment;
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
    private String extras;

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
            extras = bundle.getString("extras", "");
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

        switch (type) {
            case SearchPresenter.TYPE_MARKET_MYGOODS:
                if (userId.equals(searchText)) {
                    toolbarTitle.setText("我的商品");
                } else {
                    toolbarTitle.setText(String.valueOf(extras + "的商品"));
                }
                break;
            case SearchPresenter.TYPE_MYLOSTANDFOUND:
                if (userId.equals(searchText)) {
                    toolbarTitle.setText("我的失物招领");
                } else {
                    toolbarTitle.setText(String.valueOf(extras + "的失物招领"));
                }
                break;
            case SearchPresenter.TYPE_MYSAY:
                if (userId.equals(searchText)) {
                    toolbarTitle.setText("我的说说");
                } else {
                    toolbarTitle.setText(String.valueOf(extras + "的说说"));
                }
                break;
            default:
                toolbarTitle.setText(searchText);
                break;

        }

        if (TextUtils.isEmpty(searchText)) {
            toolbarTitle.setText("搜索结果");
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
            case SearchPresenter.TYPE_MARKET_SEARCH:
                transaction.replace(R.id.fragment_content,
                        MarketFragment.newInstance(MarketFragment.SEARCH, searchText));
                break;
            case SearchPresenter.TYPE_MARKET_MYGOODS:
                transaction.replace(R.id.fragment_content,
                        MarketFragment.newInstance(MarketFragment.MYGOODS, searchText));
                break;
            case SearchPresenter.TYPE_LOSTANDFOUND_SERACH:
                transaction.replace(R.id.fragment_content,
                        LostAndFoundFragment.newInstance(LostAndFoundFragment.SEARCH, searchText));
                break;
            case SearchPresenter.TYPE_MYLOSTANDFOUND:
                transaction.replace(R.id.fragment_content,
                        LostAndFoundFragment.newInstance(LostAndFoundFragment.MYLOSTANDFOUND, searchText));
                break;
            case SearchPresenter.TYPE_USER_SEARCH:
                transaction.replace(R.id.fragment_content, UserListFragment.newInstance(searchText));
                break;
            case SearchPresenter.TYPE_MYSAY:
                transaction.replace(R.id.fragment_content, SayFragment.newInstance(SayFragment.MYSAY, searchText));
                break;
            default:
                ToastUtil.showToastShort("未知类型！");
        }
        transaction.commit();
    }
}
