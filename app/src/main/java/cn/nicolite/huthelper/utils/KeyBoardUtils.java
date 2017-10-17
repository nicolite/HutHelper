package cn.nicolite.huthelper.utils;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * 键盘工具类
 * Created by nicolite on 17-8-28.
 */

public class KeyBoardUtils {
    /**
     * 将布局滑动键盘遮住部分的高度
     *
     * @param root 根布局
     * @param scrollToView 最后可见的控件,外层不可以有布局包裹
     */
    public static void scrollLayoutAboveKeyBoard(final Context context, final View root, final View scrollToView) {

        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //获取root在窗体的可视区域
                root.getWindowVisibleDisplayFrame(rect);
                //获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
                //若不可视区域高度大于400，则键盘显示
                if (rootInvisibleHeight > 400) {
                    int[] location = new int[2];
                    //获取root在窗体的坐标
                    root.getLocationInWindow(location);
                    //计算root滚动高度，使scrollToView在可见区域
                    int srollHeight = (location[1] + root.getHeight())
                            - (root.getHeight() - scrollToView.getBottom())
                            + DensityUtils.dp2px(context, 5) - rect.bottom;

                    root.scrollTo(0, srollHeight);

                } else {
                    //键盘隐藏
                    root.scrollTo(0, 0);
                }
            }
        });
    }
}
