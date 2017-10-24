package cn.nicolite.huthelper.presenter;

import android.content.Intent;

import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

import java.util.ArrayList;
import java.util.List;

import cn.nicolite.huthelper.BuildConfig;
import cn.nicolite.huthelper.base.presenter.BasePresenter;
import cn.nicolite.huthelper.model.bean.HttpResult;
import cn.nicolite.huthelper.model.bean.Menu;
import cn.nicolite.huthelper.model.bean.Menu_;
import cn.nicolite.huthelper.model.bean.TimeAxis;
import cn.nicolite.huthelper.model.bean.Update;
import cn.nicolite.huthelper.model.bean.Weather;
import cn.nicolite.huthelper.network.api.APIUtils;
import cn.nicolite.huthelper.network.exception.ExceptionEngine;
import cn.nicolite.huthelper.utils.ListUtils;
import cn.nicolite.huthelper.utils.LogUtils;
import cn.nicolite.huthelper.view.activity.MainActivity;
import cn.nicolite.huthelper.view.iview.IMainView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nicolite on 17-10-22.
 */

public class MainPresenter extends BasePresenter<IMainView, MainActivity> {

    public MainPresenter(IMainView view, MainActivity activity) {
        super(view, activity);
    }

    public void showWeather() {
        APIUtils
                .getWeatherAPI()
                .getWeather()
                .compose(getActivity().<Weather>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Weather>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        if (getView() != null) {
                            getView().showLoading();
                        }
                    }

                    @Override
                    public void onNext(@NonNull Weather weather) {
                        if (getView() != null) {
                            getView().closeLoading();
                            if (weather != null) {
                                getView().showWeather(weather);
                            } else {
                                getView().showMessage("未获取到天气数据！");
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (getView() != null) {
                            getView().closeLoading();
                            getView().showMessage(ExceptionEngine.handleException(e).getMsg());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void showDateLine() {
        APIUtils
                .getDateLineAPI()
                .getTimeAxis()
                .compose(getActivity().<List<TimeAxis>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<TimeAxis>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        if (getView() != null) {
                            getView().showLoading();
                        }
                    }

                    @Override
                    public void onNext(@NonNull List<TimeAxis> timeAxisList) {
                        if (getView() != null) {
                            getView().closeLoading();
                            if (!ListUtils.isEmpty(timeAxisList)) {
                                getView().showDateLine(timeAxisList);
                            } else {
                                getView().showMessage("未获取到时间轴数据！");
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (getView() != null) {
                            getView().closeLoading();
                            getView().showMessage(ExceptionEngine.handleException(e).getMsg());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void showNotification() {

    }

    public void showSyllabus() {

    }

    public void showMenu() {

        if (boxHelper.getMenuBox().count() < 15) {
            List<Menu> menuItems = new ArrayList<>();
            Menu item = new Menu(0, 0, 0, 1, "cn.nicolite.huthelper.view.activity.WebViewActivity", "图书馆", true);
            menuItems.add(item);
            item = new Menu(1, 1, 1, 0, "cn.nicolite.huthelper.view.activity.CourseTableActivity", "课程表", true);
            menuItems.add(item);
            item = new Menu(2, 2, 2, 0, "cn.nicolite.huthelper.view.activity.ExamActivity", "考试查询", true);
            menuItems.add(item);
            item = new Menu(3, 3, 3, 0, "cn.nicolite.huthelper.view.activity.NewGradeActivity", "成绩查询", true);
            menuItems.add(item);
            item = new Menu(4, 4, 4, 2, "cn.nicolite.huthelper.view.activity.WebViewActivity", "网上作业", true);
            menuItems.add(item);
            item = new Menu(5, 5, 5, 0, "cn.nicolite.huthelper.view.activity.MarketActivity", "二手市场", true);
            menuItems.add(item);
            item = new Menu(6, 6, 6, 0, "cn.nicolite.huthelper.view.activity.SayActivity", "校园说说", true);
            menuItems.add(item);
            item = new Menu(7, 7, 7, 0, "cn.nicolite.huthelper.view.activity.ElecticActivity", "电费查询", true);
            menuItems.add(item);
            item = new Menu(8, 8, 8, 0, "cn.nicolite.huthelper.view.activity.OfferActivity", "校招薪水", false);
            menuItems.add(item);
            item = new Menu(9, 9, 9, 0, "cn.nicolite.huthelper.view.activity.ExpLessonActivity", "实验课表", true);
            menuItems.add(item);
            item = new Menu(10, 10, 10, 0, "cn.nicolite.huthelper.view.activity.CalendarActivity", "校历", false);
            menuItems.add(item);
            item = new Menu(11, 11, 11, 0, "cn.nicolite.huthelper.view.activity.LoseListActivity", "失物招领",true);
            menuItems.add(item);
            item = new Menu(12, 12, 12, 0, "cn.nicolite.huthelper.view.activity.CareerTalkActivity", "宣讲会", true);
            menuItems.add(item);
            item = new Menu(13, 13, 13, 0, "cn.nicolite.huthelper.view.activity.AllActivity", "全部", true);
            menuItems.add(item);
            item = new Menu(14, 14, 14, 0, "cn.nicolite.huthelper.view.activity.VideoListActivity", "视频专栏", false);
            menuItems.add(item);
            item = new Menu(15, 15, 15, 0, "cn.nicolite.huthelper.view.activity.FreshmanHelpActivity", "新生攻略", false);
            menuItems.add(item);

            boxHelper.getMenuBox().put(menuItems);
        }

        List<Menu> menuList = boxHelper.getMenuBox().query().order(Menu_.index).equal(Menu_.isMain, 1).build().find();

        if (getView() != null){
            getView().showMenu(menuList);
        }

    }

    //必须调用此接口以记录该用户是否使用工大助手
    public void checkUpdate(String num, String versionCode) {
        APIUtils
                .getUpdateAPI()
                .checkUpdate(num, versionCode)
                .compose(getActivity().<HttpResult<Update>>bindToLifecycle())
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<Update>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull HttpResult<Update> updateHttpResult) {

                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void connectRongIM() {

    }

    public void initPush(String studentKH) {
        XGPushManager.registerPush(getActivity().getApplicationContext(), studentKH, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object o, int i) {
                LogUtils.d(TAG, "注册成功，设备token为：" + o);
            }

            @Override
            public void onFail(Object o, int i, String s) {
                LogUtils.d(TAG, "注册失败，错误码：" + i + ",错误信息：" + s);
                getView().showMessage("推送注册失败：" + s);
            }
        });
        XGPushConfig.enableDebug(getActivity().getApplicationContext(), BuildConfig.LOG_DEBUG);
    }

    public void initNotice() {

    }

    public void share()
    {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        intent.putExtra(Intent.EXTRA_TEXT, "工大助手下载连接：https://www.coolapk.com/apk/cn.nicolite.huthelper");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(Intent.createChooser(intent, "分享"));
    }
}
