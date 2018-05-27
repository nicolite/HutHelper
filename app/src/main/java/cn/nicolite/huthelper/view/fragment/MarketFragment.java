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
import cn.nicolite.huthelper.model.bean.Goods;
import cn.nicolite.huthelper.presenter.MarketPresenter;
import cn.nicolite.huthelper.utils.SnackbarUtils;
import cn.nicolite.huthelper.view.activity.GoodsInfoActivity;
import cn.nicolite.huthelper.view.adapter.MarketAdapter;
import cn.nicolite.huthelper.view.iview.IMarketView;

/**
 * 二手市场页面
 * Created by nicolite on 17-11-6.
 */

public class MarketFragment extends BaseFragment implements IMarketView {
    @BindView(R.id.lRecyclerView)
    LRecyclerView lRecyclerView;
    @BindView(R.id.rootView)
    LinearLayout rootView;

    List<Goods> goodsList = new ArrayList<>();

    public static final int ALL = 0;
    public static final int SOLD = 1;
    public static final int BUY = 2;
    public static final int SEARCH = 3;
    public static final int MYGOODS = 4;

    private int type = ALL;
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private MarketPresenter marketPresenter;
    private int currentPage = 1;
    private boolean isNoMore = false;
    private String searchText;

    public static MarketFragment newInstance(int type, String searchText) {

        Bundle args = new Bundle();

        args.putInt("type", type);
        if (!TextUtils.isEmpty(searchText)) {
            args.putString("searchText", searchText);
        }
        MarketFragment fragment = new MarketFragment();
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
            if (type == SEARCH || type == MYGOODS) {
                searchText = arguments.getString("searchText", "");
            }
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_market;
    }

    @Override
    protected void doBusiness() {
        lRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));
        lRecyclerViewAdapter = new LRecyclerViewAdapter(new MarketAdapter(context, goodsList));
        lRecyclerView.setAdapter(lRecyclerViewAdapter);

        marketPresenter = new MarketPresenter(this, this);

        lRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                switch (type) {
                    case SEARCH:
                        marketPresenter.searchGoods(searchText, 1, false);
                        break;
                    case MYGOODS:
                        marketPresenter.showGoodsByUserId(1, searchText, false);
                        break;
                    default:
                        marketPresenter.showGoodsList(type, true);
                }
            }
        });

        lRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!isNoMore) {
                    switch (type) {
                        case SEARCH:
                            marketPresenter.searchGoods(searchText, ++currentPage, true);
                            break;
                        case MYGOODS:
                            marketPresenter.showGoodsByUserId(++currentPage, searchText, true);
                            break;
                        default:
                            marketPresenter.loadMore(++currentPage, type);
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
                            marketPresenter.searchGoods(searchText, currentPage, true);
                            break;
                        case MYGOODS:
                            marketPresenter.showGoodsByUserId(currentPage, searchText, true);
                            break;
                        default:
                            marketPresenter.loadMore(currentPage, type);
                    }
                }
            }
        });

        lRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Goods goods = goodsList.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("goodsId", goods.getId());
                bundle.putString("userId", goods.getUser_id());
                bundle.putInt("position", position);
                if (type == MYGOODS && userId.equals(goods.getUser_id())) {
                    bundle.putBoolean("delete", true);
                } else {
                    bundle.putBoolean("delete", false);
                }

                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT){
                    startActivityForResult(GoodsInfoActivity.class, bundle, Constants.REQUEST);
                }else {
                    Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, "goodsTransition").toBundle();
                    startActivityForResult(GoodsInfoActivity.class, Constants.REQUEST, bundle, options);
                }

            }
        });

        //第一次打开Activity时不会回调visibleToUser，会导致第一个Fragment页面不加载数据，在这里进行处理
        if ((isUIVisible && isFirstVisible) || type == SEARCH || type == MYGOODS) {
            //marketPresenter.showGoodsList(type, false);
            lRecyclerView.forceToRefresh();
            isFirstVisible = false;
        }
    }

    @Override
    protected void visibleToUser(boolean isVisible, boolean isFirstVisible) {
        if (isFirstVisible) {
            //marketPresenter.showGoodsList(type, false);
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
    public void showGoodsList(List<Goods> goodsBeanList) {
        isNoMore = false;
        lRecyclerView.setNoMore(false);
        goodsList.clear();
        goodsList.addAll(goodsBeanList);
        lRecyclerView.refreshComplete(goodsList.size());
        lRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLoadMoreList(List<Goods> goodsBeanList) {
        int start = goodsList.size() + 1;
        goodsList.addAll(goodsBeanList);
        lRecyclerView.refreshComplete(goodsList.size());
        lRecyclerViewAdapter.notifyItemRangeInserted(start, goodsBeanList.size());
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
        //goodsList.remove(position);
        //lRecyclerViewAdapter.notifyItemRemoved(position);
        refreshData();
    }

    public void changetItem(int position, Goods good) {
        goodsList.remove(position);
        goodsList.add(position, good);
        lRecyclerViewAdapter.notifyItemChanged(position);
    }


}
