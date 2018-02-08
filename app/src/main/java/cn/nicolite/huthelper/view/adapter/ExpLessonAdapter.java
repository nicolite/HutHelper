package cn.nicolite.huthelper.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.model.bean.ExpLesson;
import cn.nicolite.huthelper.utils.ListUtils;

/**
 * Created by nicolite on 17-11-4.
 */

public class ExpLessonAdapter extends RecyclerView.Adapter<ExpLessonAdapter.ExpLessonViewHolder> {

    private Context context;
    private List<ExpLesson> expLessonList;

    public ExpLessonAdapter(Context context, List<ExpLesson> expLessonList) {
        this.context = context;
        this.expLessonList = expLessonList;
    }

    @Override
    public ExpLessonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_explesson, parent, false);
        return new ExpLessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExpLessonViewHolder holder, int position) {
        ExpLesson expLesson = expLessonList.get(position);

        holder.tvExpitemRealtime.setText(String.valueOf(expLesson.getWeeks_no() + "周周"
                + getWeekNum(expLesson.getWeek()) + " " + expLesson.getReal_time()));

        if (!TextUtils.isEmpty(expLesson.getObj())){
            holder.tvExpitemLesson.setText(String.valueOf(expLesson.getLesson() + "-" + expLesson.getObj()));
        }else {
            holder.tvExpitemLesson.setText(expLesson.getLesson());
        }

        holder.tvExpitemPlace.setText(expLesson.getLocate());
        holder.tvExpitemTeacher.setText(expLesson.getTeacher());
    }

    @Override
    public int getItemCount() {
        return ListUtils.isEmpty(expLessonList) ? 0 : expLessonList.size();
    }

    static class ExpLessonViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_expitem_realtime)
        TextView tvExpitemRealtime;
        @BindView(R.id.tv_expitem_lesson)
        TextView tvExpitemLesson;
        @BindView(R.id.tv_expitem_place)
        TextView tvExpitemPlace;
        @BindView(R.id.tv_expitem_teacher)
        TextView tvExpitemTeacher;
        @BindView(R.id.ll_exp_root)
        LinearLayout llExpRoot;
        public ExpLessonViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private String getWeekNum(String num){
        switch (num){
            case "1":
                return "一";
            case "2":
                return "二";
            case "3":
                return "三";
            case "4":
                return "四";
            case "5":
                return "五";
            case "6":
                return "六";
            case "7":
                return "日";

        }
        return num;
    }
}
