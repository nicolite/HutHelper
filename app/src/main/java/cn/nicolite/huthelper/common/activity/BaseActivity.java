package cn.nicolite.huthelper.common.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.WindowManager;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.nicolite.huthelper.listener.LifeCycleListener;
import cn.nicolite.huthelper.manager.ActivityStackManager;
import cn.nicolite.huthelper.utils.LogUtils;
import cn.nicolite.huthelper.utils.SlidrUtils;
import cn.nicolite.huthelper.utils.StatusBarUtils;

/**
 * Activity 基类
 * 所有Activity都要继承此类
 * Created by nicolite on 17-9-6.
 */

public abstract class BaseActivity extends RxAppCompatActivity {
    /**Log Tag*/
    protected final String TAG = getClass().getSimpleName();
    protected Context context;
    protected LifeCycleListener lifeCycleListener;
    protected Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.d(TAG, TAG + "-->onCreate()");
        if (lifeCycleListener != null){
            lifeCycleListener.onCreate(savedInstanceState);
        }
        ActivityStackManager.getManager().push(this);
        Bundle bundle = getIntent().getExtras();
        initConfig(savedInstanceState);
        setContentView(setLayoutId());
        context = this;
        unbinder = ButterKnife.bind(this);
        initBundleData(bundle);
        doBusiness();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.d(TAG, TAG + "-->onStart()");
        if (lifeCycleListener != null){
            lifeCycleListener.onStart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.d(TAG, TAG + "-->onResume()");
        if (lifeCycleListener != null){
            lifeCycleListener.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.d(TAG, TAG + "-->onPause()");
        if (lifeCycleListener != null){
            lifeCycleListener.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.d(TAG, TAG + "-->onStop()");
        if (lifeCycleListener != null){
            lifeCycleListener.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.d(TAG, TAG + "-->onDestroy()");
        if (lifeCycleListener != null){
            lifeCycleListener.onDestroy();
        }
        if (unbinder != null){
            unbinder.unbind();
        }
        ActivityStackManager.getManager().remove(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtils.d(TAG, TAG + "-->onRestart()");
        if (lifeCycleListener != null){
            lifeCycleListener.onRestart();
        }
    }

    /**
     * 初始化Activity配置,
     */
    protected abstract void initConfig(Bundle savedInstanceState);

    /**
     * 初始化Bundle参数
     * @param bundle
     */
    protected abstract void initBundleData(Bundle bundle);

    /**
     * 获取 xml layout
     */
    protected abstract int setLayoutId();

    /**
     * 业务逻辑代码
     */
    protected abstract void doBusiness();

    /**
     * 设置生命周期监听
     * @param lifecycleListener
     */
    public void setOnLifeCycleListener(LifeCycleListener lifecycleListener){
        this.lifeCycleListener = lifecycleListener;
    }

    /**
     * 页面跳转
     * @param clazz
     */
    public void startActivity(Class<?> clazz){
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    /**
     * 页面携带数据跳转
     * @param clazz
     * @param bundle
     */
    public void startActivity(Class<?> clazz, Bundle bundle){
        Intent intent = new Intent();
        intent.setClass(this, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 包含回调的页面跳转
     * @param clazz
     * @param requestCode
     */
    public void startActivityForResult(Class<?> clazz, int requestCode){
        Intent intent = new Intent();
        intent.setClass(this, clazz);
        startActivityForResult(intent, requestCode);
    }

    /**
     *包含回调和数据的页面跳转
     * @param clazz
     * @param bundle
     * @param requestCode
     */
    public void startActivityForResult(Class<?> clazz, Bundle bundle, int requestCode){
        Intent intent = new Intent();
        intent.setClass(this, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 是否允许全屏
     * @param isAllowFullScreen
     */
    public void setAllowFullScreen(boolean isAllowFullScreen){
        if (isAllowFullScreen){
            this.getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    public void hideToolBar(boolean hide){
        if (hide){
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null){
                actionBar.hide();
            }
            //requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
    }

    /**
     * 是否允许旋转
     * @param isAllowScreenRotate
     */
    public void setScreenRotate(boolean isAllowScreenRotate){
        if (isAllowScreenRotate){
            //TODO 允许旋转
        }else {
            //TODO 不允许
        }
    }

    /**
     * 是否设置沉浸状态栏
     * @param isSetStatusBar
     */
    public void setImmersiveStatusBar(boolean isSetStatusBar){
        if (isSetStatusBar){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                // 透明状态栏
                getWindow().addFlags(
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                // 透明导航栏
                getWindow().addFlags(
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }
    }

    /**
     * 设置状态栏字体为深色
     * @param isDeepColor
     */
    public void setStatusBarFontDeepColor(boolean isDeepColor){
        if (isDeepColor){
            StatusBarUtils.setStatusBarFontDeepColor(this.getWindow(), isDeepColor);
        }
    }

    /**
     * 是否设置滑动退出
     * 需要在主题中设置<item name="android:windowIsTranslucent">true</item>，否则将显示异常
     * @param isSlideExit
     */
    public void setSlideExit(boolean isSlideExit){
        if (isSlideExit){
            SlidrUtils.setSlidrExit(this);
        }
    }

}
