package cn.nicolite.huthelper.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by nicolite on 17-6-29.
 */

public class SnackbarUtils {
    public static void showShortSnackbar(View view, String msg){
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
    }

    public static void showLongSnackbar(View view, String msg){
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
    }
}
