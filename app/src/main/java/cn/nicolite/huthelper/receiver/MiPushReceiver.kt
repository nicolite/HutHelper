package cn.nicolite.huthelper.receiver

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import cn.nicolite.huthelper.app.MApplication
import cn.nicolite.huthelper.db.DaoHelper
import cn.nicolite.huthelper.db.dao.DaoSession
import cn.nicolite.huthelper.model.Constants
import cn.nicolite.huthelper.model.bean.Notice
import cn.nicolite.mvp.utils.LogUtils
import com.xiaomi.mipush.sdk.MiPushClient
import com.xiaomi.mipush.sdk.MiPushCommandMessage
import com.xiaomi.mipush.sdk.MiPushMessage
import com.xiaomi.mipush.sdk.PushMessageReceiver
import java.text.SimpleDateFormat
import java.util.*

/**
 * 小米推送
 * Created by nicolite on 17-9-13.
 */

class MiPushReceiver : PushMessageReceiver() {
    private val TAG = "MiPushReceiver"
    /**
     * 获取daoSession
     */
    private val daoSession: DaoSession
        get() = DaoHelper.getDaoHelper(MApplication.appContext).daoSession

    /**
     * 获取当前登录用户
     */
    private val loginUser: String
        get() {
            val preferences = MApplication.appContext.getSharedPreferences("login_user", Context.MODE_PRIVATE)
            return preferences.getString("userId", "")
        }

    override fun onReceivePassThroughMessage(context: Context?, message: MiPushMessage?) {
        if (context != null && message != null) {
            saveNoticeToDb(context, message.title, message.content)
        }
    }

    override fun onNotificationMessageClicked(context: Context?, message: MiPushMessage?) {

    }

    override fun onNotificationMessageArrived(context: Context?, message: MiPushMessage?) {
        if (context != null && message != null) {
            saveNoticeToDb(context, message.title, message.content)
        }
    }

    override fun onCommandResult(context: Context?, message: MiPushCommandMessage?) {

    }

    override fun onReceiveRegisterResult(context: Context?, message: MiPushCommandMessage?) {

    }

    private fun saveNoticeToDb(context: Context?, title: String, content: String) {
        LogUtils.d(TAG, "title: $title content: $content")
        MiPushClient.clearNotification(context)

        val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.CHINA)

        val notice = Notice()
        notice.title = title
        notice.content = content
        notice.time = simpleDateFormat.format(Date())
        notice.userId = loginUser
        val noticeDao = daoSession.noticeDao
        noticeDao.insert(notice)

        //发送广播通知更新
        val intent = Intent(Constants.MainBroadcast)
        val bundle = Bundle()
        bundle.putInt("type", Constants.BROADCAST_TYPE_NOTICE)
        intent.putExtras(bundle)
        LocalBroadcastManager.getInstance(context!!).sendBroadcast(intent)
    }

}
