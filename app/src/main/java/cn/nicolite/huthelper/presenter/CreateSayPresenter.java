package cn.nicolite.huthelper.presenter;

import cn.nicolite.huthelper.base.presenter.BasePresenter;
import cn.nicolite.huthelper.view.activity.CreateSayActivity;
import cn.nicolite.huthelper.view.iview.ICreateSayView;

/**
 * Created by nicolite on 17-11-15.
 */

public class CreateSayPresenter extends BasePresenter<ICreateSayView, CreateSayActivity> {
    public CreateSayPresenter(ICreateSayView view, CreateSayActivity activity) {
        super(view, activity);
    }
}
