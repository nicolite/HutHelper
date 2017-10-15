package cn.nicolite.huthelper.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.stat.MtaSDkException;
import com.tencent.stat.StatCrashReporter;
import com.tencent.stat.StatService;
import com.tencent.stat.common.StatConstants;

import cn.nicolite.huthelper.view.activity.MainActivity;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.push.RongPushClient;

/**
 * Created by nicolite on 17-9-6.
 */

public class InitializeService extends IntentService {

    private static final String ACTION_INIT_WHEN_APP_CREATE = "cn.nicolite.huthelper.service.action.INIT";

    private static final String TAG = "InitializeService";

    public InitializeService(){
        super("InitializeService");
    }

    public static void start(Context context){
        Intent intent = new Intent(context, InitializeService.class);
        intent.setAction(ACTION_INIT_WHEN_APP_CREATE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null){
            if (intent.getAction().equals(ACTION_INIT_WHEN_APP_CREATE)){
                performInit();
            }
        }
    }

    private void performInit(){

        //注册融云小米push
        RongPushClient.registerMiPush(getApplicationContext(), "2882303761517605932", "5561760591932");

        //初始化融云
        RongIM.init(getApplicationContext());

        //设置支持消息回执的会话类型
        Conversation.ConversationType[] types = new Conversation.ConversationType[]{
                Conversation.ConversationType.PRIVATE,
                Conversation.ConversationType.GROUP,
                Conversation.ConversationType.DISCUSSION
        };

        RongIM.getInstance().setReadReceiptConversationTypeList(types);

        try {
            StatService.startStatService(getApplicationContext(), "ACIDT96D3M7R", StatConstants.VERSION);
            //开启Java Crash异常捕获
            StatCrashReporter.getStatCrashReporter(getApplicationContext()).setJavaCrashHandlerStatus(true);
            //开启Native异常捕获
            // StatCrashReporter.getStatCrashReporter(getApplicationContext()).setJniNativeCrashStatus(true);
        } catch (MtaSDkException e) {
            e.printStackTrace();
        }

        //只在MainActivity上显示升级对话框
        Beta.canShowUpgradeActs.add(MainActivity.class);
        //初始化腾讯Bugly
        Bugly.init(getApplicationContext(), "01c78d2135", false);
    }

}
