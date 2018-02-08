package cn.nicolite.huthelper.utils;

import android.app.Activity;
import android.graphics.Color;

import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrListener;
import com.r0adkll.slidr.model.SlidrPosition;

/**
 * Created by nicolite on 17-6-23.
 */

public class SlidrUtils {

    /**
     * 绑定滑动退出，使用默认配置
     * @param activity
     */
    public static void setSlidrExit(Activity activity){
        Slidr.attach(activity, getSlidrConfig());
    }

    /**
     * 绑定滑动退出，自定义配置
     * @param activity
     * @param slidrConfig
     */
    public static void setSlidrExit(Activity activity, SlidrConfig slidrConfig){
        Slidr.attach(activity, slidrConfig);
    }

    /**
     * Slidr默认配置
     * @return
     */
    private static SlidrConfig getSlidrConfig(){
        return new SlidrConfig.Builder()
                // .primaryColor(getResources().getColor(R.color.primary)
                // .secondaryColor(getResources().getColor(R.color.secondary)
                .position(SlidrPosition.LEFT)
                .sensitivity(1f)
                .scrimColor(Color.BLACK)
                .scrimStartAlpha(0.8f)
                .scrimEndAlpha(0f)
                .velocityThreshold(2400)
                .distanceThreshold(0.25f)
                .edge(true)
                .edgeSize(0.18f) // The % of the screen that counts as the edge, default 18%
                .listener(new SlidrListener() {
                    @Override
                    public void onSlideStateChanged(int state) {

                    }

                    @Override
                    public void onSlideChange(float percent) {

                    }

                    @Override
                    public void onSlideOpened() {

                    }

                    @Override
                    public void onSlideClosed() {

                    }
                })
                .build();
    }
}
