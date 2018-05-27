package cn.nicolite.huthelper.view.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.BaseActivity;
import cn.nicolite.huthelper.utils.ButtonUtils;
import cn.nicolite.huthelper.utils.CommUtil;
import cn.nicolite.huthelper.utils.ListUtils;
import cn.nicolite.huthelper.utils.SnackbarUtils;
import cn.nicolite.huthelper.utils.ToastUtil;
import cn.nicolite.huthelper.view.adapter.ShowImageAdapter;

/**
 * Created by nicolite on 17-10-19.
 */

public class ShowImageActivity extends BaseActivity {
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.rootView)
    FrameLayout rootView;

    private ArrayList<String> images;
    private int currentPosition;

    @Override
    protected void initConfig(Bundle savedInstanceState) {
        setImmersiveStatusBar(true);
        setLayoutNoLimits(true);
        setSlideExit(true);
    }

    @Override
    protected void initBundleData(Bundle bundle) {
        images = bundle.getStringArrayList("images");
        currentPosition = bundle.getInt("curr");
        if (ListUtils.isEmpty(images)) {
            ToastUtil.showToastShort("获取数据出错");
            finish();
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_show_image;
    }

    @Override
    protected void doBusiness() {
        toolbarTitle.setText(String.valueOf((currentPosition + 1) + "/" + images.size()));
        viewpager.setAdapter(new ShowImageAdapter(context, images));
        viewpager.setCurrentItem(currentPosition);

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                toolbarTitle.setText(String.valueOf((position + 1) + "/" + images.size()));
                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @OnClick({R.id.toolbar_back, R.id.toolbar_download})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.toolbar_download:
                if (!ButtonUtils.isFastDoubleClick()) {
                    CommUtil.downloadBitmap(context, images.get(currentPosition));
                }else {
                    SnackbarUtils.showShortSnackbar(rootView, "你点的太快了！");
                }
                break;
        }
    }
}
