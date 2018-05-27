package cn.nicolite.huthelper.view.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.BaseFragment;
import cn.nicolite.huthelper.model.Constants;
import cn.nicolite.huthelper.model.bean.LostAndFound;
import cn.nicolite.huthelper.presenter.LostAndFoundPresenter;
import cn.nicolite.huthelper.utils.SnackbarUtils;
import cn.nicolite.huthelper.view.activity.LostAndFoundInfoActivity;
import cn.nicolite.huthelper.view.adapter.LostAndFoundAdapter;
import cn.nicolite.huthelper.view.iview.ILostAndFoundView;

/**
 * 失物招领页面
 * Created by nicolite on 17-11-12.
 */

public class LostAndFoundFragment extends BaseFragment implements ILostAndFoundView {
    @BindView(R.id.lRecyclerView)
    LRecyclerView lRecyclerView;
    @BindView(R.id.rootView)
    LinearLayout rootView;

    public static final int ALL = 0;
    public static final int LOST = 1;
    public static final int FOUND = 2;
    public static final int SEARCH = 3;
    public static final int MYLOSTANDFOUND = 4;
    private int type = ALL;
    private String searchText = "";
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private List<LostAndFound> lostAndFoundList = new ArrayList<>();
    private int currentPage = 1;
    private boolean isNoMore = false;
    private LostAndFoundPresenter lostAndFoundPresenter;

    public static LostAndFoundFragment newInstance(int type, String searchText) {

        Bundle args = new Bundle();
        args.putInt("type", type);
        if (!TextUtils.isEmpty(searchText)) {
            args.putString("searchText", searchText);
        }
        LostAndFoundFragment fragment = new LostAndFoundFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initConfig(Bundle savedInstanceState) {

    }

    @Override
    protected void initArguments(Bundle arguments) {
        if (arguments != null) {
            type = arguments.getInt("type", ALL);
            if (type == SEARCH || type == MYLOSTANDFOUND) {
                searchText = arguments.getString("searchText", "");
            }
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_lost_and_found;
    }

    @Override
    protected void doBusiness() {
        lRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));
        lRecyclerViewAdapter = new LRecyclerViewAdapter(new LostAndFoundAdapter(context, lostAndFoundList));
        lRecyclerView.setAdapter(lRecyclerViewAdapter);

        lostAndFoundPresenter = new LostAndFoundPresenter(this, this);

        lRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                switch (type) {
                    case SEARCH:
                        lostAndFoundPresenter.searchLostAndFound(searchText, 1, false);
                        break;
                    case MYLOSTANDFOUND:
                        lostAndFoundPresenter.showLostAndFoundByUserId(1, searchText, false);
                        break;
                    default:
                        lostAndFoundPresenter.showLostAndFoundList(type, true);
                }
            }
        });

        lRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!isNoMore) {
                    switch (type) {
                        case SEARCH:
                            lostAndFoundPresenter.searchLostAndFound(searchText, ++currentPage, true);
                            break;
                        case MYLOSTANDFOUND:
                            lostAndFoundPresenter.showLostAndFoundByUserId(++currentPage, searchText, true);
                            break;
                        default:
                            lostAndFoundPresenter.loadMore(++currentPage, type);
                    }
                }
            }
        });

        lRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
            @Override
            public void reload() {
                if (!isNoMore) {
                    switch (type) {
                        case SEARCH:
                            lostAndFoundPresenter.searchLostAndFound(searchText, currentPage, true);
                            break;
                        case MYLOSTANDFOUND:
                            lostAndFoundPresenter.showLostAndFoundByUserId(currentPage, searchText, true);
                            break;
                        default:
                            lostAndFoundPresenter.loadMore(currentPage, type);
                    }
                }
            }
        });

        lRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                LostAndFound lostAndFound = lostAndFoundList.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", lostAndFound);
                bundle.putInt("position", position);
                if (type == MYLOSTANDFOUND && userId.equals(lostAndFound.getUser_id())) {
                    bundle.putBoolean("delete", true);
                } else {
                    bundle.putBoolean("delete", false);
                }
                if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT){
                    startActivityForResult(LostAndFoundInfoActivity.class, bundle, Constants.REQUEST);
                }else {
                    Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, "lostAndFoundTransition").toBundle();
                    startActivityForResult(LostAndFoundInfoActivity.class, Constants.REQUEST,bundle, options);
                }

            }
        });

        //第一次打开Activity时不会回调visibleToUser，会导致第一个Fragment页面不加载数据，在这里进行处理
        if ((isUIVisible && isFirstVisible) || type == SEARCH || type == MYLOSTANDFOUND) {
            lRecyclerView.forceToRefresh();
            isFirstVisible = false;
        }

    }

    @Override
    protected void visibleToUser(boolean isVisible, boolean isFirstVisible) {
        if (isFirstVisible) {
            lRecyclerView.forceToRefresh();
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
        SnackbarUtils.showShortSnackbar(rootView, msg);
    }

    @Override
    public void showLostAndFoundList(List<LostAndFound> lostAndFoundList) {
        isNoMore = false;
        lRecyclerView.setNoMore(false);
        this.lostAndFoundList.clear();
        this.lostAndFoundList.addAll(lostAndFoundList);
        lRecyclerView.refreshComplete(lostAndFoundList.size());
        lRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLoadMoreList(List<LostAndFound> lostAndFoundList) {
        int start = this.lostAndFoundList.size() + 1;
        this.lostAndFoundList.addAll(lostAndFoundList);
        lRecyclerView.refreshComplete(lostAndFoundList.size());
        lRecyclerViewAdapter.notifyItemRangeInserted(start, lostAndFoundList.size());
    }

    @Override
    public void noMoreData() {
        --currentPage;
        isNoMore = true;
        lRecyclerView.setNoMore(true);
    }

    @Override
    public void loadMoreFailure() {
        --currentPage;
    }

    @Override
    public void loadFailure() {
        lRecyclerView.refreshComplete(0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST) {
            switch (resultCode) {
                case Constants.DELETE:
                    int position = data.getIntExtra("position", -1);
                    if (position != -1) {
                        deleteItem(position);
                    }
                    break;
                case Constants.PUBLISH:
                    refreshData();
                    break;
                case Constants.CHANGE:
                    break;
            }
        }
    }

    public void refreshData() {
        lRecyclerView.forceToRefresh();
    }

    public void deleteItem(int position) {
       // lostAndFoundList.remove(position);
       // lRecyclerViewAdapter.notifyItemRemoved(position);

        refreshData();
    }

    public void changetItem(int position, LostAndFound lostAndFound) {
        lostAndFoundList.remove(position);
        lostAndFoundList.add(position, lostAndFound);
        lRecyclerViewAdapter.notifyItemChanged(position);
    }
}
