package cn.nicolite.huthelper.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
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
import cn.nicolite.huthelper.base.fragment.BaseFragment;
import cn.nicolite.huthelper.model.bean.Goods;
import cn.nicolite.huthelper.presenter.MarketPresenter;
import cn.nicolite.huthelper.utils.LogUtils;
import cn.nicolite.huthelper.utils.SnackbarUtils;
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

    List<Goods.GoodsBean> goodsList = new ArrayList<>();

    public static final int ALL = 0;
    public static final int SOLD = 1;
    public static final int BUY = 2;
    private int type = ALL;
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private MarketPresenter marketPresenter;
    private int currentPage = 1;
    private boolean isNoMore = false;
    public static MarketFragment newInstance(int type) {

        Bundle args = new Bundle();

        args.putInt("type", type);
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
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_market;
    }

    @Override
    protected void doBusiness() {
        lRecyclerView.setLayoutManager(new GridLayoutManager(context, 2, OrientationHelper.VERTICAL, false));
        lRecyclerViewAdapter = new LRecyclerViewAdapter(new MarketAdapter(context, goodsList));
        lRecyclerView.setAdapter(lRecyclerViewAdapter);

        marketPresenter = new MarketPresenter(this, this);

        lRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                marketPresenter.showGoodsList(type, true);
            }
        });

        lRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!isNoMore){
                    marketPresenter.loadMore(++currentPage, type);
                }
            }
        });

        lRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
            @Override
            public void reload() {
                if (!isNoMore){
                    marketPresenter.loadMore(currentPage, type);
                }
            }
        });

        lRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });

        if (isUIVisible && isFirstVisible){
            //marketPresenter.showGoodsList(type, false);
            lRecyclerView.forceToRefresh();
            isFirstVisible = false;
        }
    }

    @Override
    protected void visibleToUser(boolean isVisible, boolean isFirstVisible) {
        LogUtils.d(TAG, "xxx " +  isVisible + " " + isFirstVisible);
        if (isFirstVisible){
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
    public void showGoodsList(List<Goods.GoodsBean> goodsBeanList) {
        isNoMore = false;
        lRecyclerView.setNoMore(false);
        goodsList.clear();
        goodsList.addAll(goodsBeanList);
        lRecyclerView.refreshComplete(goodsList.size());
        lRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLoadMoreList(List<Goods.GoodsBean> goodsBeanList) {
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
}
