package cn.nicolite.huthelper.view.iview;

import java.util.List;

import cn.nicolite.huthelper.base.IBaseView;
import cn.nicolite.huthelper.model.bean.CareerTalk;

/**
 * ICareerTalkView
 * Created by nicolite on 17-11-5.
 */

public interface ICareerTalkView extends IBaseView {
    void showCareerTalkList(List<CareerTalk> careerTalkList);
    void loadMore(List<CareerTalk> careerTalkList);
}
