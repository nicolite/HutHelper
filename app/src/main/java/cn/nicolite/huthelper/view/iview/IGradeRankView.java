package cn.nicolite.huthelper.view.iview;

import cn.nicolite.huthelper.base.iview.IBaseView;
import cn.nicolite.huthelper.model.bean.GradeRankResult;

/**
 * Created by nicolite on 17-12-2.
 */

public interface IGradeRankView extends IBaseView {
    void showRank(GradeRankResult gradeRankResult);
}
