package cn.nicolite.huthelper.listener;

import android.os.Bundle;

/**
 * Activity生命周期监听
 * Created by nicolite on 17-10-14.
 */

public interface ActivityLifeCycleListener {

    void onCreate(Bundle saveInstanceState);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

    void onRestart();
}
