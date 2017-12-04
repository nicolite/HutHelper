package cn.nicolite.huthelper.view.activity;

import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.activity.BaseActivity;
import cn.nicolite.huthelper.model.bean.Video;
import cn.nicolite.huthelper.presenter.VideoPresenter;
import cn.nicolite.huthelper.utils.SnackbarUtils;
import cn.nicolite.huthelper.view.adapter.VideoAdapter;
import cn.nicolite.huthelper.view.iview.IVideoView;
import cn.nicolite.huthelper.view.widget.LoadingDialog;

/**
 * 视频专栏
 * Created by nicolite on 17-12-2.
 */

public class VideoActivity extends BaseActivity implements IVideoView {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.lRecyclerView)
    LRecyclerView lRecyclerView;
    @BindView(R.id.rootView)
    LinearLayout rootView;
    private LoadingDialog loadingDialog;
    private VideoPresenter videoPresenter;
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private List<Video.LinksBean> videoList = new ArrayList<>();
    private Video video;

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
        return R.layout.activity_video;
    }

    @Override
    protected void doBusiness() {
        toolbarTitle.setText("视频专栏");
        lRecyclerView.setLayoutManager(new GridLayoutManager(context, 2, OrientationHelper.VERTICAL, false));
        lRecyclerViewAdapter = new LRecyclerViewAdapter(new VideoAdapter(context, videoList));
        lRecyclerView.setAdapter(lRecyclerViewAdapter);
        lRecyclerView.setLoadMoreEnabled(false);

        lRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("url", video.get_$480P());
                bundle.putSerializable("link", video.getLinks().get(position));
                Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, "videoTransition").toBundle();
                startActivity(VideoItemActivity.class, bundle, options);
            }
        });

        lRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                videoPresenter.showVideoList(true);
            }
        });

        videoPresenter = new VideoPresenter(this, this);
        videoPresenter.showVideoList(false);
    }

    @OnClick({R.id.toolbar_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
        }
    }

    @Override
    public void showLoading() {
        loadingDialog = new LoadingDialog(context)
                .setLoadingText("加载中...");
        loadingDialog.show();
    }

    @Override
    public void closeLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void showMessage(String msg) {
        SnackbarUtils.showShortSnackbar(rootView, msg);
    }

    @Override
    public void showVideoList(Video video) {
        this.video = video;
        videoList.clear();
        videoList.addAll(video.getLinks());
        lRecyclerView.refreshComplete(video.getLinks().size());
        lRecyclerViewAdapter.notifyDataSetChanged();
    }
}
