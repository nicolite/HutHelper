package cn.nicolite.huthelper.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.model.bean.Exam;
import cn.nicolite.huthelper.utils.ListUtils;

/**
 * Created by nicolite on 17-11-2.
 */

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ExamViewHolder> {
    private Context context;
    private List<Exam> examList;

    public ExamAdapter(Context context, List<Exam> examList) {
        this.examList = examList;
        this.context = context;
    }

    @Override
    public ExamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_exam, parent, false);
        return new ExamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExamViewHolder holder, int position) {
        Exam exam = examList.get(position);
        //坑，居然还会返回null
        if (!TextUtils.isEmpty(exam.getStarttime()) && !TextUtils.isEmpty(exam.getEndTime())) {
            String[] startTime = exam.getStarttime().split(" ");
            String[] endTime = exam.getEndTime().split(" ");

            holder.tvGradeLesson.setText(startTime[0]);
            holder.tvExamitemTime.setText(String.valueOf("（" + exam.getWeek_Num() + "周 "
                    + startTime[1].substring(0, 5) + "-" + endTime[1].substring(0, 5) + "）"));

            String remainder = "今天";
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                Date startDate = simpleDateFormat.parse(exam.getStarttime());
                Date endDate = simpleDateFormat.parse(exam.getEndTime());
                Date nowDate = new Date();

                long startHours = (startDate.getTime() - nowDate.getTime()) / (60 * 60 * 1000); //获得剩余小时
                long endHours = (endDate.getTime() - nowDate.getTime()) / (60 * 60 * 1000);

                if (startHours >= 24) {
                    long day = startHours / 24;
                    if (startHours % 24 > 0) {
                        day++;
                    }
                    remainder = "剩余" + day + "天";
                } else if (startHours > 0 && startHours < 24) {
                    remainder = "剩余" + (startHours + 1) + "小时";
                } else if (startHours <= 0 && endHours > 0) {
                    remainder = "正在进行";
                } else if (endHours <= 0) {
                    remainder = "已结束";
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.tvGradeScore.setText(remainder);
        } else {
            holder.tvGradeLesson.setText("未知");
            holder.tvGradeTime.setText("未知");
            holder.tvExamitemTime.setText(String.valueOf("（" + exam.getWeek_Num() + "周" + "）"));
            holder.tvGradeScore.setText("未知");
        }

        //isset不确定是不是重修标记
        // if (!exam.getIsset().equals("0")){
        //     holder.tvGradeTime.setText(String.valueOf(exam.getCourseName() + "（重修）"));
        // }else {
        //     holder.tvGradeTime.setText(exam.getCourseName());
        // }

        holder.tvGradeTime.setText(exam.getCourseName());

        String roomName = exam.getRoomName();
        holder.tvGradeJidian.setText(TextUtils.isEmpty(roomName) ? "未知" : roomName);

    }

    @Override
    public int getItemCount() {
        return ListUtils.isEmpty(examList) ? 0 : examList.size();
    }

    static class ExamViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_grade_lesson)
        TextView tvGradeLesson; //日期
        @BindView(R.id.tv_examitem_time)
        TextView tvExamitemTime; //第几周几点到几点
        @BindView(R.id.tv_grade_time)
        TextView tvGradeTime; //课程名
        @BindView(R.id.tv_grade_jidian)
        TextView tvGradeJidian; //地点
        @BindView(R.id.tv_grade_score)
        TextView tvGradeScore; //剩余多少天
        @BindView(R.id.rootView)
        RelativeLayout rootView;

        public ExamViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
