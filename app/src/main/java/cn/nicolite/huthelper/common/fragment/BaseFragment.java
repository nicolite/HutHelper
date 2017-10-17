package cn.nicolite.huthelper.common.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.support.RxFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.nicolite.huthelper.utils.LogUtils;

/**
 * Fragment基类，所有Fragment都要继承此类
 * Created by nicolite on 17-10-13.
 */

public abstract class BaseFragment extends RxFragment {
    protected final String TAG = getClass().getSimpleName();
    protected Unbinder unbinder;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtils.d(TAG, TAG + "-->onCreateView()");
        View view = inflater.inflate(setLayoutId(), container, false);
        initConfig();
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtils.d(TAG, TAG + "-->onActivityCreated()");
        doBusiness();
    }

    protected abstract void initConfig();

    protected abstract int setLayoutId();

    protected  abstract void doBusiness();

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d(TAG, TAG + "-->onDestroy()");
        if (unbinder != null){
            unbinder.unbind();
        }
    }
}
