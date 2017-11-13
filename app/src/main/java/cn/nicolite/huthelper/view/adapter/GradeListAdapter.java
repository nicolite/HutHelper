package cn.nicolite.huthelper.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.model.bean.Grade;
import cn.nicolite.huthelper.utils.ListUtils;

/**
 * Created by nicolite on 17-11-13.
 */

public class GradeListAdapter extends RecyclerView.Adapter<GradeListAdapter.GradeListViewHolder> {

    private Context context;
    private List<Grade> gradeList;

    public GradeListAdapter(Context context, List<Grade> gradeList) {
        this.context = context;
        this.gradeList = gradeList;
    }

    @Override
    public GradeListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_grade_list, parent, false);
        return new GradeListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GradeListViewHolder holder, int position) {
        Grade grade = gradeList.get(position);
        String xfjd = "学分&绩点：" + grade.getXf() + "&" + grade.getJd();
        String gradeTime = grade.getXn() + "学年第" + grade.getXq() + "学期";
        String lessonName = grade.getKcmc();
        String gradeScore = grade.getCj();
        if (!TextUtils.isEmpty(grade.getBkcj())) {
            gradeTime += "（补考）";
            gradeScore = grade.getBkcj();
        } else if (!TextUtils.isEmpty(grade.getCxbj()) && grade.getCxbj().equals("1")) {
            gradeTime += "（重修）";
        }

        if (TextUtils.isDigitsOnly(gradeScore)) {
            if (Integer.parseInt(gradeScore) < 60) {
                lessonName += "（未通过）";
            }
            gradeScore += "分";
        } else if (gradeScore.equals("不及格")) {
            lessonName += "（未通过）";
        }
        holder.tvGradeLesson.setText(lessonName);
        holder.tvGradeTime.setText(gradeTime);
        holder.tvGradeJidian.setText(xfjd);
        holder.tvGradeScore.setText(gradeScore);
    }

    @Override
    public int getItemCount() {
        return ListUtils.isEmpty(gradeList) ? 0 : gradeList.size();
    }

    static class GradeListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_grade_lesson)
        TextView tvGradeLesson;
        @BindView(R.id.tv_grade_time)
        TextView tvGradeTime;
        @BindView(R.id.tv_grade_jidian)
        TextView tvGradeJidian;
        @BindView(R.id.tv_grade_score)
        TextView tvGradeScore;
        @BindView(R.id.rootView)
        RelativeLayout rootView;

        public GradeListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
