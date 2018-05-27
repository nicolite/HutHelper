package cn.nicolite.huthelper.base;

/**
 *
 * Created by nicolite on 17-10-13.
 */

public interface IBaseView {
    void showLoading();

    void closeLoading();

    void showMessage(String msg);
}
