package cn.nicolite.huthelper.view.customView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by 高沛 on 2017/4/8.
 */

public class VideoAvatarBehavior extends CoordinatorLayout.Behavior<ImageView> {
    private static final String TAG = "VideoAvatarBehavior";
    // 缩放动画变化的支点
    private static final float ANIM_CHANGE_POINT = 0.2f;
    // 整个滚动的范围
    private int mTotalScrollRange;
    // AppBarLayout高度
    private int mAppBarHeight;
    // AppBar的起始Y坐标
    private float mAppBarStartY;
    // 滚动执行百分比[0~1]
    private float mPercent;

    public VideoAvatarBehavior() {
    }

    public VideoAvatarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, ImageView child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, final ImageView child, View dependency) {
        if (dependency instanceof AppBarLayout) {
            _initVariables(dependency);

            mPercent = (mAppBarStartY - dependency.getY()) * 1.0f / mTotalScrollRange;

            if (mPercent > ANIM_CHANGE_POINT) {
                child.setVisibility(View.GONE);
                // hide(child);
//                child.animate()
//                        .scaleX(0f)
//                        .scaleY(0f)
//                        .alpha(0f)
//                        .setDuration(200)
//                        .setInterpolator(new FastOutLinearInInterpolator())
//                        .setListener(new AnimatorListenerAdapter() {
//                            private boolean mCancelled;
//
//                            @Override
//                            public void onAnimationStart(Animator animation) {
//                                mCancelled = false;
//                            }
//
//                            @Override
//                            public void onAnimationCancel(Animator animation) {
//                                mCancelled = true;
//                            }
//
//                            @Override
//                            public void onAnimationEnd(Animator animation) {
//                                if (!mCancelled) {
//                                    child.setVisibility(View.GONE);
//                                }
//                            }
//                        }).start();
            } else {
                child.setVisibility(View.VISIBLE);

//                if (child.getVisibility() != View.VISIBLE) {
//                    // If the view isn't visible currently, we'll animate it from a single pixel
//                    child.setAlpha(0f);
//                    child.setScaleY(0f);
//                    child.setScaleX(0f);
//                }
//                child.animate()
//                        .scaleX(1f)
//                        .scaleY(1f)
//                        .alpha(1f)
//                        .setDuration(200)
//                        .setInterpolator(new LinearOutSlowInInterpolator())
//                        .setListener(new AnimatorListenerAdapter() {
//                            @Override
//                            public void onAnimationStart(Animator animation) {
//                                child.setVisibility(View.VISIBLE);
//                            }
//
//                            @Override
//                            public void onAnimationEnd(Animator animation) {
//                            }
//                        }).start();
            }
        }
        return false;
    }

    /**
     * 初始化变量
     *
     * @param dependency
     */
    private void _initVariables(View dependency) {
        if (mAppBarHeight == 0) {
            mAppBarHeight = dependency.getHeight();
            mAppBarStartY = dependency.getY();
        }
        if (mTotalScrollRange == 0) {
            mTotalScrollRange = ((AppBarLayout) dependency).getTotalScrollRange();
        }

    }

    private void hide(final ImageView view) {
        view.animate()
                .scaleX(0f)
                .scaleY(0f)
                .alpha(0f)
                .setDuration(200)
                .setInterpolator(new FastOutLinearInInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    private boolean mCancelled;

                    @Override
                    public void onAnimationStart(Animator animation) {
                        mCancelled = false;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        mCancelled = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (!mCancelled) {
                            view.setVisibility(View.GONE);
                        }
                    }
                }).start();
    }

    private void show(final ImageView view) {
        if (view.getVisibility() != View.VISIBLE) {
            // If the view isn't visible currently, we'll animate it from a single pixel
            view.setAlpha(0f);
            view.setScaleY(0f);
            view.setScaleX(0f);
        }
        view.animate()
                .scaleX(1f)
                .scaleY(1f)
                .alpha(1f)
                .setDuration(200)
                .setInterpolator(new LinearOutSlowInInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        view.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                    }
                }).start();
    }
}
