package cn.nicolite.huthelper.presenter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.nicolite.huthelper.base.BasePresenter;
import cn.nicolite.huthelper.model.bean.HttpResult;
import cn.nicolite.huthelper.model.bean.User;
import cn.nicolite.huthelper.network.APIUtils;
import cn.nicolite.huthelper.network.exception.ExceptionEngine;
import cn.nicolite.huthelper.utils.EncryptUtils;
import cn.nicolite.huthelper.view.fragment.UserListFragment;
import cn.nicolite.huthelper.view.iview.IUserListView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nicolite on 17-11-11.
 */

public class UserListPresenter extends BasePresenter<IUserListView, UserListFragment> {
    public UserListPresenter(IUserListView view, UserListFragment activity) {
        super(view, activity);
    }

    public void showUsers(String name) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
        String date = simpleDateFormat.format(new Date());
        String env = EncryptUtils.SHA1(configure.getStudentKH() + configure.getAppRememberCode() + date);

        APIUtils.INSTANCE
                .getUserAPI()
                .getStudents(configure.getStudentKH(), configure.getAppRememberCode(), env, name)
                .compose(getActivity().<HttpResult<List<User>>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<User>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (getView() != null) {
                            getView().showLoading();
                        }
                    }

                    @Override
                    public void onNext(HttpResult<List<User>> listHttpResult) {
                        if (getView() != null) {
                            getView().closeLoading();
                            if (listHttpResult.getCode() == 200){
                                getView().showUsers(listHttpResult.getData());
                            }else {
                                getView().loadFailure();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().closeLoading();
                            getView().loadFailure();
                            getView().showMessage(ExceptionEngine.handleException(e).getMsg());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
