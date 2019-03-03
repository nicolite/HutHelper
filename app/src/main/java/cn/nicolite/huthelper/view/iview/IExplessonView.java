package cn.nicolite.huthelper.view.iview;

import java.util.List;

import cn.nicolite.huthelper.base.IBaseView;
import cn.nicolite.huthelper.model.bean.ExpLesson;

/**
 * IExplessonView
 * Created by nicolite on 17-11-4.
 */

public interface IExplessonView extends IBaseView {
    void showExpLesson(List<ExpLesson> expLessonList);
}
