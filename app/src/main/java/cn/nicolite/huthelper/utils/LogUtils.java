package cn.nicolite.huthelper.utils;

/**
 * Created by nicolite on 17-6-24.
 * 用来控制Log输出，、、
 */

public class LogUtils {

    public static void v(String tag, String msg) {
        cn.nicolite.mvp.utils.LogUtils.v(tag, msg);
    }

    public static void d(String tag, String msg) {
        cn.nicolite.mvp.utils.LogUtils.d(tag, msg);

    }

    public static void i(String tag, String msg) {
        cn.nicolite.mvp.utils.LogUtils.i(tag, msg);

    }

    public static void e(String tag, String msg) {
        cn.nicolite.mvp.utils.LogUtils.e(tag, msg);
    }

    public static void v(String tag, String msg, Throwable throwable) {
        cn.nicolite.mvp.utils.LogUtils.v(tag, msg, throwable);
    }

    public static void d(String tag, String msg, Throwable throwable) {
        cn.nicolite.mvp.utils.LogUtils.d(tag, msg, throwable);

    }

    public static void i(String tag, String msg, Throwable throwable) {
        cn.nicolite.mvp.utils.LogUtils.i(tag, msg, throwable);

    }

    public static void e(String tag, String msg, Throwable throwable) {
        cn.nicolite.mvp.utils.LogUtils.e(tag, msg, throwable);
    }
}
