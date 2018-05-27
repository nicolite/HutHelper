package cn.nicolite.huthelper.view.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.BaseActivity;
import cn.nicolite.huthelper.model.bean.Video;
import cn.nicolite.huthelper.utils.CommUtil;
import cn.nicolite.huthelper.utils.ToastUtils;
import cn.nicolite.huthelper.view.adapter.VideoItemAdapter;
import cn.nicolite.huthelper.view.customView.CommonDialog;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.ColorFilterTransformation;

/**
 * Created by nicolite on 17-12-4.
 */

public class VideoItemActivity extends BaseActivity {
    @BindView(R.id.iv_videoitem_bg)
    ImageView ivVideoitemBg;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.iv_videoitem_img)
    ImageView ivVideoitemImg;
    @BindView(R.id.rl_videoitem)
    RecyclerView rlVideoitem;
    private Video.LinksBean linksBean;
    private String url;

    @Override
    protected void initConfig(Bundle savedInstanceState) {
        setImmersiveStatusBar(true);
        setSlideExit(true);
    }

    @Override
    protected void initBundleData(Bundle bundle) {
        if (bundle != null) {
            url = bundle.getString("url");
            linksBean = (Video.LinksBean) bundle.getSerializable("link");
        } else {
            ToastUtils.showToastShort("数据异常");
            finish();
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_video_item;
    }

    @Override
    protected void doBusiness() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(linksBean.getName());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        VideoItemAdapter adapter = new VideoItemAdapter(context, linksBean.getVedioList());
        adapter.setOnItemClickListener(new VideoItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, long itemId) {
                String path = url + linksBean.getVedioList().get(position).getUrl();
                if (CommUtil.isWifiConnected(VideoItemActivity.this)) {
                    startPlay(path);
                } else {
                    showDialog(path);
                }
            }
        });

        rlVideoitem.setLayoutManager(new GridLayoutManager(context, 3));
        rlVideoitem.setAdapter(adapter);

        Glide
                .with(activity)
                .load(linksBean.getImg())
                .skipMemoryCache(true)
                .crossFade()
                .into(ivVideoitemImg);
        Glide
                .with(activity)
                .load(linksBean.getImg())
                .skipMemoryCache(true)
                .crossFade()
                .bitmapTransform(new BlurTransformation(context, 25),
                        new ColorFilterTransformation(context, 0x21000000))
                .into(ivVideoitemBg);
    }

    private void startPlay(String url) {
        Bundle bundle = new Bundle();
        bundle.putString("video_path", url);
        startActivity(VideoH5PlayerActivity.class, bundle);
    }

    private void showDialog(final String url) {
        final CommonDialog commonDialog = new CommonDialog(context);
        commonDialog
                .setTitle("提示")
                .setMessage("当前非WIFI网络，是否继续播放？")
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startPlay(url);
                        commonDialog.dismiss();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }
}
