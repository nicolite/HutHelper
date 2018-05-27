package cn.nicolite.huthelper.view.iview;

import cn.nicolite.huthelper.base.IBaseView;
import cn.nicolite.huthelper.model.bean.Electric;

/**
 * IElectricView
 * Created by nicolite on 17-10-31.
 */

public interface IElectricView extends IBaseView {
    void showLouHao(String lou, String hao);
    void showElectric(Electric electric);
    void showWeather(String city, String tmp, String content);
    void showVoteSummary(String yes, String no, String opt);
}
