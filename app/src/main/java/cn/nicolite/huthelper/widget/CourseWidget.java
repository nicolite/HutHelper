package cn.nicolite.huthelper.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.app.MApplication;
import cn.nicolite.huthelper.db.DaoHelper;
import cn.nicolite.huthelper.db.dao.LessonDao;
import cn.nicolite.huthelper.model.bean.Lesson;
import cn.nicolite.huthelper.utils.CommUtil;
import cn.nicolite.huthelper.utils.DateUtils;
import cn.nicolite.huthelper.utils.LogUtils;
import cn.nicolite.huthelper.view.activity.SplashActivity;
import cn.nicolite.huthelper.view.activity.SyllabusActivity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by 高沛 on 16-11-15.
 * 课程表小部件
 */

public class CourseWidget extends AppWidgetProvider {

    private List<Lesson> lessonList = DaoHelper.getDaoHelper(MApplication.AppContext).getDaoSession()
            .getLessonDao().queryBuilder().where(LessonDao.Properties.UserId.eq(getLoginUser())).list();

    private static int xqj = DateUtils.getWeekOfToday();

    public static final String TAG = "CourseWidgetProvider";

    public static final String COURSE_UP_ACTION = "cn.nicolite.huthelper.action_courseup";

    public static final String COURSE_DOWN_ACTION = "cn.nicolite.huthelper.action_coursedown";

    public static final String WEEK_PRE_ACTION = "cn.nicolite.huthelper.action_weekprevious";

    public static final String WEEK_NEXT_ACTION = "cn.nicolite.huthelper.action_weeknext";

    // public static final String START_COURSEACTIVITY_ACTION = "cn.nicolite.huthelper.action_startcouActivity";

    //public static final String START_MAINACTIVITY_ACTION="cn.nicolite.huthelper.action_startmainActivity";


