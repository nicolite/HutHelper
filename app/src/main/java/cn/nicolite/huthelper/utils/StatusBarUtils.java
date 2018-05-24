package cn.nicolite.huthelper.utils;

import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 设置状态栏颜色为深色
 * Created by nicolite on 17-8-10.
 */

public class StatusBarUtils {
    private static final String TAG = "StatusBarUtils";

    /**
     * 设置状态栏颜色为深色，支持MIUIV6+，FLYME4.0+，Android 6.0+
     *
     * @param window
     */

    public static void setDeepColorStatusBar(Window window) {
        if (AndroidMStatusBarLightMode(window, true)) {
            LogUtils.d(TAG, "setDeepColorStatusBar: " + "Android 6.0+");
        }
        if (MIUIStatusBarLightMode(window, true)) {
            LogUtils.d(TAG, "setDeepColorStatusBar: " + "MIUI");
        } else if (FlymeStatusBarLightMode(window, true)) {
            LogUtils.d(TAG, "setDeepColorStatusBar: " + "Flyme");
        } else {
            // 透明状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            LogUtils.d(TAG, "setDeepColorStatusBar: " + "not supported device");
        }
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格，Flyme4.0以上
     * 可以用来判断是否为Flyme用户
     *
     * @param window      需要设置的窗口
     * @param isDeepColor 是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    private static boolean FlymeStatusBarLightMode(Window window, boolean isDeepColor) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (isDeepColor) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {
                LogUtils.d(TAG, "FlymeStatusBarLightMode: " + e.toString());
            }
        }
        return result;
    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     *
     * @param window      需要设置的窗口
     * @param isDeepColor 是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    private static boolean MIUIStatusBarLightMode(Window window, boolean isDeepColor) {
        boolean result = false;
        if (window != null) {
            Class<?> clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (isDeepColor) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception e) {
                LogUtils.d(TAG, "MIUIStatusBarLightMode: " + e.toString());
            }
        }
        return result;
    }

    private static boolean AndroidMStatusBarLightMode(Window window, boolean isDeepColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && isDeepColor) {
            //必须清除这个flag
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(Color.TRANSPARENT);
            return true;
        }
        return false;
    }

    public static void setImmersiveStatusBar(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //透明状态栏
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        } else {
            LogUtils.d(TAG, "该系统版本不支持设置沉浸状态栏");
        }
    }

}
