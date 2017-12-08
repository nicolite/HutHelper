package cn.nicolite.huthelper.utils;

import android.util.Log;

import cn.nicolite.huthelper.BuildConfig;

/**
 * Created by nicolite on 17-6-24.
 * 用来控制Log输出，、、
 */

public class LogUtils {

    private static final int VERBOSE = 1;
    private static  final int DEBUG = 2;
    private static final int INFO = 3;
    private static final int WARN = 4;
    private static final int  ERROR = 5;
    private static final int NOTHING = 6;
    private static final int LEVEL = VERBOSE; //将LEVEL设置为NOTHING就不会输出任何日志

    public static void v(String tag, String msg) {
        if (BuildConfig.LOG_DEBUG && LEVEL <= VERBOSE) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (BuildConfig.LOG_DEBUG && LEVEL <= DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (BuildConfig.LOG_DEBUG && LEVEL <= INFO) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (BuildConfig.LOG_DEBUG && LEVEL <= WARN) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (BuildConfig.LOG_DEBUG && LEVEL <= ERROR) {
            Log.e(tag, msg);
        }
    }

    public static void v(String tag, String msg, Throwable throwable) {
        if (BuildConfig.LOG_DEBUG && LEVEL <= VERBOSE) {
            Log.v(tag, msg, throwable);
        }
    }

    public static void d(String tag, String msg, Throwable throwable) {
        if (BuildConfig.LOG_DEBUG && LEVEL <= DEBUG) {
            Log.d(tag, msg, throwable);
        }
    }

    public static void i(String tag, String msg, Throwable throwable) {
        if (BuildConfig.LOG_DEBUG && LEVEL <= INFO) {
            Log.i(tag, msg, throwable);
        }
    }

    public static void w(String tag, String msg, Throwable throwable) {
        if (BuildConfig.LOG_DEBUG && LEVEL <= WARN) {
            Log.w(tag, msg, throwable);
        }
    }

    public static void e(String tag, String msg, Throwable throwable) {
        if (BuildConfig.LOG_DEBUG && LEVEL <= ERROR) {
            Log.e(tag, msg, throwable);
        }
    }
}
