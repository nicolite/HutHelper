package cn.nicolite.huthelper.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
        String[] startTime = exam.getStarttime().split(" ");
        String[] endTime = exam.getEndTime().split(" ");

        holder.tvGradeLesson.setText(startTime[0]);
        holder.tvExamitemTime.setText(String.valueOf("（" + exam.getWeek_Num() + "周 "
                + startTime[1].substring(0, 5) + "-" + endTime[1].substring(0, 5) + "）"));

        if (!exam.getIsset().equals("0")){
            holder.tvGradeTime.setText(String.valueOf(exam.getCourseName() + "（重修）"));
        }else {
            holder.tvGradeTime.setText(exam.getCourseName());
        }

        holder.tvGradeJidian.setText(exam.getRoomName());
        String remainder = "今天";
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            Date date = simpleDateFormat.parse(startTime[0]);
            Date nowDate = new Date();
            long rem = (date.getTime() - nowDate.getTime()) / (24 * 60 * 60 * 1000);
            if (rem > 0) {
                remainder = "剩余" + rem + "天";
            }else if (rem < 0){
                remainder = "已结束";
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.tvGradeScore.setText(remainder);
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
