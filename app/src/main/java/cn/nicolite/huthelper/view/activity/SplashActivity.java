package cn.nicolite.huthelper.view.activity;

import android.text.TextUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.BaseActivity;
import cn.nicolite.huthelper.model.bean.Configure;
import cn.nicolite.huthelper.utils.ListUtils;

/**
 * 闪屏页
 * Created by nicolite on 17-10-17.
 */

public class SplashActivity extends BaseActivity {
    private Timer timer = null;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void doBusiness() {
        timer = timer == null ? new Timer() : timer;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isLogin()) {
                    startActivity(MainActivity.class);
                } else {
                    startActivity(LoginActivity.class);
                }
            }
        }, 680);
    }

    public boolean isLogin() {
        //对1.3.8以前的版本做兼容，需要重新登录, 版本迭代完成后可删除
        List<Configure> configureList = getConfigureList();
        if (!ListUtils.isEmpty(configureList)) {
            if (TextUtils.isEmpty(configureList.get(0).getStudentKH())) {
                return false;
            }
        }
        return userId != null && !userId.equals("*") && !ListUtils.isEmpty(getConfigureList());
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (timer != null) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        timer = null;
        super.onDestroy();
    }
}
