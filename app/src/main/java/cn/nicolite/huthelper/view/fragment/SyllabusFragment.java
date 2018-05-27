package cn.nicolite.huthelper.view.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.BaseFragment;
import cn.nicolite.huthelper.db.dao.LessonDao;
import cn.nicolite.huthelper.model.Constants;
import cn.nicolite.huthelper.model.bean.Lesson;
import cn.nicolite.huthelper.utils.CommUtil;
import cn.nicolite.huthelper.utils.CustomDate;
import cn.nicolite.huthelper.utils.DateUtils;
import cn.nicolite.huthelper.utils.DensityUtils;
import cn.nicolite.huthelper.utils.ScreenUtils;
import cn.nicolite.huthelper.view.activity.SyllabusItemActivity;
import cn.nicolite.huthelper.view.adapter.CourseInfoAdapter;
import cn.nicolite.huthelper.view.customView.CourseInfoGallery;

/**
 * Created by nicolite on 17-12-4.
 * 课程表
 */

public class SyllabusFragment extends BaseFragment {
    /**
     * 第一个月份格子
     */
    @BindView(R.id.tv_month)
    TextView mMonth;

    /**
     * 周一至周日格子
     */
    @BindViews({R.id.tv_monday_course, R.id.tv_tuesday_course, R.id.tv_wednesday_course, R.id.tv_thursday_course, R.id.tv_friday_course, R.id.tv_saturday_course, R.id.tv_sunday_course})
    List<TextView> mWeekViews;

    /**
     * 日期
     */
    @BindViews({R.id.ll_layout1, R.id.ll_layout2, R.id.ll_layout3, R.id.ll_layout4, R.id.ll_layout5, R.id.ll_layout6, R.id.ll_layout7})
    List<LinearLayout> mLayouts;

    @BindViews({R.id.tv_day1, R.id.tv_day2, R.id.tv_day3, R.id.tv_day4, R.id.tv_day5, R.id.tv_day6, R.id.tv_day7})
    List<TextView> mTextViews;

    @BindViews({R.id.tv_monday_course, R.id.tv_tuesday_course, R.id.tv_wednesday_course, R.id.tv_thursday_course, R.id.tv_friday_course, R.id.tv_saturday_course, R.id.tv_sunday_course})
    List<TextView> mWeekNumViews;

    /**
     * 课程表
     */
    @BindView(R.id.ll_weekView)
    LinearLayout courseTableLayout;

    /**
     * 时间轴
     */
    @BindView(R.id.ll_time)
    LinearLayout timeLayout;

    /**
     * 课程轴
     */
    @BindView(R.id.rl_courses)
    RelativeLayout courseLayout;

    /**
     * 课程轴
     */
    @BindView(R.id.rl_user_courses)
    RelativeLayout mUserCourseLayout;

    @BindView(R.id.scroll_body)
    ScrollView scrollView;

    public final String[] HOURS = {"8:00", "8:55", "10:00", "10:55", "14:00", "14:55", "16:00", "16:55", "19:00", "19:55"};
    /**
     * 课程格子平均宽度 高度
     **/
    protected int aveWidth, gridHeight;
    //第一个格子宽度
    private int firstGridWidth;
    //一小时区域
    private RelativeLayout.LayoutParams mHourParams;

    private RelativeLayout.LayoutParams mNumTextParams;
    //时间文字
    private RelativeLayout.LayoutParams mHourTextParams;
    //时间截止线
    private RelativeLayout.LayoutParams mHourLineParams;

    protected List<TextView> courseTextViewList = new ArrayList<TextView>();
    protected SparseArray<List<Lesson>> textviewLessonSparseArray = new SparseArray<>();
    public int CurrWeek;
    private View curClickView;

    private static final int WEEK = 7;
    private static final int TOTAL_COL = 7;
    private static final int TOTAL_ROW = 1;

    private CustomDate mShowDate;//自定义的日期  包括year month day
    private CourseInfoInitMessageHandler courseInfoInitMessageHandler = new CourseInfoInitMessageHandler(this);
    private int what = 111;

