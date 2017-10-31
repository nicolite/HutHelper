package cn.nicolite.huthelper.presenter;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.nicolite.huthelper.base.presenter.BasePresenter;
import cn.nicolite.huthelper.db.dao.ConfigureDao;
import cn.nicolite.huthelper.model.bean.Configure;
import cn.nicolite.huthelper.model.bean.Electric;
import cn.nicolite.huthelper.model.bean.User;
import cn.nicolite.huthelper.network.api.APIUtils;
import cn.nicolite.huthelper.network.exception.ExceptionEngine;
import cn.nicolite.huthelper.utils.ListUtils;
import cn.nicolite.huthelper.view.activity.ElectricActivity;
import cn.nicolite.huthelper.view.iview.IElectricView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nicolite on 17-10-31.
 */

public class ElectricPresenter extends BasePresenter<IElectricView, ElectricActivity> {

    public ElectricPresenter(IElectricView view, ElectricActivity activity) {
        super(view, activity);
    }

    public void showElectricData(String lou, String hao){

        String userId = getLoginUser();

        if (TextUtils.isEmpty(userId)) {
            getView().showMessage("获取当前登录用户失败，请重新登录！");
            return;
        }

        final ConfigureDao configureDao = getDaoSession().getConfigureDao();
        List<Configure> list = configureDao.queryBuilder().where(ConfigureDao.Properties.UserId.eq(userId)).list();
        if (ListUtils.isEmpty(list)){
            getView().showMessage("获取用户信息失败！");
            return;
        }

        Configure configure = list.get(0);
        User user = list.get(0).getUser();

        configure.setLou(lou);
        configure.setHao(hao);
        configureDao.update(configure);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
        String date = simpleDateFormat.format(new Date());
        String env = lou + hao + date + user.getStudentKH() + configure.getAppRememberCode();

        APIUtils
                .getElectricAPI()
                .getElectric(lou, hao, user.getStudentKH(), configure.getAppRememberCode(), env)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Electric>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        getView().showLoading();
                    }

                    @Override
                    public void onNext(Electric electric) {
                        getView().closeLoading();
                        getView().showElectric(electric);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().closeLoading();
                        getView().showMessage(ExceptionEngine.handleException(e).getMsg());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
