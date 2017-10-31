package cn.nicolite.huthelper.presenter;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.util.List;

import cn.nicolite.huthelper.base.presenter.BasePresenter;
import cn.nicolite.huthelper.db.dao.ConfigureDao;
import cn.nicolite.huthelper.db.dao.UserDao;
import cn.nicolite.huthelper.model.bean.Configure;
import cn.nicolite.huthelper.model.bean.HttpResult;
import cn.nicolite.huthelper.model.bean.User;
import cn.nicolite.huthelper.network.api.APIUtils;
import cn.nicolite.huthelper.network.exception.ExceptionEngine;
import cn.nicolite.huthelper.utils.ListUtils;
import cn.nicolite.huthelper.view.activity.UserInfoActivity;
import cn.nicolite.huthelper.view.iview.IUserInfoView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * UserInfoPresenter
 * Created by nicolite on 17-10-28.
 */

public class UserInfoPresenter extends BasePresenter<IUserInfoView, UserInfoActivity> {
    public UserInfoPresenter(IUserInfoView view, UserInfoActivity activity) {
        super(view, activity);
    }

    public void showUserData() {
        SharedPreferences preferences = getActivity().getSharedPreferences("login_user", Context.MODE_PRIVATE);
        String userId = preferences.getString("userId", null);

        if (TextUtils.isEmpty(userId)) {
            getView().showMessage("获取当前登录用户失败，请重新登录！");
            return;
        }

        List<Configure> configureList = getConfigureList();
        if (ListUtils.isEmpty(configureList)){
            getView().showMessage("获取用户信息失败！");
            return;
        }

        User user = configureList.get(0).getUser();

        if (user == null) {
            getView().showMessage("获取用户信息失败！");
            return;
        }
        getView().showUserInfo(user);
    }

    public void uploadAvatar(final Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
        byte[] bytes = outputStream.toByteArray();
        RequestBody requestBody = RequestBody.create(MediaType.parse("img/jpeg"), bytes);
        MultipartBody.Part file = MultipartBody.Part.createFormData("file", "01.jpg", requestBody);

        String userId = getLoginUser();
        if (TextUtils.isEmpty(userId)) {
            getView().showMessage("获取当前登录用户失败，请重新登录！");
            return;
        }

        ConfigureDao configureDao = getDaoSession().getConfigureDao();
        List<Configure> list = configureDao.queryBuilder().where(ConfigureDao.Properties.UserId.eq(userId)).list();
        if (ListUtils.isEmpty(list)){
            getView().showMessage("获取用户信息失败！");
            return;
        }

        final Configure configure = list.get(0);
        final User user = configure.getUser();

        getView().showMessage("头像上传中！");

        APIUtils
                .getUploadAPI()
                .uploadAvatar(user.getStudentKH(), configure.getAppRememberCode(), file)
                .compose(getActivity().<HttpResult<String>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        getActivity().showLoading();
                    }

                    @Override
                    public void onNext(HttpResult<String> stringHttpResult) {
                        getActivity().closeLoading();
                        String msg;
                        switch (stringHttpResult.getMsg()) {
                            case "ok":
                                msg = "修改成功!";
                                getActivity().changeAvatarSuccess(bitmap);
                                user.setHead_pic_thumb(stringHttpResult.getData());
                                user.setHead_pic(stringHttpResult.getData());

                                break;
                            case "令牌错误":
                                msg = "修改失败：帐号异地登录，请重新登录！";
                                break;
                            default:
                                msg = stringHttpResult.getMsg();
                                break;
                        }
                        getActivity().showMessage(msg);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getActivity().closeLoading();
                        getActivity().showMessage(ExceptionEngine.handleException(e).getMsg());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void changAvatar() {
        AndPermission
                .with(getActivity())
                .requestCode(100)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                        if (requestCode == 100) {
                            getView().changeAvatar();
                        }
                    }

                    @Override
                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                        getView().showMessage("获取权限失败，请授予文件读写权限！");
                    }
                })
                .start();
    }

    public void changeUserName(final String userName) {
        String userId = getLoginUser();

        if (TextUtils.isEmpty(userId)) {
            getView().showMessage("获取当前登录用户失败，请重新登录！");
            return;
        }

        ConfigureDao configureDao = getDaoSession().getConfigureDao();
        List<Configure> list = configureDao.queryBuilder().where(ConfigureDao.Properties.UserId.eq(userId)).list();
        if (ListUtils.isEmpty(list)){
            getView().showMessage("获取用户信息失败！");
            return;
        }

        Configure configure = list.get(0);
        User user = list.get(0).getUser();

        getView().showMessage("昵称修改中！");
        APIUtils
                .getUserAPI()
                .changeUsername(user.getStudentKH(), configure.getAppRememberCode(), userName)
                .compose(getActivity().<HttpResult>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult httpResult) {
                        String msg;
                        switch (httpResult.getMsg()) {
                            case "ok":
                                msg = "修改成功！";
                                String userId = getLoginUser();

                                if (TextUtils.isEmpty(userId)) {
                                    getView().showMessage("获取当前登录用户失败，请重新登录！");
                                    return;
                                }

                                UserDao userDao = getDaoSession().getUserDao();
                                List<User> userList = userDao.queryBuilder().where(UserDao.Properties.User_id.eq(userId)).list();

                                if (ListUtils.isEmpty(userList)){
                                    getView().showMessage("获取用户信息失败！");
                                    return;
                                }

                                User user = userList.get(0);
                                user.setUsername(userName);
                                userDao.update(user);

                                break;
                            case "令牌错误":
                                msg = "令牌错误，请重新登录！";
                                break;
                            default:
                                msg = httpResult.getMsg();
                        }
                        getView().showMessage(msg);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showMessage(ExceptionEngine.handleException(e).getMsg());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