    public static SyllabusFragment newInstance() {

        Bundle args = new Bundle();

        SyllabusFragment fragment = new SyllabusFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initConfig(Bundle savedInstanceState) {

    }

    @Override
    protected void initArguments(Bundle arguments) {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_syllabus;
    }

    @Override
    protected void doBusiness() {
        firstGridWidth = DensityUtils.dp2px(context, 30 + 2);
        aveWidth = (ScreenUtils.getScreenWidth(context) - firstGridWidth) / 7;
        gridHeight = (ScreenUtils.getScreenHeight(context) - DensityUtils.dp2px(context, 3) * 4) / 12;
        CurrWeek = DateUtils.getNowWeek();
        //初始化24小时view
        initTwentyFourHourViews();
        //导入日期
        initDate();
    }

    @Override
    protected void visibleToUser(boolean isVisible, boolean isFirstVisible) {

    }

    public void updateData() {
        courseTextViewList.clear();
        textviewLessonSparseArray.clear();
        CurrWeek = DateUtils.getNowWeek();

        //更新数据前 移除view
        if (mUserCourseLayout != null) {
            mUserCourseLayout.removeAllViews();
        }

        courseInfoInitMessageHandler.sendEmptyMessage(what);
    }

    public void changeWeek(int weekNo, CustomDate date) {
        courseTextViewList.clear();
        textviewLessonSparseArray.clear();
        CurrWeek = weekNo;
        mShowDate = date;
        fillDate();
        mMonth.setText(String.valueOf(mShowDate.getMonth() + "月"));
    }

    public void removeAllViews() {
        courseLayout.removeAllViews();
        mUserCourseLayout.removeAllViews();
        timeLayout.removeAllViews();
    }

    /**
     * 初始化日期数据
     */
    public void initDate() {
        if (mShowDate == null) {
            mShowDate = DateUtils.getNextSunday();
        }
        mMonth.setText(mShowDate.getMonth() < 10 ? "0" + mShowDate.getMonth() : mShowDate.getMonth() + "月");
        fillDate();
    }

    /**
     * 初始化添加课程点击事件
     */
    private void initTwentyFourHourViews() {
        initViewParams();
        for (int i = 1; i < 11; i++) {
            for (int j = 1; j <= 8; j++) {
                if (j == 1) {
                    addTimeView(i);
                } else if (i % 2 == 0) {
                    //i 节数 j  周数
                    addDotView(i, j - 1);
                    //可以点击的TextView 用来添加课程
                    TextView tx = new TextView(context);
                    tx.setId((i - 1) * 7 + j);
                    //相对布局参数
                    RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(aveWidth, gridHeight * 2);
                    //设置他们的相对位置
                    if (j == 2) {
                        if (i > 1) {
                            rp.addRule(RelativeLayout.BELOW, (i - 3) * 7 + j);
                        }
                    } else {
                        //字体样式
                        tx.setTextAppearance(context, R.style.courseTableText);
                        rp.addRule(RelativeLayout.RIGHT_OF, (i - 1) * 7 + j - 1);
                        rp.addRule(RelativeLayout.ALIGN_TOP, (i - 1) * 7 + j - 1);
                        tx.setText("");
                    }

                    tx.setLayoutParams(rp);
                    tx.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //莫忘了计算点击的时间时 加上开始时间
                            if (curClickView != null) {
                                curClickView.setBackground(null);
                                if (curClickView.getId() == v.getId()) {
                                    curClickView = null;
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("type", SyllabusItemActivity.ADD_COURSE);
                                    bundle.putLong("lessonId", -3);
                                    //跳转到添加课程界面
                                    startActivityForResult(SyllabusItemActivity.class, bundle, Constants.REQUEST);
                                    return;
                                }
                            }
                            curClickView = v;
                            //TODO 修改添加课表背景
                            curClickView.setBackground(getResources().getDrawable(R.drawable.add_course_bg));
                            curClickView.setAlpha((float) 0.5);
                        }
                    });
                    if (courseLayout != null) {
                        courseLayout.addView(tx);
                    }
                }
            }
        }
    }

    /**
     * 时间轴
     *
     * @param hour 几节
     */
    public void addTimeView(int hour) {
        RelativeLayout layout = new RelativeLayout(context);
        layout.setLayoutParams(mHourParams);
        TextView textView = new TextView(context);
        textView.setLayoutParams(mHourTextParams);
        textView.setTextAppearance(context, R.style.weekViewTimeText);
        textView.setText(String.valueOf(hour));
        layout.addView(textView);
        textView = new TextView(context);
        textView.setLayoutParams(mNumTextParams);
        textView.setTextAppearance(context, R.style.weekViewNumText);
        textView.setText(HOURS[hour - 1]);
        layout.addView(textView);
        //第10节横线不显示
        if (hour != 10) {
            TextView lineView = new TextView(context);
            lineView.setLayoutParams(mHourLineParams);
            lineView.setBackgroundColor(getResources().getColor(R.color.line_grey));
            layout.addView(lineView);
        }
        timeLayout.addView(layout);
    }


    /**
     * 周视图上面的小点
     *
     * @param hour
     * @param j
     */
    public void addDotView(int hour, int j) {
        if (j == 7 || hour == 10) {
            return;
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(DensityUtils.dp2px(context, 3),
                DensityUtils.dp2px(context, 3));
        params.topMargin = hour * gridHeight;
        params.leftMargin = aveWidth * j;

        ImageView view = new ImageView(context);
        view.setLayoutParams(params);
        view.setBackgroundColor(getResources().getColor(R.color.week_view_text_date));

        if (courseLayout != null) {
            courseLayout.addView(view);
        }

    }

    /**
     * 初始化viewparams
     */
    private void initViewParams() {
        if (mHourParams == null) {
            mHourParams = new RelativeLayout.LayoutParams(firstGridWidth, gridHeight);
        }
        if (mHourTextParams == null) {
            mHourTextParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            mHourTextParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            mHourTextParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        }
        if (mNumTextParams == null) {
            mNumTextParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            mNumTextParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            mNumTextParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            mNumTextParams.topMargin = 6;
        }

        if (mHourLineParams == null) {
            mHourLineParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, DensityUtils.dp2px(context, (float) 0.5));
            mHourLineParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            mHourLineParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        }
    }

    /**
     * 导入日期数据
     */
    public void fillDate() {
        fillWeekDate();

        courseTextViewList.clear();
        //更新数据前 移除view
        mUserCourseLayout.removeAllViews();
        //更新视图
        courseInfoInitMessageHandler.sendEmptyMessage(what);
    }

    /**
     * 填充星期模式下的数据 默认通过当前日期得到所在星期天的日期，然后依次填充日期
     */
    private void fillWeekDate() {
        int lastMonthDays = DateUtils.getMonthDays(mShowDate.year, mShowDate.month - 1);
        int year = mShowDate.year;
        int month = mShowDate.month;
        int day = mShowDate.day;
        for (int i = TOTAL_COL - 1; i >= 0; i--) {
            day -= 1;
            if (day < 1) {
                if (month == 1) {
                    year--;
                    month = 12;
                } else {
                    month--;
                }
                day = lastMonthDays;
            }
            CustomDate date = CustomDate.modifiDayForObject(year, month, day);
            date.year = year;
            date.week = i;
            //今天
            if (DateUtils.isToday(date)) {
                mTextViews.get(i).setTextColor(context.getResources().getColor(R.color.white));
                mWeekNumViews.get(i).setTextColor(context.getResources().getColor(R.color.white));
                mTextViews.get(i).setText(String.valueOf(day));
                mLayouts.get(i).setBackgroundColor(context.getResources().getColor(R.color.themeColor));
                continue;
            }
            mTextViews.get(i).setText(String.valueOf(day));
            mTextViews.get(i).setTextColor(context.getResources().getColor(R.color.textColor_black));
            mWeekNumViews.get(i).setTextColor(context.getResources().getColor(R.color.textColor_black));
            mLayouts.get(i).setBackgroundColor(context.getResources().getColor(R.color.transparent));
        }
    }


    private class CourseInfoInitMessageHandler extends Handler {
        WeakReference<SyllabusFragment> mTable;

        public CourseInfoInitMessageHandler(SyllabusFragment table) {
            mTable = new WeakReference<SyllabusFragment>(table);
        }

        @Override
        public void handleMessage(Message msg) {
            //获取课程
            List<Lesson> lessonList = daoSession.getLessonDao().queryBuilder()
                    .where(LessonDao.Properties.UserId.eq(userId)).list();
            Lesson upperCourse = null;
            int currentWeek = mTable.get().CurrWeek;
            int lastListSize;
            //七种颜色的背景
            int[] background = {R.drawable.kb_item1, R.drawable.kb_item2, R.drawable.kb_item3, R.drawable.kb_item4};
            do {
                lastListSize = lessonList.size();
                Iterator<Lesson> it = lessonList.iterator();
                while (it.hasNext()) {
                    Lesson le = it.next();
                    if (true) {
                        //判断开始周数是否小于当前周数，结束周数是否大于当前周数
                        //判断单双周
                        upperCourse = le;//设置为顶层课
                        it.remove();
                        break;
                    }
                }

                if (upperCourse != null) {
                    List<Lesson> lList = new ArrayList<>();
                    lList.add(upperCourse);
                    int index = -1;
                    if (CommUtil.ifHaveCourse(upperCourse, mTable.get().CurrWeek)) {
                        index = 0;
                    }
                    it = lessonList.iterator();
                    //查找是否有重叠的课
                    while (it.hasNext()) {
                        Lesson lesson = it.next();
                        if (lesson.getDjj().equals(upperCourse.getDjj()) && lesson.getXqj().equals(upperCourse.getXqj())) {
                            boolean change = false;
                            for (int i = 0; i < lList.size(); i++) {
                                if (lList.get(i).getName().equals(lesson.getName())) {
                                    if (CommUtil.ifHaveCourse(lesson, mTable.get().CurrWeek)) {
                                        upperCourse = lesson;
                                        index = i;
                                    }
                                    change = true;
                                    it.remove();
                                }
                            }
                            if (!change) {
                                lList.add(lesson);
                                it.remove();
                                if (CommUtil.ifHaveCourse(lesson, mTable.get().CurrWeek)) {
                                    upperCourse = lesson;
                                    index++;
                                }
                            }
                        }
                    }
                    final int upperCourseIndex = index;
                    TextView lesson = new TextView(mTable.get().context);
                    lesson.setId(10 * Integer.parseInt(upperCourse.getXqj()) + Integer.parseInt(upperCourse.getDjj()));
                    mTable.get().textviewLessonSparseArray.put(lesson.getId(), lList);
                    lesson.setText(String.valueOf(upperCourse.getName() + "@" + upperCourse.getRoom()));
                    RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(mTable.get().aveWidth - 6, mTable.get().gridHeight * 2 - 10);
                    rlp.topMargin = ((Integer.parseInt(upperCourse.getDjj()) + 1) / 2 - 1) * mTable.get().gridHeight * 2 + 3;
                    rlp.leftMargin = mTable.get().firstGridWidth + (Integer.parseInt(upperCourse.getXqj()) - 1) * mTable.get().aveWidth + 5;
                    lesson.setTextSize(12);
                    lesson.setPadding(10, 10, 10, 10);

                    if (index != -1) {
                        int randomN = Math.abs(upperCourse.getName().hashCode()) % (background.length - 1);
                        int bgRes = background[randomN];
                        // int bgRes = background[(int) (Math.random() * (background.length - 1))];//随机获取背景色
                        lesson.setBackgroundResource(bgRes);//设置背景
                        lesson.setTextColor(Color.WHITE);
                    } else {
                        lesson.setBackgroundResource(R.drawable.kbno);
                        lesson.setTextColor(Color.parseColor("#cacaca"));
                    }
                    lesson.setLayoutParams(rlp);
                    //处理点击事件
                    lesson.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SparseArray<List<Lesson>> map = mTable.get().textviewLessonSparseArray;
                            final List<Lesson> tempList = map.get(v.getId());
                            if (tempList.size() > 1) {
                                //如果有多个课程，则设置点击弹出gallery 3d 对话框
                                LayoutInflater layoutInflater = (LayoutInflater) mTable.get().context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View galleryView = layoutInflater.inflate(R.layout.gallery_3d_lesson, null);
                                final AlertDialog coursePopupDialog = new AlertDialog.Builder(mTable.get().context, R.style.CustomDialog).create();
                                coursePopupDialog.setCanceledOnTouchOutside(true);
                                coursePopupDialog.setCancelable(true);

                                coursePopupDialog.show();
                                WindowManager.LayoutParams params = coursePopupDialog.getWindow().getAttributes();
                                params.width = ViewGroup.LayoutParams.MATCH_PARENT;

                                coursePopupDialog.getWindow().setAttributes(params);
                                CourseInfoAdapter adapter = new CourseInfoAdapter(mTable.get().context, tempList, ScreenUtils.getScreenWidth(mTable.get().context), mTable.get().CurrWeek);
                                CourseInfoGallery gallery = (CourseInfoGallery) galleryView.findViewById(R.id.lesson_gallery);
                                gallery.setSpacing(10);
                                gallery.setAdapter(adapter);
                                gallery.setSelection(upperCourseIndex);
                                gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                                        coursePopupDialog.dismiss();
                                        Bundle mBundle = new Bundle();
                                        mBundle.putLong("lessonId", tempList.get(arg2).getId());
                                        mBundle.putInt("type", SyllabusItemActivity.SHOW_COURSE);
                                        startActivityForResult(SyllabusItemActivity.class, mBundle, Constants.REQUEST);
                                    }
                                });

                                coursePopupDialog.setContentView(galleryView);
                            } else {
                                Bundle mBundle = new Bundle();
                                mBundle.putInt("type", SyllabusItemActivity.SHOW_COURSE);
                                mBundle.putLong("lessonId", tempList.get(0).getId());
                                startActivityForResult(SyllabusItemActivity.class, mBundle, Constants.REQUEST);
                            }
                        }
                    });
                    //添加布局
                    if (mTable.get().mUserCourseLayout != null) {
                        mTable.get().mUserCourseLayout.addView(lesson);
                    }

                    if (mTable.get().courseTextViewList != null) {
                        mTable.get().courseTextViewList.add(lesson);
                    }
                    upperCourse = null;
                }
            } while (lessonList.size() < lastListSize && lessonList.size() != 0);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST) {
            if (resultCode == Constants.REFRESH || resultCode == Constants.DELETE) {
                updateData();
            }
        }
    }
}
