package cn.nicolite.huthelper.utils;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

/**
 * 键盘工具类
 * Created by nicolite on 17-8-28.
 */

public class KeyBoardUtils {
    /**
     * 将布局滑动键盘遮住部分的高度
     *
     * @param root 根布局
     * @param scrollToView 最后可见的控件,外层不可以有布局包裹，必须为根布局的直接子布局
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

    /**
     * 显示软件盘
     * @param context
     * @param view 需要输入的view，必须为EditText或者它的子类，必须可见且能够获取焦点
     */
    public static void showSoftInput(Context context, View view){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null){
            imm.showSoftInput(view, 0);
        }
    }

    /**
     * 关闭软键盘
     * @param context
     * @param window
     */
    public static void hideSoftInput(Context context, Window window){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null){
            imm.hideSoftInputFromWindow(window.getDecorView().getWindowToken(), 0);
        }
    }
}
