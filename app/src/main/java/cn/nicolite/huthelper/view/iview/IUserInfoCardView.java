package cn.nicolite.huthelper.view.iview;

import cn.nicolite.huthelper.base.IBaseView;
import cn.nicolite.huthelper.model.bean.User;

/**
 * Created by nicolite on 17-11-12.
 */

public interface IUserInfoCardView extends IBaseView {
    void showInfo(User user);
}
