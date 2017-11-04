package cn.nicolite.huthelper.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.support.RxFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.nicolite.huthelper.listener.FragmentLifeCycleListener;
import cn.nicolite.huthelper.utils.LogUtils;

/**
 * Fragment基类，所有Fragment都要继承此类
 * Created by nicolite on 17-10-13.
 */

public abstract class BaseFragment extends RxFragment {
    protected final String TAG = getClass().getSimpleName();
    protected Unbinder unbinder;
    protected FragmentLifeCycleListener lifeCycleListener;
    protected Context context;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtils.d(TAG, TAG + "-->onAttach()");
        if (lifeCycleListener != null) {
            lifeCycleListener.onAttach(context);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.d(TAG, TAG + "-->onCreate()");
        if (lifeCycleListener != null) {
            lifeCycleListener.onCreate(savedInstanceState);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtils.d(TAG, TAG + "-->onCreateView()");
        if (lifeCycleListener != null) {
            lifeCycleListener.onCreateView(inflater, container, savedInstanceState);
        }

        View view = inflater.inflate(setLayoutId(), container, false);
        initConfig(savedInstanceState);

        Bundle arguments = getArguments();
        initBundleData(arguments);

        context = getContext();

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtils.d(TAG, TAG + "-->onActivityCreated()");
        if (lifeCycleListener != null) {
            lifeCycleListener.onActivityCreated(savedInstanceState);
        }
        doBusiness();
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.d(TAG, TAG + "-->onStart()");
        if (lifeCycleListener != null) {
            lifeCycleListener.onStart();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d(TAG, TAG + "-->onResume()");
        if (lifeCycleListener != null) {
            lifeCycleListener.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.d(TAG, TAG + "-->onPause()");
        if (lifeCycleListener != null) {
            lifeCycleListener.onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.d(TAG, TAG + "-->onStop()");
        if (lifeCycleListener != null) {
            lifeCycleListener.onStop();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtils.d(TAG, TAG + "-->onDestroyView()");
        if (lifeCycleListener != null) {
            lifeCycleListener.onDestroyView();
        }
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d(TAG, TAG + "-->onDestroy()");
        if (lifeCycleListener != null) {
            lifeCycleListener.onDestroy();
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtils.d(TAG, TAG + "-->onDetach()");
        if (lifeCycleListener != null) {
            lifeCycleListener.onDetach();
        }
    }

    /**
     * 设置生命周期监听
     *
     * @param lifecycleListener
     */
    public void setOnLifeCycleListener(FragmentLifeCycleListener lifecycleListener) {
        this.lifeCycleListener = lifecycleListener;
    }

    /**
     * 初始化Activity配置,
     */
    protected abstract void initConfig(Bundle savedInstanceState);

    /**
     * 初始化Bundle参数
     *
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


}
