package cn.nicolite.huthelper.presenter;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

import cn.nicolite.huthelper.base.presenter.BasePresenter;
import cn.nicolite.huthelper.network.api.APIUtils;
import cn.nicolite.huthelper.network.exception.ExceptionEngine;
import cn.nicolite.huthelper.view.activity.FeedBackActivity;
import cn.nicolite.huthelper.view.iview.IFeedBackView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by nicolite on 17-10-24.
 */

public class FeedBackPresenter extends BasePresenter<IFeedBackView, FeedBackActivity> {

    public FeedBackPresenter(IFeedBackView view, FeedBackActivity activity) {
        super(view, activity);
    }

    public void feeBack(String content, String contact) {

        if (TextUtils.isEmpty(content)) {

            getView().showMessage("反馈意见不能为空！");
        } else if (contact.length() > 200) {

            getView().showMessage("字数超过限制！");

        } else if (TextUtils.isEmpty(contact)) {
            getView().showMessage("联系方式不能为空！");
        } else {
            String version = null, model = null, from = null;
            try {
                PackageManager pm = getActivity().getPackageManager();
                PackageInfo pi = pm.getPackageInfo(getActivity().getPackageName(), PackageManager.GET_ACTIVITIES);
                version = "版本：Android " + pi.versionName + " (" + pi.versionCode + ")";
                model = "机型：" + Build.MANUFACTURER + Build.MODEL + " (Android" + Build.VERSION.RELEASE + ")";
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            from = "来源：" + boxHelper.getUserBox().get(1).getStudentKH();
            content = "内容：";

            if (!TextUtils.isEmpty(version) && !TextUtils.isEmpty(model)) {
                content = from + "<br/>" + version + "<br/>" + model + "<br/>" + content;
            }

            APIUtils
                    .getFeedBackAPI()
                    .feedBack(contact, content)
                    .compose(getActivity().<ResponseBody>bindToLifecycle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ResponseBody>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            getView().showLoading();
                        }

                        @Override
                        public void onNext(@NonNull ResponseBody responseBody) {
                            getView().closeLoading();
                            getView().onSuccess();
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            getView().closeLoading();
                            getView().showMessage(ExceptionEngine.handleException(e).getMsg());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }
}
