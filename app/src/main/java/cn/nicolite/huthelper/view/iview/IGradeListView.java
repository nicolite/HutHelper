package cn.nicolite.huthelper.view.iview;

import java.util.List;

import cn.nicolite.huthelper.base.IBaseView;
import cn.nicolite.huthelper.model.bean.Grade;

/**
 * Created by nicolite on 17-11-13.
 */

public interface IGradeListView extends IBaseView {
    void showGradeList(List<Grade> gradeList);
    void changeGradeList(List<Grade> gradeList);
}
