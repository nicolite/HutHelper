package cn.nicolite.huthelper.view.iview;

import java.util.List;

import cn.nicolite.huthelper.base.IBaseView;
import cn.nicolite.huthelper.model.bean.Lesson;

/**
 * Created by nicolite on 17-12-4.
 */

public interface ISyllabusView extends IBaseView {
    void showSyllabus(List<Lesson> lessonList);
}
