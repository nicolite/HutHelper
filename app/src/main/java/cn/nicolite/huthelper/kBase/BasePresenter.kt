package cn.nicolite.huthelper.kBase

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.ViewGroup
import cn.nicolite.huthelper.db.DaoUtils
import cn.nicolite.huthelper.db.dao.DaoSession
import cn.nicolite.huthelper.listener.ActivityLifeCycleListener
import cn.nicolite.huthelper.listener.FragmentLifeCycleListener
import cn.nicolite.huthelper.model.bean.Configure
import java.lang.ref.Reference
import java.lang.ref.WeakReference

/**
 * Created by nicolite on 2018/5/20.
 * email nicolite@nicolite.cn
 *  kotlin BasePresenter 基类
 * 所有kotlin BasePresenter都要继承此类
 */
abstract class BasePresenter<I, V>(iView: I, view: V) : ActivityLifeCycleListener, FragmentLifeCycleListener {
    protected val TAG = javaClass.simpleName
    protected lateinit var iViewRef: Reference<I>
    protected lateinit var viewRef: Reference<V>
    protected val daoSession: DaoSession = DaoUtils.getDaoSession()
    protected val userId: String = DaoUtils.getLoginUser()
    protected val configureList: List<Configure> = DaoUtils.getConfigureList()
    protected val configure: Configure = configureList[0]
    protected var context: Context? = null
    protected var activity: AppCompatActivity? = null
    protected var fragment: Fragment? = null

    init {
        attachIView(iView)
        attachView(view)
        setLifeCycleListener(view)
    }

    /**
     * 设置生命周期监听
     */
    private fun setLifeCycleListener(view: V) {
        when (view) {
            is BaseActivity -> view.setOnLifeCycleListener(this)
            is BaseFragment -> view.setOnLifeCycleListener(this)
        }
    }

    /**
     * 绑定IView
     */
    private fun attachIView(iView: I) {
        iViewRef = WeakReference<I>(iView)
    }

    /**
     * 绑定View
     */
    private fun attachView(view: V) {
        viewRef = WeakReference<V>(view)
    }

    /**
     * 解除IView绑定
     */
    private fun detachIView() {
        if (isIViewAttached()) {
            iViewRef.clear()
        }
    }

    /**
     * 解除View绑定
     */
    private fun detachView() {
        if (isViewAttached()) {
            context = null
            activity = null
            viewRef.clear()
        }
    }

    /**
     * 获取IView
     */
    protected fun getIView(): I? {
        return iViewRef.get()
    }

    /**
     * 获取View
     */
    protected fun getView(): V? {
        return viewRef.get()
    }

    /**
     * 判断是否已经绑定IView
     */
    protected fun isIViewAttached(): Boolean {
        return iViewRef.get() != null
    }

    /**
     * 判断是否已经绑定View
     */
    protected fun isViewAttached(): Boolean {
        return viewRef.get() != null
    }

    override fun onCreate(saveInstanceState: Bundle?) {
        val view = getView()
        if (view is BaseActivity) {
            context = view
            activity = view
        }
    }

    override fun onStart() {}

    override fun onResume() {}

    override fun onPause() {}

    override fun onStop() {}

    override fun onDestroy() {
        if (getView() is BaseActivity) {
            detachIView()
            detachView()
        }
    }

    override fun onRestart() {}

    override fun onAttach(context: Context?) {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) {}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        val view = getView()
        if (view is BaseFragment) {
            context = view.context
            fragment = view
            activity = view.activity as AppCompatActivity
        }
    }

    override fun onDestroyView() {
        if (getView() is BaseFragment) {
            detachIView()
            detachView()
        }
    }

    override fun onDetach() {}

}