    private static int currNum = 0;
    /**
     * 获取当前登录用户
     */
    protected String getLoginUser() {
        SharedPreferences preferences = MApplication.AppContext.getSharedPreferences("login_user", Context.MODE_PRIVATE);
        return preferences.getString("userId", null);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String week = "";
        switch (intent.getAction()) {
            case COURSE_UP_ACTION:
                currNum -= 2;
                //可能会出现负数。。。。。。
                if (currNum < 0)
                    currNum += 2;
                week = whatToday(false, 0);
                break;
            case COURSE_DOWN_ACTION:
                currNum += 2;
                week = whatToday(false, 0);
                break;
            case WEEK_PRE_ACTION:
                week = whatToday(false, 1);
                currNum = 0;
                break;
            case WEEK_NEXT_ACTION:
                week = whatToday(true, 1);
                currNum = 0;
                break;
        }
        RemoteViews remoteviews = new RemoteViews(context.getPackageName(), R.layout.course_widget);
        remoteviews.setTextViewText(R.id.tv_widget_week, week);
        init(context, remoteviews);
        //获得appwidget管理实例，用于管理appwidget以便进行更新操作
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        //相当于获得所有本程序创建的appwidget
        ComponentName componentName = new ComponentName(context, CourseWidget.class);
        //更新appwidget
        appWidgetManager.updateAppWidget(componentName, remoteviews);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int counter = appWidgetIds.length;
        for (int i = 0; i < counter; i++) {
            int appWidgetId = appWidgetIds[i];
            onWidgetUpdate(context, appWidgetManager, appWidgetId);
        }

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private void onWidgetUpdate(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.course_widget);
        remoteViews.setTextViewText(R.id.tv_widget_weeknum, "第" + DateUtils.getNowWeek() + "周");
        Intent intent = new Intent(context, CourseWidget.class);
        intent.setAction(WEEK_PRE_ACTION);
        PendingIntent pendingIntentpre = PendingIntent.getBroadcast(context, 0, intent, 0);
        Intent intent1 = new Intent(context, CourseWidget.class);
        intent1.setAction(WEEK_NEXT_ACTION);
        PendingIntent pendingIntentnnext = PendingIntent.getBroadcast(context, 0, intent1, 0);
        remoteViews.setOnClickPendingIntent(R.id.btn_widget_previous, pendingIntentpre);
        remoteViews.setOnClickPendingIntent(R.id.btn_widget_next, pendingIntentnnext);


        init(context, remoteViews);
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    private void init(Context context, RemoteViews views) {
        views.setTextViewText(R.id.tv_widget_week, whatToday(false, 0));
        List<Lesson> todayLesson = new ArrayList<>();
        Iterator it = lessonList.iterator();
        while (it.hasNext()) {
            Lesson l = (Lesson) it.next();
            if (!CommUtil.ifHaveCourse(l, DateUtils.getNowWeek())) {
                it.remove();
            } else {
                if (Integer.parseInt(l.getXqj()) == xqj) {
                    todayLesson.add(l);
                }
            }
        }
        int size = todayLesson.size();
        if (size == 0) {
            views.setViewVisibility(R.id.ll_widget_course1, GONE);
            views.setViewVisibility(R.id.ll_widget_course2, GONE);
            views.setViewVisibility(R.id.iv_widget_line, GONE);
            views.setViewVisibility(R.id.tv_widget_coure_empty, VISIBLE);

            Intent intent2 = new Intent(context, SplashActivity.class);
            PendingIntent pendingIntentcourse = PendingIntent.getActivity(context, 0, intent2, 0);
            views.setOnClickPendingIntent(R.id.tv_widget_coure_empty, pendingIntentcourse);
        } else {
            views.setViewVisibility(R.id.tv_widget_coure_empty, GONE);
            views.setViewVisibility(R.id.ll_widget_course1, VISIBLE);
            views.setViewVisibility(R.id.ll_widget_course2, VISIBLE);
            views.setViewVisibility(R.id.iv_widget_line, VISIBLE);
            Intent intent3 = new Intent(context, SyllabusActivity.class);
            PendingIntent pendingIntentmain = PendingIntent.getActivity(context, 0, intent3, 0);
            views.setOnClickPendingIntent(R.id.ll_widget_coursecontent1, pendingIntentmain);
            views.setOnClickPendingIntent(R.id.ll_widget_coursecontent2, pendingIntentmain);

            Collections.sort(todayLesson, new LessonComparator());
            int num1 = Integer.parseInt(todayLesson.get(currNum).getDjj());
            String name1 = todayLesson.get(currNum).getName();
            String room1 = todayLesson.get(currNum).getRoom();
            if (currNum + 2 <= size) {
                int num2 = Integer.parseInt(todayLesson.get(currNum + 1).getDjj());
                String name2 = todayLesson.get(currNum + 1).getName();
                String room2 = todayLesson.get(currNum + 1).getRoom();
                views.setTextViewText(R.id.tv_widget_course_num1, num1 + "-" + (num1 + 1));
                views.setTextViewText(R.id.tv_widget_course_num2, num2 + "-" + (num2 + 1));
                views.setTextViewText(R.id.tv_widget_course_name2, name2);
                views.setTextViewText(R.id.tv_widget_course_name1, name1);
                views.setTextViewText(R.id.tv_widget_course_classroom1, room1);
                views.setTextViewText(R.id.tv_widget_course_classroom2, room2);
            } else {
                views.setTextViewText(R.id.tv_widget_course_num1, num1 + "-" + (num1 + 1));
                views.setTextViewText(R.id.tv_widget_course_name1, name1);
                views.setTextViewText(R.id.tv_widget_course_classroom1, room1);
                views.setTextViewText(R.id.tv_widget_course_num2, "");
                views.setTextViewText(R.id.tv_widget_course_name2, "");
                views.setTextViewText(R.id.tv_widget_course_classroom2, "");
            }
            if (currNum == 0) {
                views.setViewVisibility(R.id.btn_widget_up, GONE);
                if (currNum + 3 <= size) {
                    Intent intent = new Intent(context, CourseWidget.class);
                    intent.setAction(COURSE_DOWN_ACTION);
                    PendingIntent pendingIntentdown = PendingIntent.getBroadcast(context, 0, intent, 0);
                    views.setOnClickPendingIntent(R.id.btn_widget_down, pendingIntentdown);
                    views.setViewVisibility(R.id.btn_widget_down, VISIBLE);
                } else {
                    views.setViewVisibility(R.id.btn_widget_down, GONE);
                }

            } else {
                Intent intent = new Intent(context, CourseWidget.class);
                intent.setAction(COURSE_UP_ACTION);
                PendingIntent pendingIntentup = PendingIntent.getBroadcast(context, 0, intent, 0);
                views.setOnClickPendingIntent(R.id.btn_widget_up, pendingIntentup);
                views.setViewVisibility(R.id.btn_widget_up, VISIBLE);
                if (currNum + 3 <= size) {
                    intent = new Intent(context, CourseWidget.class);
                    intent.setAction(COURSE_DOWN_ACTION);
                    PendingIntent pendingIntentdown = PendingIntent.getBroadcast(context, 0, intent, 0);
                    views.setOnClickPendingIntent(R.id.btn_widget_down, pendingIntentdown);
                    views.setViewVisibility(R.id.btn_widget_up, VISIBLE);
                } else {
                    views.setViewVisibility(R.id.btn_widget_down, GONE);
                }
            }


        }
    }

    //判断当前显示应该是星期几，
    //x 加减 判定符  ss 是否触发自动加减一的设定。
    public String whatToday(boolean x, int ss) {
        String xx = "erorr";
        if (x) {
            if (ss == 1) {
                xqj++;
                if (xqj > 7) xqj = xqj - 7;
            }

            switch (xqj) {
                case 1:
                    xx = "星期一";
                    break;
                case 2:
                    xx = "星期二";
                    break;
                case 3:
                    xx = "星期三";
                    break;
                case 4:
                    xx = "星期四";
                    break;
                case 5:
                    xx = "星期五";
                    break;
                case 6:
                    xx = "星期六";
                    break;
                case 7:
                    xx = "星期天";
                    break;
            }
        } else {
            if (ss == 1) {
                xqj--;
                if (xqj <= 0) xqj = 7;
            }

            switch (xqj) {
                case 1:
                    xx = "星期一";
                    break;
                case 2:
                    xx = "星期二";
                    break;
                case 3:
                    xx = "星期三";
                    break;
                case 4:
                    xx = "星期四";
                    break;
                case 5:
                    xx = "星期五";
                    break;
                case 6:
                    xx = "星期六";
                    break;
                case 7:
                    xx = "星期天";
                    break;
            }
        }

        return xx;
    }

    private static class LessonComparator implements Comparator<Lesson> {
        @Override
        public int compare(Lesson lhs, Lesson rhs) {
            LogUtils.e(TAG, "compare: " + lhs.getDjj() + rhs.getDjj());
            return Integer.parseInt(lhs.getDjj()) - Integer.parseInt(rhs.getDjj());
        }
    }
}
