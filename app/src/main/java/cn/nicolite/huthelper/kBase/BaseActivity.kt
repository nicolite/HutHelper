package cn.nicolite.huthelper.kBase

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.PixelFormat
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import cn.nicolite.huthelper.R
import cn.nicolite.huthelper.db.DaoUtils
import cn.nicolite.huthelper.db.dao.DaoSession
import cn.nicolite.huthelper.listener.ActivityLifeCycleListener
import cn.nicolite.huthelper.manager.ActivityStackManager
import cn.nicolite.huthelper.model.bean.Configure
import cn.nicolite.huthelper.utils.LogUtils
import cn.nicolite.huthelper.utils.SlidrUtils
import cn.nicolite.huthelper.utils.StatusBarUtils
import com.tencent.stat.StatService
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity

/**
 * Created by nicolite on 2018/5/20.
 * email nicolite@nicolite.cn
 * kotlin Activity 基类 包含生命周期管理
 * 所有kotlin Activity都要继承此类
 */
abstract class BaseActivity : RxAppCompatActivity() {
    protected val TAG = javaClass.simpleName
    protected lateinit var context: Context
    protected lateinit var activity: AppCompatActivity
    protected val daoSession: DaoSession = DaoUtils.getDaoSession()
    protected val loginUserId: String = DaoUtils.getLoginUser()
    protected val configureList: List<Configure> = DaoUtils.getConfigureList()
    private var lifeCycleListener: ActivityLifeCycleListener? = null

    companion object {
        protected const val SENSOR = 697
        protected const val PORTRAIT = 519
        protected const val LANDSCAPE = 539
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtils.d(TAG, "$TAG-->onCreate()")
        lifeCycleListener?.onCreate(savedInstanceState)
        ActivityStackManager.getManager().push(this)
        initConfig(savedInstanceState)
        setContentView(setLayoutId())
        context = this
        activity = this
        initBundleData(intent.extras)
        doBusiness()
    }

    override fun onStart() {
        super.onStart()
        LogUtils.d(TAG, "$TAG-->onStart()")
        lifeCycleListener?.onStart()
    }

    override fun onResume() {
        super.onResume()
        LogUtils.d(TAG, "$TAG-->onResume()")
        lifeCycleListener?.onResume()
        StatService.onResume(context)
    }

    override fun onPause() {
        super.onPause()
        LogUtils.d(TAG, "$TAG-->onPause()")
        lifeCycleListener?.onPause()
        StatService.onPause(context)
    }

    override fun onStop() {
        super.onStop()
        LogUtils.d(TAG, "$TAG-->onStop()")
        lifeCycleListener?.onStop()
        StatService.onStop(context)
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtils.d(TAG, "$TAG-->onDestroy()")
        ActivityStackManager.getManager().remove(this)
        lifeCycleListener?.onDestroy()
    }

    override fun onRestart() {
        super.onRestart()
        LogUtils.d(TAG, "$TAG-->onRestart()")
        lifeCycleListener?.onRestart()
    }

    /**
     * 初始化Activity配置,
     */
    protected open fun initConfig(savedInstanceState: Bundle?) {
        LogUtils.d(TAG, "$TAG-->initConfig()")
    }

    /**
     * 初始化Bundle参数
     */
    protected open fun initBundleData(bundle: Bundle?) {
        LogUtils.d(TAG, "$TAG-->initBundleData()")
    }

    /**
     * 获取 xml layout
     */
    protected open fun setLayoutId(): Int {
        LogUtils.d(TAG, "$TAG-->setLayoutId()")
        return R.layout.activity_default
    }

    /**
     * 业务逻辑代码
     */
    protected open fun doBusiness() {
        LogUtils.d(TAG, "$TAG-->doBusiness()")
    }

    /**
     * 设置生命周期监听
     */
    fun setOnLifeCycleListener(lifecycleListener: ActivityLifeCycleListener) {
        this.lifeCycleListener = lifecycleListener
    }

    /**
     * 页面跳转
     */
    fun startActivity(clazz: Class<*>) {
        val intent = Intent(this, clazz)
        startActivity(intent)
    }

    /**
     * 页面携带数据跳转
     *
     * @param clazz
     * @param bundle
     */
    fun startActivity(clazz: Class<*>, bundle: Bundle?) {
        val intent = Intent()
        intent.setClass(this, clazz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    /**
     * 包含回调的页面跳转
     *
     * @param clazz
     * @param requestCode
     */
    fun startActivityForResult(clazz: Class<*>, requestCode: Int) {
        val intent = Intent()
        intent.setClass(this, clazz)
        startActivityForResult(intent, requestCode)
    }

    /**
     * 带动画的页面跳转
     *
     * @param clazz
     * @param options ActivityOptionsCompat.makeSceneTransitionAnimation()
     */
    fun startActivityWithOptions(clazz: Class<*>, options: Bundle?) {
        val intent = Intent()
        intent.setClass(this, clazz)
        if (options != null) {
            startActivity(intent, options)
        }
    }

    /**
     * 带数据和动画的页面跳转
     *
     * @param clazz
     * @param bundle  数据
     * @param options ActivityOptionsCompat.makeSceneTransitionAnimation()
     */
    fun startActivity(clazz: Class<*>, bundle: Bundle?, options: Bundle?) {
        val intent = Intent()
        intent.setClass(this, clazz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        if (options != null) {
            startActivity(intent, options)
        }
    }

    /**
     * 包含回调和数据的页面跳转
     *
     * @param clazz
     * @param bundle
     * @param requestCode
     */
    fun startActivityForResult(clazz: Class<*>, bundle: Bundle?, requestCode: Int) {
        val intent = Intent()
        intent.setClass(this, clazz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivityForResult(intent, requestCode)
    }


    fun setPixelFormat() {
        this.window.setFormat(PixelFormat.TRANSLUCENT)
    }

    /**
     * 是否允许全屏
     */
    fun setFullScreen() {
        this.window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    fun hideToolBar() {
        val actionBar = supportActionBar
        actionBar?.hide()
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    /**
     * 设置屏幕旋转
     *
     * @param rotate SENSOR根据传感器自动旋转 PORTRAIT竖屏 LANDSCAPE横屏
     */
    fun setScreenRotate(rotate: Int) {
        when (rotate) {
            SENSOR -> requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
            PORTRAIT -> requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            LANDSCAPE -> requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
    }

    /**
     * 是否设置沉浸状态栏
     *
     * @param isSetStatusBar
     */
    fun setImmersiveStatusBar() {
        StatusBarUtils.setImmersiveStatusBar(this.window)
    }

    /**
     * 使布局背景填充状态栏
     */
    fun setLayoutNoLimits() {
        // 布局背景填充状态栏 与键盘监听冲突
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    /**
     * 设置状态栏字体为深色
     */
    fun setDeepColorStatusBar() {
        StatusBarUtils.setDeepColorStatusBar(this.window)
    }

    /**
     * 是否设置滑动退出
     * 需要在主题中设置<item name="android:windowIsTranslucent">true</item>，否则将显示异常
     */
    fun setSlideExit() {
        SlidrUtils.setSlidrExit(this)
    }
}