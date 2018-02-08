package cn.nicolite.huthelper.view.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.view.adapter.BaseViewPagerAdapter;

/**
 * Created by 高沛 on 16-10-31.
 */

public class AutoScrollViewPager extends RelativeLayout {

    private Context context;

    private int pointLayout;

    private AutoViewPager viewPager;
    private LinearLayout layout;

    public AutoScrollViewPager(Context context) {
        this(context, null);
    }

    public AutoScrollViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public AutoScrollViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        viewPager = new AutoViewPager(context);
        layout = new LinearLayout(context);
        addView(viewPager);
    }

    public void setAdpter(BaseViewPagerAdapter viewPagerAdapter) {
        if (viewPager != null) {
            viewPager.init(viewPager, viewPagerAdapter);
        }
    }

    public AutoViewPager getViewPager() {
        return viewPager;
    }

    public void initPointView(int size) {
        layout = new LinearLayout(context);
        for (int i = 0; i < size; i++) {
            ImageView imageView = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
            params.leftMargin = 8;
            params.gravity = Gravity.CENTER;
            imageView.setLayoutParams(params);
            if (i == 0) {
                imageView.setBackgroundResource(R.drawable.ic_point_checked);
            } else {
                imageView.setBackgroundResource(R.drawable.ic_point_normal);
            }
            layout.addView(imageView);
        }
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(CENTER_HORIZONTAL);

        layoutParams.setMargins(12, 20, 12, 20);
        layout.setLayoutParams(layoutParams);
        addView(layout);
    }

    public void updatePointView(int position) {
        int size = layout.getChildCount();
        for (int i = 0; i < size; i++) {
            ImageView imageView = (ImageView) layout.getChildAt(i);
            if (i == position) {
                imageView.setBackgroundResource(R.drawable.ic_point_checked);
            } else {
                imageView.setBackgroundResource(R.drawable.ic_point_normal);
            }
        }
    }

    public void onDestroy() {
        if (viewPager != null) {
            viewPager.onDestroy();
        }
    }
}
