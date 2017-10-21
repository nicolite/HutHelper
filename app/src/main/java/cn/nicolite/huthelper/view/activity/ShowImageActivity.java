package cn.nicolite.huthelper.view.activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.activity.BaseActivity;
import cn.nicolite.huthelper.utils.ListUtils;
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
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rootView)
    CoordinatorLayout rootView;
    private ArrayList<String> images;
    private int currentPosition;

    @Override
    protected void initConfig(Bundle savedInstanceState) {
        setImmersiveStatusBar(true);
    }

    @Override
    protected void initBundleData(Bundle bundle) {
        images = bundle.getStringArrayList("images");
        currentPosition = bundle.getInt("curr");
        if (ListUtils.isEmpty(images)){
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
        toolbarTitle.setText((currentPosition + 1) + "/" + images.size());
        viewpager.setAdapter(new ShowImageAdapter(context, images));
        viewpager.setCurrentItem(currentPosition);

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                toolbarTitle.setText((position + 1) + "/" + images.size());
                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
