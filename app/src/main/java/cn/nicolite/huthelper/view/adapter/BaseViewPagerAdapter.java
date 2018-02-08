package cn.nicolite.huthelper.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import cn.nicolite.huthelper.view.customView.AutoViewPager;

/**
 * Created by gaop on 16-10-31.
 */

public abstract class BaseViewPagerAdapter<T> extends PagerAdapter implements ViewPager.OnPageChangeListener {

    private List<T> datas = new ArrayList<>();

    private Context context;

    private AutoViewPager viewPager;

    private OnViewPagerItemClickListener listener;

    public BaseViewPagerAdapter(List<T> list) {
        this.datas = list;
    }

    public BaseViewPagerAdapter(Context context, List<T> list) {
        this.datas = list;
        this.context = context;
    }

    public BaseViewPagerAdapter(Context context, List<T> data, OnViewPagerItemClickListener listener) {
        this.context = context;
        this.datas = data;
        this.listener = listener;
    }


    public void init(AutoViewPager viewPager, BaseViewPagerAdapter baseViewPagerAdapter) {
        this.viewPager = viewPager;
        viewPager.setAdapter(this);
        viewPager.addOnPageChangeListener(this);

        if (datas == null || datas.size() == 0) {
            return;
        }

        int position = Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2) % getRealCount();
        viewPager.setCurrentItem(position);
        viewPager.start();
        viewPager.updatePointView(getRealCount());
    }

    public int getRealCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public int getCount() {
        return (datas == null || datas.size() == 0 ) ? 0 : Integer.MAX_VALUE;
       // return (datas == null) ? 0 : datas.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView img = new ImageView(context);
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(position % getRealCount(), datas.get(position % getRealCount()));
                }
            }
        });

       loadImage(img, position, datas.get(position % getRealCount()));
        container.addView(img);
        return img;
    }

    public abstract void loadImage(ImageView view, int position, T t);

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        viewPager.onPageSelected(position % getRealCount());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public interface OnViewPagerItemClickListener<T> {
        void onItemClick(int position, T t);
    }
}
