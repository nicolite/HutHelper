package cn.nicolite.huthelper.view.iview;

import java.util.List;

import cn.nicolite.huthelper.base.IBaseView;
import cn.nicolite.huthelper.model.bean.Exam;

/**
 * ExamPresenter
 * Created by nicolite on 17-11-1.
 */

public interface IExamView extends IBaseView {
    void showExam(List<Exam> examList);
}
