package cn.nicolite.huthelper.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.nicolite.huthelper.app.MApplication;
import cn.nicolite.huthelper.db.DaoHelper;
import cn.nicolite.huthelper.db.dao.ConfigureDao;
import cn.nicolite.huthelper.db.dao.DaoSession;
import cn.nicolite.huthelper.model.bean.Configure;
import cn.nicolite.huthelper.model.bean.User;
import cn.nicolite.huthelper.model.bean.Valid;
import cn.nicolite.huthelper.network.api.APIUtils;
import cn.nicolite.huthelper.utils.ListUtils;
import cn.nicolite.huthelper.view.activity.LoginActivity;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * 登录服务，检测是否已在其它设备登录
 * Created by nicolite on 17-11-2.
 */

public class LoginService extends IntentService {

    private static final int DELAY = 3000;
    private static final int PERIOD = 1000 * 60 * 60;
    private static final String ACTION_INIT_WHEN_APP_CREATE = "cn.nicolite.huthelper.service.action.INIT";

    public LoginService(){
        super("LoginService");
    }

    public LoginService(String name) {
        super(name);
    }

    public static void start(Context context){
        Intent intent = new Intent(context, LoginService.class);
        intent.setAction(ACTION_INIT_WHEN_APP_CREATE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                isLoginOnOtherPlace();
            }
        }, DELAY, PERIOD);
    }

    private void isLoginOnOtherPlace(){
        DaoSession daoSession = DaoHelper.getDaoHelper(MApplication.AppContext).getDaoSession();
        SharedPreferences preferences = getSharedPreferences("login_user", Context.MODE_PRIVATE);
        String userId = preferences.getString("userId", null);

        if (userId == null || userId.equals("*")){
            return;
        }

        List<Configure> list = daoSession.getConfigureDao().queryBuilder().where(ConfigureDao.Properties.UserId.eq(userId)).list();

        if (ListUtils.isEmpty(list)){
            return;
        }

        Configure configure = list.get(0);
        User user = configure.getUser();

        APIUtils
                .getMessageAPI()
                .isValid(user.getStudentKH(), configure.getAppRememberCode())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Valid>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Valid valid) {
                        if (!valid.isCode()){
                            startLogin();
                           // final CommonDialog commonDialog = new CommonDialog(MApplication.application);
                           // commonDialog
                           //         .setTitle("下线通知")
                           //         .setMessage("你的帐号已在另一台设备登录。如非本人操作，则密码可能已泄露，建议修改密码。")
                           //         .setPositiveButton("重新登录", new View.OnClickListener() {
                           //             @Override
                           //             public void onClick(View view) {
                           //                 startLogin();
                           //                 commonDialog.dismiss();
                           //             }
                           //         })
                           //         .setNegativeButton("退出", null)
                           //         .show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void startLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
