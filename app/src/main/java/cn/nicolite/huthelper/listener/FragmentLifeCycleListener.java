package cn.nicolite.huthelper.listener;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Fragment生命周期监听
 * Created by nicolite on 17-11-4.
 */

public interface FragmentLifeCycleListener extends ActivityLifeCycleListener {

    void onAttach(Context context);

    void onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    void onActivityCreated(@Nullable Bundle savedInstanceState);

    void onDestroyView();

    void onDetach();
}
