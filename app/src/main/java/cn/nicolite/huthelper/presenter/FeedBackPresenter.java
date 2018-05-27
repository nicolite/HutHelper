package cn.nicolite.huthelper.presenter;

import android.os.Build;
import android.text.TextUtils;

import cn.nicolite.huthelper.BuildConfig;
import cn.nicolite.huthelper.base.BasePresenter;
import cn.nicolite.huthelper.model.bean.User;
import cn.nicolite.huthelper.network.APIUtils;
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

        User user = configure.getUser();

        if (TextUtils.isEmpty(content)) {
            if (getView() != null) {
                getView().showMessage("反馈意见不能为空！");
            }
        } else if (contact.length() > 200) {
            if (getView() != null) {
                getView().showMessage("字数超过限制！");
            }
        } else if (TextUtils.isEmpty(contact)) {
            if (getView() != null) {
                getView().showMessage("联系方式不能为空！");
            }
        } else {
            String version = "版本：Android " + BuildConfig.VERSION_NAME + "（" + BuildConfig.VERSION_CODE + "）";
            String model = "机型：" + Build.MANUFACTURER + Build.MODEL + "（Android " + Build.VERSION.RELEASE + "）";
            String from = "来源：" + user.getTrueName() + " " + user.getClass_name() + " " + user.getStudentKH();

            if (!TextUtils.isEmpty(version) && !TextUtils.isEmpty(model)) {
                content = from + "<br/>" + version + "<br/>" + model + "<br/>" + "内容：" + content;
            }

            APIUtils.INSTANCE
                    .getFeedBackAPI()
                    .feedBack(contact, content)
                    .compose(getActivity().<ResponseBody>bindToLifecycle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ResponseBody>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            if (getView() != null) {
                                getView().showLoading();
                            }
                        }

                        @Override
                        public void onNext(@NonNull ResponseBody responseBody) {
                            if (getView() != null) {
                                getView().closeLoading();
                                getView().onSuccess();
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
    }
}
