package cn.nicolite.huthelper.view.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.activity.BaseActivity;
import cn.nicolite.huthelper.utils.ListUtils;

/**
 * 闪屏页
 * Created by nicolite on 17-10-17.
 */

public class SplashActivity extends BaseActivity {

    @BindView(R.id.img)
    ImageView imageView;

    private final int[] bgs = {R.drawable.start_1, R.drawable.start_2, R.drawable.start_3,
            R.drawable.start_4, R.drawable.start_5};

    private static final int what = 958;
    private static final int finish = 156;

    private final MyHandler handler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private final WeakReference<SplashActivity> activityWeakReference;

        private MyHandler(SplashActivity activity) {
            this.activityWeakReference = new WeakReference<SplashActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SplashActivity splashActivity = activityWeakReference.get();
            switch (msg.what) {
                case what:
                    if (splashActivity.isLogin()) {
                        splashActivity.startActivity(MainActivity.class);
                    } else {
                        splashActivity.startActivity(LoginActivity.class);
                    }
                    //解决MIUI9上Splash结束后出现应用图标
                    splashActivity.handler.sendEmptyMessageDelayed(finish, 3000);
                    break;
                case finish:
                    if (splashActivity != null) {
                        splashActivity.finish();
                    }
                    break;
            }
        }
    }

    @Override
    protected void initConfig(Bundle savedInstanceState) {
        setImmersiveStatusBar(true);
    }

    @Override
    protected void initBundleData(Bundle bundle) {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void doBusiness() {
        Glide
                .with(this)
                .load(bgs[(int) (Math.random() * 4)])
                .asBitmap()
                .centerCrop()
                .crossFade()
                .into(imageView);
        handler.sendEmptyMessageDelayed(what, 1500);
    }

    public boolean isLogin() {
        SharedPreferences preferences = getSharedPreferences("login_user", MODE_PRIVATE);
        String userId = preferences.getString("userId", null);
        return userId != null && !userId.equals("*") && !ListUtils.isEmpty(getConfigureList());
    }
}
