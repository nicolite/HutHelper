package cn.nicolite.huthelper.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.BaseActivity;
import cn.nicolite.huthelper.model.bean.Configure;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
    }


    private MyHandler handler = new MyHandler(this);

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
                    if (splashActivity != null) {
                        if (splashActivity.isLogin()) {
                            splashActivity.startActivity(MainActivity.class);
                        } else {
                            splashActivity.startActivity(LoginActivity.class);
                        }
                        splashActivity.finish();
                    }
                    break;
            }
        }
    }

    @Override
    protected void initConfig(Bundle savedInstanceState) {
        setImmersiveStatusBar();
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
        handler.sendEmptyMessageDelayed(what, 1000);
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
    protected void onDestroy() {
        handler = null;
        super.onDestroy();
    }
}
