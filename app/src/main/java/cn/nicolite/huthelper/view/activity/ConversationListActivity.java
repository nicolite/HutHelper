package cn.nicolite.huthelper.view.activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.activity.BaseActivity;
import cn.nicolite.huthelper.utils.LogUtils;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * 消息列表
 * Created by nicolite on 17-10-24.
 */

public class ConversationListActivity extends BaseActivity {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.rootView)
    CoordinatorLayout rootView;

    @Override
    protected void initConfig(Bundle savedInstanceState) {
        setDeepColorStatusBar(true);
        setImmersiveStatusBar(true);
        setSlideExit(true);
    }

    @Override
    protected void initBundleData(Bundle bundle) {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_conversationlist;
    }

    @Override
    protected void doBusiness() {
        toolbarTitle.setText("消息列表");

        if ( RongIMClient.getInstance().getCurrentConnectionStatus()
                == RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED  ){
            String token = getSharedPreferences("RongIM", MODE_PRIVATE).getString("token", null);
            if (!TextUtils.isEmpty(token)){
                RongIM.connect(token, new RongIMClient.ConnectCallback() {
                    @Override
                    public void onTokenIncorrect() {
                        LogUtils.d(TAG, "onTokenIncorrect: " + "Token错误！");
                    }

                    @Override
                    public void onSuccess(String s) {
                        LogUtils.d(TAG, "onSuccess: " + "连接成功" +  s);
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        LogUtils.d(TAG, "onError: " + "融云服务器错误：" + errorCode);
                    }
                });
            }
        }
    }


    @OnClick({R.id.toolbar_back, R.id.toolbar_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.toolbar_search:
                break;
        }
    }
}
