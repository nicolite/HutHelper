package cn.nicolite.huthelper.view.iview;

import java.util.List;

import cn.nicolite.huthelper.base.IBaseView;
import cn.nicolite.huthelper.model.bean.Menu;
import cn.nicolite.huthelper.model.bean.Notice;
import cn.nicolite.huthelper.model.bean.TimeAxis;
import cn.nicolite.huthelper.model.bean.User;

/**
 * Created by nicolite on 17-10-22.
 */

public interface IMainView extends IBaseView {
    void showWeather(String city, String tmp, String content);
    void showTimeAxis(List<TimeAxis> timeAxisList);
    void showNotice(Notice notice, boolean isReceiver);
    void showSyllabus(String date, String nextClass);
    void showMenu(List<Menu> menuList);
    void showUser(User user);
}
