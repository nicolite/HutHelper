package cn.nicolite.huthelper.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.text.TextUtils;
import android.widget.TextView;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.activity.BaseActivity;
import cn.nicolite.huthelper.utils.LogUtils;
import io.rong.imkit.RongIM;
import io.rong.imlib.MessageTag;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.TypingMessage.TypingStatus;
import io.rong.imlib.model.Conversation;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

/**
 * 私信聊天界面
 * Created by nicolite on 17-10-24.
 */

public class ConversationActivity extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.rootView)
    CoordinatorLayout rootView;

    public static final int SET_TEXT_TYPING_TITLE = 1;
    public static final int SET_VOICE_TYPING_TITLE = 2;
    public static final int SET_TARGET_ID_TITLE = 0;
    private final String TextTypingTitle = "对方正在输入...";
    private final String VoiceTypingTitle = "对方正在讲话...";
    private Handler mHandler;

    @Override
    protected void initConfig(Bundle savedInstanceState) {
        setDeepColorStatusBar(true);
        setSlideExit(true);
        setImmersiveStatusBar(true);
    }

    @Override
    protected void initBundleData(Bundle bundle) {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_conversation;
    }

    @Override
    protected void doBusiness() {

        if (RongIM.getInstance().getCurrentConnectionStatus()
                == RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED) {
            String token = getSharedPreferences("RongIM", MODE_PRIVATE).getString("token", null);
            if (!TextUtils.isEmpty(token)) {
                RongIM.connect(token, new RongIMClient.ConnectCallback() {
                    @Override
                    public void onTokenIncorrect() {
                    }

                    @Override
                    public void onSuccess(String s) {
                        LogUtils.d(TAG, "onSuccess: " + "连接成功" + s);
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                    }
                });
            }
        }

        final String userId = getIntent().getData().getQueryParameter("targetId");
        final String name = getIntent().getData().getQueryParameter("title");

        toolbarTitle.setText(userId);
        if (!TextUtils.isEmpty(name)) {
            toolbarTitle.setText(name);
        }

      /*  HttpMethods.getInstance()
                .getUserDataById(userId, new Subscriber<UserInfos>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        toolbarTitle.setText(userId);
                        LogUtils.d(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onNext(final UserInfos userInfos) {
                        final UserInfos.DataBean dataBean = userInfos.getData();
                        toolbarTitle.setText(dataBean.getTrueName());
                        LogUtils.d(TAG, "onNext: " + userId + " " + dataBean.getTrueName() + " " + dataBean.getHead_pic());
                        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
                            @Override
                            public UserInfo getUserInfo(String s) {
                                return new UserInfo(userId, dataBean.getTrueName(),
                                        Uri.parse(HttpMethods.BASE_URL + dataBean.getHead_pic()));
                            }
                        }, true);

                        RongIM.getInstance().refreshUserInfoCache(new UserInfo(userId, dataBean.getTrueName(),
                                Uri.parse(HttpMethods.BASE_URL + dataBean.getHead_pic())));
                    }
                }); */

        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case SET_TEXT_TYPING_TITLE:
                        toolbarTitle.setText(TextTypingTitle);
                        break;
                    case SET_VOICE_TYPING_TITLE:
                        toolbarTitle.setText(VoiceTypingTitle);
                        break;
                    case SET_TARGET_ID_TITLE:
                        toolbarTitle.setText(getIntent().getData().getQueryParameter("title"));
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        RongIMClient.setTypingStatusListener(new RongIMClient.TypingStatusListener() {
            @Override
            public void onTypingStatusChanged(Conversation.ConversationType conversationType, String s, Collection<TypingStatus> collection) {
                //当输入状态的会话类型和targetID与当前会话一致时，才需要显示
                if (conversationType.equals(Conversation.ConversationType.valueOf(getIntent().getData()
                        .getLastPathSegment().toUpperCase(Locale.US)))
                        && s.equals(getIntent().getData().getQueryParameter("targetId"))) {
                    //count表示当前会话中正在输入的用户数量，目前只支持单聊，所以判断大于0就可以给予显示了
                    int count = collection.size();
                    if (count > 0) {
                        Iterator iterator = collection.iterator();
                        TypingStatus status = (TypingStatus) iterator.next();
                        String objectName = status.getTypingContentType();

                        MessageTag textTag = TextMessage.class.getAnnotation(MessageTag.class);
                        MessageTag voiceTag = VoiceMessage.class.getAnnotation(MessageTag.class);
                        //匹配对方正在输入的是文本消息还是语音消息
                        if (objectName.equals(textTag.value())) {
                            //显示“对方正在输入”
                            mHandler.sendEmptyMessage(SET_TEXT_TYPING_TITLE);
                        } else if (objectName.equals(voiceTag.value())) {
                            //显示"对方正在讲话"
                            mHandler.sendEmptyMessage(SET_VOICE_TYPING_TITLE);
                        }
                    } else {
                        //当前会话没有用户正在输入，标题栏仍显示原来标题
                        mHandler.sendEmptyMessage(SET_TARGET_ID_TITLE);
                    }
                }
            }
        });

    }

    @OnClick(R.id.toolbar_back)
    public void onViewClicked() {
        finish();
    }
}
