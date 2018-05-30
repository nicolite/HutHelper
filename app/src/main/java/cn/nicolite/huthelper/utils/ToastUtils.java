package cn.nicolite.huthelper.utils;

import android.content.Context;
import android.widget.Toast;

import cn.nicolite.huthelper.app.MApplication;

/**
 * 弹窗工具
 * Created by gaop1 on 2016/7/25.
 */

public class ToastUtils {

    public static void showToastLong(String msg) {
        showToast(MApplication.appContext, msg, Toast.LENGTH_LONG);
    }

    public static void showToastLong(int msg) {
        showToast(MApplication.appContext, "" + msg, Toast.LENGTH_LONG);
    }

    public static void showToastShort(String msg) {
        showToast(MApplication.appContext, msg, Toast.LENGTH_SHORT);
    }

    public static void showToastShort(int msg) {
        showToast(MApplication.appContext, "" + msg, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, String msg, int durstion) {
        Toast.makeText(context, msg, durstion).show();
    }
}
