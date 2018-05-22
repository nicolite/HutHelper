package cn.nicolite.huthelper.listener

import android.os.Bundle

/**
 * Activity生命周期监听
 * Created by nicolite on 17-10-14.
 */

interface ActivityLifeCycleListener {

    fun onCreate(saveInstanceState: Bundle?)

    fun onStart()

    fun onResume()

    fun onPause()

    fun onStop()

    fun onDestroy()

    fun onRestart()
}
