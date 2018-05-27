package cn.nicolite.huthelper.view.iview;

import java.util.List;

import cn.nicolite.huthelper.base.IBaseView;
import cn.nicolite.huthelper.model.bean.GradeRank;
import cn.nicolite.huthelper.model.bean.GradeSum;

/**
 * Created by nicolite on 17-12-2.
 */

public interface IGradeRankView extends IBaseView {
    void showRank(GradeSum gradeSum, List<GradeRank> xnRank, List<GradeRank> xqRank);
}
