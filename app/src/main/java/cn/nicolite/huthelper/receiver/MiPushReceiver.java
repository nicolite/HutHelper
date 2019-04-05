package cn.nicolite.huthelper.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.nicolite.huthelper.app.MApplication;
import cn.nicolite.huthelper.db.DaoHelper;
import cn.nicolite.huthelper.db.dao.DaoSession;
import cn.nicolite.huthelper.db.dao.NoticeDao;
import cn.nicolite.huthelper.model.Constants;
import cn.nicolite.huthelper.model.bean.Notice;
import cn.nicolite.mvp.utils.LogUtils;

/**
 * 信鸽推送
 * Created by nicolite on 17-9-13.
 */

public class MiPushReceiver extends PushMessageReceiver {
    private static final String TAG = "MiPushReceiver";

    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage message) {
        saveNoticeToDb(context, message.getTitle(), message.getContent());
    }

    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage message) {

    }

    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage message) {
        saveNoticeToDb(context, message.getTitle(), message.getContent());
    }

    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {

    }

    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {

    }

    @Override
    public void onRequirePermissions(Context context, String[] permissions) {
        super.onRequirePermissions(context, permissions);
    }

    /**
     * 获取daoSession
     */
    protected DaoSession getDaoSession() {
        return DaoHelper.getDaoHelper(MApplication.Companion.getAppContext()).getDaoSession();
    }

    /**
     * 获取当前登录用户
     */
    protected String getLoginUser() {
        SharedPreferences preferences = MApplication.Companion.getAppContext().getSharedPreferences("login_user", Context.MODE_PRIVATE);
        return preferences.getString("userId", null);
    }

    private void saveNoticeToDb(Context context, String title, String content) {
        LogUtils.d(TAG, "title: " + title + " content: " + content);
        MiPushClient.clearNotification(context);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.CHINA);

        Notice notice = new Notice();
        notice.setTitle(title);
        notice.setContent(content);
        notice.setTime(simpleDateFormat.format(new Date()));
        notice.setUserId(getLoginUser());
        NoticeDao noticeDao = getDaoSession().getNoticeDao();
        noticeDao.insert(notice);

        //发送广播通知更新
        Intent intent = new Intent(Constants.MainBroadcast);
        Bundle bundle = new Bundle();
        bundle.putInt("type", Constants.BROADCAST_TYPE_NOTICE);
        intent.putExtras(bundle);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

}
