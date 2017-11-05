package cn.nicolite.huthelper.view.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.activity.BaseActivity;
import cn.nicolite.huthelper.model.bean.CareerTalk;
import cn.nicolite.huthelper.presenter.CareerTalkPresenter;
import cn.nicolite.huthelper.utils.SnackbarUtils;
import cn.nicolite.huthelper.view.adapter.CareerTalkAdapter;
import cn.nicolite.huthelper.view.iview.ICareerTalkView;
import cn.nicolite.huthelper.view.widget.LoadingDialog;

/**
 * 宣讲会页面
 * Created by nicolite on 17-11-5.
 */

public class CareerTalkActivity extends BaseActivity implements ICareerTalkView {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_left_text)
    TextView toolbarLeftText;
    @BindView(R.id.rl_empty)
    LinearLayout rlEmpty;
    @BindView(R.id.lRecyclerView)
    LRecyclerView lRecyclerView;
    @BindView(R.id.rootView)
    LinearLayout rootView;

    private List<CareerTalk> careerTalkList = new ArrayList<>();
    private CareerTalkAdapter careerTalkAdapter;
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private CareerTalkPresenter careerTalkPresenter;
    private LoadingDialog loadingDialog;
    private int currentPage = 1;
    private boolean isAll = true;

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
        return R.layout.activity_careertalk;
    }

    @Override
    protected void doBusiness() {
        toolbarTitle.setText("宣讲会");
        toolbarLeftText.setText("省内");

        careerTalkPresenter = new CareerTalkPresenter(this, this);

        lRecyclerView.setLayoutManager(new LinearLayoutManager(context, OrientationHelper.VERTICAL, false));

        careerTalkAdapter = new CareerTalkAdapter(context, careerTalkList);
        lRecyclerViewAdapter = new LRecyclerViewAdapter(careerTalkAdapter);
        lRecyclerView.setAdapter(lRecyclerViewAdapter);

        lRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
              careerTalkPresenter.showCareerTalkList(currentPage, true, isAll);
            }
        });

        lRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                careerTalkPresenter.loadMore(++currentPage, isAll);
            }
        });

        lRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
            @Override
            public void reload() {
                careerTalkPresenter.loadMore(currentPage, isAll);
            }
        });

        lRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });

        careerTalkPresenter.showCareerTalkList(1, true, isAll);
    }


    @Override
    public void showLoading() {
        loadingDialog = new LoadingDialog(context)
                .setLoadingText("加载中...");
        loadingDialog.show();
    }

    @Override
    public void closeLoading() {
        if (loadingDialog != null){
            loadingDialog.dismiss();
        }
    }

    @Override
    public void showMessage(String msg) {
        SnackbarUtils.showShortSnackbar(rootView, msg);
    }

    @Override
    public void showCareerTalkList(List<CareerTalk> careerTalkList) {
        this.careerTalkList.clear();
        this.careerTalkList.addAll(careerTalkList);
        lRecyclerView.refreshComplete(careerTalkList.size());
        lRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadMore(List<CareerTalk> careerTalkList) {
        int start = this.careerTalkList.size() + 1;
        this.careerTalkList.addAll(careerTalkList);
        lRecyclerView.refreshComplete(careerTalkList.size());
        lRecyclerViewAdapter.notifyItemRangeInserted(start, careerTalkList.size());
    }

    @OnClick({R.id.toolbar_back, R.id.toolbar_left_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.toolbar_left_text:
                isAll = !isAll;
                if (isAll){
                    toolbarLeftText.setText("省内");
                }else {
                    toolbarLeftText.setText("校内");
                }
                currentPage = 1;
                careerTalkPresenter.showCareerTalkList(currentPage, true, isAll);
                break;
        }
    }
}
