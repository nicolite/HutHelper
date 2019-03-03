package cn.nicolite.huthelper.view.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.OnClick;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.BaseActivity;
import cn.nicolite.huthelper.db.dao.LessonDao;
import cn.nicolite.huthelper.model.Constants;
import cn.nicolite.huthelper.model.bean.Lesson;
import cn.nicolite.huthelper.utils.DensityUtils;
import cn.nicolite.huthelper.utils.ListUtils;
import cn.nicolite.huthelper.utils.LogUtils;
import cn.nicolite.huthelper.utils.ScreenUtils;
import cn.nicolite.huthelper.utils.ToastUtil;
import cn.nicolite.huthelper.view.adapter.SyllabusItemAdapter;
import cn.nicolite.huthelper.view.customView.CommonDialog;

/**
 * Created by nicolite on 17-12-4.
 * 课程详情页
 */

public class SyllabusItemActivity extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_ok)
    ImageView toolbarOk;
    @BindView(R.id.toolbar_edit)
    ImageView toolbarEdit;
    @BindView(R.id.toolbar_delete)
    ImageView toolbarDelete;
    @BindView(R.id.tv_course_message)
    TextView tvCourseMessage;
    @BindView(R.id.gv_course_weeks)
    GridView gvCourseWeeks;
    @BindView(R.id.tv_course_name)
    TextView tvCourseName;
    @BindView(R.id.et_course_name)
    EditText etCourseName;
    @BindView(R.id.tv_course_time)
    TextView tvCourseTime;
    @BindView(R.id.tv_course_week)
    TextView tvCourseWeek;
    @BindView(R.id.tv_course_num)
    TextView tvCourseNum;
    @BindView(R.id.ll_course_time)
    LinearLayout llCourseTime;
    @BindView(R.id.tv_course_classroom)
    TextView tvCourseClassroom;
    @BindView(R.id.et_course_classroom)
    EditText etCourseClassroom;
    @BindView(R.id.tv_course_teacher)
    TextView tvCourseTeacher;
    @BindView(R.id.et_course_teacher)
    EditText etCourseTeacher;
    @BindView(R.id.rootView)
    LinearLayout rootView;
    private long lessonId = -2;
    public static final int EDIT_COURSE = 0;
    public static final int ADD_COURSE = 1;
    public static final int SHOW_COURSE = 2;
    private int type = ADD_COURSE;
    private Lesson lesson;

    @BindArray(R.array.weeklist)
    String[] weeklists;
    @BindArray(R.array.coursenumlist)
    String[] coursenumlists;
    private int weekSelected = 0;
    private int courseNumSelected = 0;
    private boolean[] weeklist = new boolean[20];
    private LessonDao lessonDao;
    private SyllabusItemAdapter adapter;

    @Override
    protected void initConfig(Bundle savedInstanceState) {
        setImmersiveStatusBar();
        setDeepColorStatusBar();
    }

    @Override
    protected void initBundleData(Bundle bundle) {
        super.initBundleData(bundle);
        if (bundle != null) {
            lessonId = bundle.getLong("lessonId", -2);
            type = bundle.getInt("type", ADD_COURSE);
        }

        if (lessonId == -2) {
            ToastUtil.showToastShort("获取课程信息出错！");
            finish();
        } else if (lessonId == -3) {
            type = ADD_COURSE;
        } else {
            List<Lesson> list = daoSession.getLessonDao().queryBuilder()
                    .where(LessonDao.Properties.UserId.eq(userId), LessonDao.Properties.Id.eq(lessonId))
                    .list();
            if (!ListUtils.isEmpty(list)) {
                lesson = list.get(0);
            }
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_syllabus_item;
    }

    @Override
    protected void doBusiness() {
        if (type == SHOW_COURSE) {
            toolbarTitle.setText("课程详情");
            setShowCourse();
            setCourseData();
        } else {
            toolbarTitle.setText("添加课程");
            setEditCourse();
        }
        lessonDao = daoSession.getLessonDao();

        adapter = new SyllabusItemAdapter(context, weeklist);
        gvCourseWeeks.setAdapter(adapter);
        adapter.setOnItemClickListener(new SyllabusItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, long itemId) {
                if (type == SHOW_COURSE)
                    return;
                if (weeklist[position]) {
                    view.setBackgroundResource(R.color.new_grty);
                    weeklist[position] = false;
                } else if (!weeklist[position]) {
                    view.setBackgroundResource(R.color.colorPrimary);
                    weeklist[position] = true;
                }
            }
        });
    }

    @OnClick({R.id.toolbar_back, R.id.toolbar_ok, R.id.toolbar_edit, R.id.toolbar_delete,
            R.id.tv_course_week, R.id.tv_course_num})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.toolbar_ok:
                if (fastClick()) {
                    try {
                        if (!TextUtils.isEmpty(etCourseName.getText().toString())) {
                            Lesson lesson = new Lesson();

                            int courseweek = weekSelected + 1;
                            int coursenum = courseNumSelected + 1;

                            lesson.setXqj(String.valueOf(courseweek));
                            lesson.setDjj(String.valueOf(coursenum * 2 - 1));
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < weeklist.length; i++) {
                                if (weeklist[i])
                                    sb.append(",").append(i + 1);
                            }
                            lesson.setZs(sb.toString());
                            lesson.setName(etCourseName.getText().toString());
                            lesson.setTeacher(etCourseTeacher.getText().toString());
                            lesson.setRoom(etCourseClassroom.getText().toString());
                            lesson.setUserId(userId);
                            if (type == EDIT_COURSE) {
                                lesson.setAddByUser(false);
                                lesson.setId(lessonId);
                                lessonDao.update(lesson);
                                ToastUtil.showToastShort("编辑成功");
                            } else {
                                lesson.setAddByUser(true);
                                lessonDao.insert(lesson);
                                ToastUtil.showToastShort("添加成功");
                            }
                            setResult(Constants.REFRESH);
                            finish();
                        } else {
                            ToastUtil.showToastShort("请填写课程名");
                        }
                        type = SHOW_COURSE;
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.showToastShort("添加出现错误");
                    }
                }
                break;
            case R.id.toolbar_edit:
                type = EDIT_COURSE;
                toolbarTitle.setText("编辑课程");
                setEditCourse();
                editCourseData();
                break;
            case R.id.toolbar_delete:
                deleteCourse();
                break;
            case R.id.tv_course_week:
                new AlertDialog.Builder(context)
                        .setTitle("请选择周数")
                        .setSingleChoiceItems(weeklists, weekSelected, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                weekSelected = i;
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                tvCourseWeek.setText(weeklists[weekSelected]);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
            case R.id.tv_course_num:
                new AlertDialog.Builder(context)
                        .setTitle("请选择第几节")
                        .setSingleChoiceItems(coursenumlists, courseNumSelected, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                courseNumSelected = i;
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                tvCourseNum.setText(coursenumlists[courseNumSelected]);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
        }
    }

    private void setCourseData() {
        if (lesson == null) {
            ToastUtil.showToastShort("获取课程数据出错！");
            finish();
        }

        tvCourseName.setText(lesson.getName());
        tvCourseTime.setText(String.valueOf("周" + lesson.getXqj() + "  " + lesson.getDjj() + " " + (Integer.parseInt(lesson.getDjj()) + 1) + "节"));
        tvCourseTeacher.setText(lesson.getTeacher());
        tvCourseClassroom.setText(lesson.getRoom());

        weekSelected = Integer.parseInt(lesson.getXqj()) - 1;
        courseNumSelected = (Integer.parseInt(lesson.getDjj()) + 1) / 2 - 1;

        String[] ws = lesson.getZs().split(",");
        for (String s : ws) {
            if (!TextUtils.isEmpty(s)) {
                int num = Integer.parseInt(s);
                if (num <= 20) {
                    weeklist[num - 1] = true;
                    LogUtils.d(TAG, "lesson zs: " + weeklist[num - 1] + " " + num);
                }

            }
        }
    }

    private void editCourseData() {
        etCourseName.setText(lesson.getName());
        tvCourseWeek.setText(weeklists[Integer.parseInt(lesson.getXqj()) - 1]);
        // tvCourseWeek.setCompoundDrawables(null, null, null, null);
        tvCourseNum.setText(coursenumlists[(Integer.parseInt(lesson.getDjj()) + 1) / 2 - 1]);
        //tvCourseNum.setCompoundDrawables(null, null, null, null);
        etCourseTeacher.setText(lesson.getTeacher());
        etCourseClassroom.setText(lesson.getRoom());
    }

    //展示数据模式
    private void setShowCourse() {
        tvCourseMessage.setText("周数");

        etCourseName.setVisibility(View.GONE);
        etCourseClassroom.setVisibility(View.GONE);
        etCourseTeacher.setVisibility(View.GONE);
        llCourseTime.setVisibility(View.GONE);

        tvCourseClassroom.setVisibility(View.VISIBLE);
        tvCourseName.setVisibility(View.VISIBLE);
        tvCourseTeacher.setVisibility(View.VISIBLE);
        tvCourseTime.setVisibility(View.VISIBLE);

        toolbarOk.setVisibility(View.GONE);
        toolbarDelete.setVisibility(View.VISIBLE);
        toolbarEdit.setVisibility(View.VISIBLE);

    }

    //编辑数据模式
    private void setEditCourse() {
        tvCourseMessage.setText("周数   点击下方选择");
        etCourseName.setVisibility(View.VISIBLE);
        etCourseClassroom.setVisibility(View.VISIBLE);
        etCourseTeacher.setVisibility(View.VISIBLE);
        llCourseTime.setVisibility(View.VISIBLE);

        tvCourseClassroom.setVisibility(View.GONE);
        tvCourseName.setVisibility(View.GONE);
        tvCourseTeacher.setVisibility(View.GONE);
        tvCourseTime.setVisibility(View.GONE);

        toolbarOk.setVisibility(View.VISIBLE);
        toolbarDelete.setVisibility(View.GONE);
        toolbarEdit.setVisibility(View.GONE);
    }

    protected void deleteCourse() {
        final CommonDialog commonDialog = new CommonDialog(this);
        commonDialog.setMessage("确认移除该课程吗？")
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        commonDialog.dismiss();
                        lessonDao.deleteByKey(lessonId);
                        ToastUtil.showToastShort("删除成功");
                        setResult(Constants.DELETE);
                        finish();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }


    public class MyGridViewAdapter extends BaseAdapter {
        Context context;

        boolean[] list;

        public MyGridViewAdapter(Context context) {
            this.context = context;
            this.list = weeklist;
        }

        @Override
        public int getCount() {
            return list.length;
        }

        @Override
        public Object getItem(int position) {
            return list[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, final View convertView, ViewGroup parent) {
            final Button img = new Button(context);
            int width = ScreenUtils.getScreenWidth(context) - DensityUtils.dp2px(context, 45);// 获取屏幕宽度
            int height = 0;
            width = width / 5;// 对当前的列数进行设置imgView的宽度
            height = width * 5 / 6;
            if (!weeklist[position]) {
                img.setBackgroundResource(R.color.new_grty);
            } else {
                img.setBackgroundResource(R.color.colorPrimary);
            }
            img.setText(String.valueOf(position + 1));
            img.setTextColor(Color.WHITE);
            AbsListView.LayoutParams layout = new AbsListView.LayoutParams(width, height);
            img.setLayoutParams(layout);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type == SHOW_COURSE)
                        return;
                    if (weeklist[position]) {
                        img.setBackgroundResource(R.color.new_grty);
                        weeklist[position] = false;
                    } else if (!weeklist[position]) {
                        img.setBackgroundResource(R.color.colorPrimary);
                        weeklist[position] = true;
                    }
                }
            });
            return img;
        }
    }

    /**
     * [防止快速点击]
     *
     * @return
     */
    protected boolean fastClick() {
        long lastClick = 0;
        if (System.currentTimeMillis() - lastClick <= 1000) {
            return false;
        }
        lastClick = System.currentTimeMillis();
        return true;
    }
}
