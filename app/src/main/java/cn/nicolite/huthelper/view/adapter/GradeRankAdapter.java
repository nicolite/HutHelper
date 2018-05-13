package cn.nicolite.huthelper.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.nicolite.huthelper.model.bean.GradeRank;
import cn.nicolite.huthelper.view.customView.LineChartView;

/**
 * Created by nicolite on 17-11-12.
 */

public class GradeRankAdapter extends PagerAdapter {
    private Context context;
    private List<GradeRank> xnRank;
    private List<GradeRank> xqRank;

    public GradeRankAdapter(Context context, List<GradeRank> xnRank, List<GradeRank> xqRank) {
        this.context = context;
        this.xnRank = xnRank;
        this.xqRank = xqRank;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LineChartView view = new LineChartView(context);
        if (position == 0) {
            view.setRankingData(xqRank, true);
        } else {
            view.setRankingData(xnRank, false);
        }
        container.addView(view);
        return view;
    }

}
