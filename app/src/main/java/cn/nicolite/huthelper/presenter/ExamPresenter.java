package cn.nicolite.huthelper.presenter;

import cn.nicolite.huthelper.base.presenter.BasePresenter;
import cn.nicolite.huthelper.view.activity.ExamActivity;
import cn.nicolite.huthelper.view.iview.IExamView;

/**
 * ExamPresenter
 * Created by nicolite on 17-11-1.
 */

public class ExamPresenter extends BasePresenter<IExamView, ExamActivity> {
    public ExamPresenter(IExamView view, ExamActivity activity) {
        super(view, activity);
    }
}
