package cn.nicolite.huthelper.view.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.activity.BaseActivity;
import cn.nicolite.huthelper.model.bean.SearchHistory;
import cn.nicolite.huthelper.presenter.SearchPresenter;
import cn.nicolite.huthelper.utils.KeyBoardUtils;
import cn.nicolite.huthelper.utils.SnackbarUtils;
import cn.nicolite.huthelper.utils.ToastUtil;
import cn.nicolite.huthelper.view.adapter.SearchAdapter;
import cn.nicolite.huthelper.view.iview.ISearchView;
import cn.nicolite.huthelper.view.customView.CommonDialog;

/**
 * 搜素页面
 * Created by nicolite on 17-11-10.
 */

public class SearchActivity extends BaseActivity implements ISearchView {

    @BindView(R.id.toolbar_search_edit)
    EditText toolbarSearchEdit;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.rootView)
    LinearLayout rootView;
    private SearchPresenter searchPresenter;
    private int type;
    private List<SearchHistory> searchHistorys = new ArrayList<>();
    private SearchAdapter adapter;

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
            if (type == -1) {
                ToastUtil.showToastShort("获取类型异常！");
                finish();
            }
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_goods_search;
    }

    @Override
    protected void doBusiness() {
        recyclerView.setLayoutManager(new GridLayoutManager(context, 3, OrientationHelper.VERTICAL, false));
        adapter = new SearchAdapter(context, searchHistorys);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, long itemId) {
                SearchHistory searchHistory = searchHistorys.get(position);
                toolbarSearchEdit.setText(searchHistory.getHistory());
                startSearchResult();
            }
        });

        searchPresenter = new SearchPresenter(this, this);
        searchPresenter.showHistory(type);

        adapter.setOnItemLongClickListener(new SearchAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, final int position, long itemId) {
                final CommonDialog commonDialog = new CommonDialog(context);
                commonDialog
                        .setMessage("确定删除该条记录？")
                        .setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                searchPresenter.deleteHistoryItem(searchHistorys.get(position));
                                searchHistorys.remove(position);
                                adapter.notifyItemRemoved(position);
                                commonDialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                return true;
            }
        });

        toolbarSearchEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    KeyBoardUtils.hideSoftInput(context, getWindow());
                    searchPresenter.addHistory(type, toolbarSearchEdit.getText().toString());
                    startSearchResult();
                    return true;
                }
                return false;
            }
        });
    }


    @OnClick({R.id.toolbar_back, R.id.bt_submit, R.id.delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.bt_submit:
                KeyBoardUtils.hideSoftInput(context, getWindow());
                searchPresenter.addHistory(type, toolbarSearchEdit.getText().toString());
                startSearchResult();
                break;
            case R.id.delete:
                final CommonDialog commonDialog = new CommonDialog(context);
                commonDialog
                        .setMessage("确认删除全部历史记录？")
                        .setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                searchPresenter.deleteHistory(type);
                                commonDialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void closeLoading() {

    }

    @Override
    public void showMessage(String msg) {
        SnackbarUtils.showShortSnackbar(recyclerView, msg);
    }

    @Override
    public void showHistory(List<SearchHistory> historyList) {
        searchHistorys.clear();
        searchHistorys.addAll(historyList);
        adapter.notifyDataSetChanged();
    }

    private void startSearchResult() {
        if (TextUtils.isEmpty(toolbarSearchEdit.getText().toString())){
            showMessage("你还没有输入搜索内容！");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putString("searchText", toolbarSearchEdit.getText().toString());
        startActivity(SearchResultActivity.class, bundle);
    }
}
