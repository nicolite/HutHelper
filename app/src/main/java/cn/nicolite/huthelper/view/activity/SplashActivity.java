package cn.nicolite.huthelper.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import butterknife.BindView;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.activity.BaseActivity;

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

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            startActivity(LoginActivity.class);
            finish();
        }
    };

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
        imageView.setImageResource(bgs[(int) (Math.random() * 4)]);
        handler.sendEmptyMessageDelayed(what, 2000);
    }

}
