package cn.nicolite.huthelper.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.model.bean.Lesson;
import cn.nicolite.huthelper.utils.CommUtil;
import cn.nicolite.huthelper.view.customView.CourseInfoGallery;

/**
 * 课程信息适配器
 * Created by 高沛 on 2016/7/12.
 */
public class CourseInfoAdapter extends BaseAdapter{
    private Context context;
    private TextView[] courseTextViewList;
    private int screenWidth;
    private int currentWeek;
    public CourseInfoAdapter(Context context, List<Lesson> courseList, int width, int currentWeek){
        super();
        this.screenWidth=width;
        this.context=context;
        this.currentWeek=currentWeek;

        createGalleryWithCourseList(courseList);
    }



    private void createGalleryWithCourseList(List<Lesson> lessonList){
        int[] back={};
        this.courseTextViewList = new TextView[lessonList.size()];
        for(int i=0;i<lessonList.size();i++){
            final Lesson lesson=lessonList.get(i);
            TextView textView=new TextView(context);
            textView.setText(String.valueOf(lesson.getName()+"@"+lesson.getRoom()));
            textView.setLayoutParams(new CourseInfoGallery.LayoutParams((screenWidth/6)*2,(screenWidth / 6) *2));
            textView.setTextColor(Color.BLACK);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setPadding(10,0,0,0);
           if(CommUtil.ifHaveCourse(lesson,currentWeek)){
               textView.setBackgroundResource(R.drawable.kb3);
           }else {
               textView.setBackgroundResource(R.drawable.kbno);
           }
            textView.getBackground().setAlpha(222);
            this.courseTextViewList[i]=textView;


        }

    }

    @Override
    public int getCount() {
        return courseTextViewList.length;
    }

    @Override
    public Object getItem(int position) {
        return courseTextViewList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return courseTextViewList[position];
    }

    public float getScale(boolean focused, int offset) {
        return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
    }
}
