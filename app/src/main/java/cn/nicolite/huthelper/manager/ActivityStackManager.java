package cn.nicolite.huthelper.manager;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Process;

import java.util.Stack;

/**
 * Activity栈管理
 * Created by nicolite on 17-10-15.
 */

public class ActivityStackManager {
    private volatile static ActivityStackManager instance;
    private static Stack<Activity> activityStack;

    /**
     * 私有构造
     */
    private ActivityStackManager() {
        activityStack = new Stack<>();
    }

    /**
     * 单例
     *
     * @return
     */
    public static ActivityStackManager getManager() {
        if (instance == null) {
            synchronized (ActivityStackManager.class) {
                if (instance == null) {
                    instance = new ActivityStackManager();
                }
            }
        }
        return instance;
    }

    /**
     * 压栈
     *
     * @param activity
     */
    public void push(Activity activity) {
        activityStack.add(activity);
    }

    /**
     * 出栈
     *
     * @return
     */
    public Activity pop() {
        if (activityStack.isEmpty()) {
            return null;
        }
        return activityStack.pop();
    }

    /**
     * 获取栈顶元素
     *
     * @return
     */
    public Activity peek() {
        if (activityStack.isEmpty()) {
            return null;
        }
        return activityStack.peek();
    }

    /**
     * 出栈该元素和该元素直上的元素
     *
     * @param activity
     */
    public void remove(Activity activity) {

        if (activityStack.isEmpty()) {
            return;
        }

        if (activity == activityStack.peek()) {
            activityStack.pop();
        } else {
            activityStack.remove(activity);
        }
    }

    /**
     * 判断指定元素是否在栈内
     *
     * @param activity
     * @return
     */
    public boolean contains(Activity activity) {
        return activityStack.contains(activity);
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        while (!activityStack.isEmpty()) {
            activityStack.pop().finish();
        }
    }

    /**
     * 退出App
     *
     * @param context
     */
    public void exitApp(Context context) {

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            activityManager.killBackgroundProcesses(context.getPackageName());
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancelAll();
        }

        finishAllActivity();

        Process.killProcess(Process.myPid());
    }
}
