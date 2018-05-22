package cn.nicolite.huthelper.view.customView;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;

import java.util.Timer;
import java.util.TimerTask;

import cn.nicolite.huthelper.view.adapter.BaseViewPagerAdapter;

/**
 * Created by 高沛 on 16-10-30.
 */

public class AutoViewPager extends ViewPager {

    private static final String TAG = "AutoViewPager";

    private int currentItem;

    private Timer timer;

    public AutoViewPager(Context context) {
        super(context);
    }

    public void init(AutoViewPager viewPager, BaseViewPagerAdapter adapter){
        adapter.init(viewPager,adapter);
    }

    public void start() {
        onStop();
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(new AutoTask(), 5000, 5000);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            currentItem = getCurrentItem();
            if (currentItem == getAdapter().getCount() - 1) {
                currentItem = 0;
            } else {
                currentItem++;
            }
            setCurrentItem(currentItem);
        }
    };

    public void updatePointView(int size){
        if(getParent() instanceof AutoScrollViewPager){
            AutoScrollViewPager pager= (AutoScrollViewPager) getParent();
            pager.initPointView(size);
        }
    }

    private Handler handler = new Handler();

    public void onPageSelected(int position) {
        AutoScrollViewPager page = (AutoScrollViewPager) getParent();
        page.updatePointView(position);
    }

    private class AutoTask extends TimerTask {
        @Override
        public void run() {
            handler.post(runnable);
        }
    }

    public void onStop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void onDestroy() {
        onStop();
    }

    public void onResume() {
        start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onStop();
                break;
            case MotionEvent.ACTION_UP:
                onResume();
                break;
        }
        return super.onTouchEvent(ev);
    }
}
