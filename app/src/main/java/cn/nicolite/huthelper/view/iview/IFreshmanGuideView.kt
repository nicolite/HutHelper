package cn.nicolite.huthelper.view.iview

import cn.nicolite.huthelper.base.IBaseView
import cn.nicolite.huthelper.model.bean.FreshmanGuide

/**
 * Created by nicolite on 17-11-13.
 */

interface IFreshmanGuideView : IBaseView {
    fun showGuideList(list: List<FreshmanGuide>)
}
