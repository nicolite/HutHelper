package cn.nicolite.huthelper.listener

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * Fragment生命周期监听
 * Created by nicolite on 17-11-4.
 */

interface FragmentLifeCycleListener {

    fun onAttach(context: Context?)

    fun onCreate(saveInstanceState: Bundle?)

    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)

    fun onActivityCreated(savedInstanceState: Bundle?)

    fun onResume()

    fun onPause()

    fun onStart()

    fun onStop()

    fun onRestart()

    fun onDestroy()
    fun onDestroyView()

    fun onDetach()


}
