package cn.nicolite.huthelper.view.iview;

import java.util.List;

import cn.nicolite.huthelper.base.iview.IBaseView;
import cn.nicolite.huthelper.model.bean.FreshmanGuide;

/**
 * Created by nicolite on 17-11-13.
 */

public interface IFreshmanGuideView extends IBaseView {
    void showGuideList(List<FreshmanGuide> list);
}
