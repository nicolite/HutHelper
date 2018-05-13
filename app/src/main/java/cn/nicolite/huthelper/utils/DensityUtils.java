package cn.nicolite.huthelper.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * 尺寸转换工具
 * Created by gaop1 on 2016/7/10.
 */
public class DensityUtils {
    /**
     * dpתpx
     *
     * @param context
     * @return
     */
    public static int dp2px(Context context, float dpVal)
    {

        //TODO 此处居然报context空指针错误
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * spתpx
     *
     * @param context
     * @return
     */
    public static int sp2px(Context context, float spVal)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * pxתdp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static float px2dp(Context context, float pxVal)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * pxתsp
     *
     * @param pxVal
     * @return
     */
    public static float px2sp(Context context, float pxVal)
    {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }

}
