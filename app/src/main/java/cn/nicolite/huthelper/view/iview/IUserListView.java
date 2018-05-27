package cn.nicolite.huthelper.view.iview;

import java.util.List;

import cn.nicolite.huthelper.base.IBaseView;
import cn.nicolite.huthelper.model.bean.User;

/**
 * Created by nicolite on 17-11-11.
 */

public interface IUserListView extends IBaseView {
    void showUsers(List<User> users);
    void loadFailure();
}
