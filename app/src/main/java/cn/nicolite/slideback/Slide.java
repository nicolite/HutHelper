package cn.nicolite.slideback;


import android.app.Activity;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import cn.nicolite.huthelper.R;
import cn.nicolite.slideback.slidemodel.SlideInterface;
import cn.nicolite.slideback.slidemodel.SlidrConfig;
import cn.nicolite.slideback.widget.SlidePanel;


/**
 * This attacher class is used to attach the sliding mechanism to any {@link Activity}
 * that lets the user slide (or swipe) the activity away as a form of back or up action. The action
 * causes {@link Activity#finish()} to be called.
 */
public final class Slide {

    /**
     * Attach a slideable mechanism to an activity that adds the slide to dismiss functionality
     *
     * @param activity the activity to attach the slider to
     * @return a {@link SlideInterface} that allows
     * the user to lock/unlock the sliding mechanism for whatever purpose.
     */
    @NonNull
    public static SlideInterface attach(@NonNull Activity activity) {
        return attach(activity, -1, -1);
    }


    /**
     * Attach a slideable mechanism to an activity that adds the slide to dismiss functionality
     * and allows for the statusbar to transition between colors
     *
     * @param activity        the activity to attach the slider to
     * @param statusBarColor1 the primaryDark status bar color of the interface that this will slide back to
     * @param statusBarColor2 the primaryDark status bar color of the activity this is attaching to that will transition
     *                        back to the statusBarColor1 color
     * @return a {@link SlideInterface} that allows
     * the user to lock/unlock the sliding mechanism for whatever purpose.
     */
    @NonNull
    public static SlideInterface attach(@NonNull Activity activity, @ColorInt int statusBarColor1,
                                        @ColorInt int statusBarColor2) {

        // Setup the slider panel and attach it to the decor
        final SlidePanel panel = attachSliderPanel(activity, null);

        // Set the panel slide listener for when it becomes closed or opened
        panel.setOnPanelSlideListener(new ColorPanelSlideListener(activity, statusBarColor1, statusBarColor2));

        // Return the lock interface
        return panel.getDefaultInterface();
    }


    /**
     * Attach a slider mechanism to an activity based on the passed {@link SlidrConfig}
     *
     * @param activity the activity to attach the slider to
     * @param config   the slider configuration to make
     * @return a {@link SlideInterface} that allows
     * the user to lock/unlock the sliding mechanism for whatever purpose.
     */
    @NonNull
    public static SlideInterface attach(@NonNull Activity activity, @NonNull SlidrConfig config) {

        // Setup the slider panel and attach it to the decor
        final SlidePanel panel = attachSliderPanel(activity, config);

        // Set the panel slide listener for when it becomes closed or opened
        panel.setOnPanelSlideListener(new ConfigPanelSlideListener(activity, config));

        // Return the lock interface
        return panel.getDefaultInterface();
    }


    /**
     * Attach a new {@link SlidePanel} to the root of the activity's content
     */
    @NonNull
    private static SlidePanel attachSliderPanel(@NonNull Activity activity, @NonNull SlidrConfig config) {
        // Hijack the decorview
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        View oldScreen = decorView.getChildAt(0);
        decorView.removeViewAt(0);

        // Setup the slider panel and attach it to the decor
        SlidePanel panel = new SlidePanel(activity, oldScreen, config);
        panel.setId(R.id.slidable_panel);
        oldScreen.setId(R.id.slidable_content);
        panel.addView(oldScreen);
        decorView.addView(panel, 0);
        return panel;
    }


    /**
     * Attach a slider mechanism to a fragment view replacing an internal view
     *
     * @param oldScreen the view within a fragment to replace
     * @param config    the slider configuration to attach with
     * @return a {@link SlideInterface} that allows
     * the user to lock/unlock the sliding mechanism for whatever purpose.
     */
    @NonNull
    public static SlideInterface replace(@NonNull final View oldScreen, @NonNull final SlidrConfig config) {
        ViewGroup parent = (ViewGroup) oldScreen.getParent();
        ViewGroup.LayoutParams params = oldScreen.getLayoutParams();
        parent.removeView(oldScreen);

        // Setup the slider panel and attach it
        final SlidePanel panel = new SlidePanel(oldScreen.getContext(), oldScreen, config);
        panel.setId(R.id.slidable_panel);
        oldScreen.setId(R.id.slidable_content);

        panel.addView(oldScreen);
        parent.addView(panel, 0, params);

        // Set the panel slide listener for when it becomes closed or opened
        panel.setOnPanelSlideListener(new FragmentPanelSlideListener(oldScreen, config));

        // Return the lock interface
        return panel.getDefaultInterface();
    }

}
