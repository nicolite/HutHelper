package cn.nicolite.huthelper.view.iview;

import cn.nicolite.huthelper.base.IBaseView;
import cn.nicolite.huthelper.model.bean.CareerTalkItem;

/**
 * ICareerTalkItemView
 * Created by nicolite on 17-11-5.
 */

public interface ICareerTalkItemView extends IBaseView{
    void  showContent(CareerTalkItem careerTalkItem);
}
