package cn.nicolite.huthelper.receiver;

import android.content.Context;

import cn.nicolite.huthelper.utils.LogUtils;
import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

/**
 * 融云消息推送
 * Created by nicolite on 17-8-12.
 */

public class RongIMNotificationReceiver extends PushMessageReceiver {
    private static final String TAG = "Notification";
    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage pushNotificationMessage) {
        LogUtils.d(TAG, "onNotificationMessageArrived: " + pushNotificationMessage.getTargetUserName());
        return false;
    }

    @Override
    public boolean onNotificationMessageClicked(final Context context, final PushNotificationMessage pushNotificationMessage) {
        return false;
    }
}
