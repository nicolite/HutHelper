package cn.nicolite.huthelper.utils

import android.app.Activity
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.View

/**
 * Created by nicolite on 17-6-29.
 */

object SnackbarUtils {
    fun showSnackbar(activity: Activity, msg: String, isShowLong: Boolean = false) {
        SnackbarUtils.showSnackbar(activity.window.decorView.rootView, msg, isShowLong)
    }

    fun showSnackbar(fragment: Fragment, msg: String, isShowLong: Boolean = false) {
        fragment.view?.let {
            SnackbarUtils.showSnackbar(it.rootView, msg, isShowLong)
        }
    }

    fun showSnackbar(view: View, msg: String, isShowLong: Boolean = false) {
        Snackbar.make(view, msg,
                if (isShowLong) {
                    Snackbar.LENGTH_LONG
                } else {
                    Snackbar.LENGTH_SHORT
                }
        ).show()

    }

    fun showShortSnackbar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }

    fun showLongSnackbar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show()
    }
}
