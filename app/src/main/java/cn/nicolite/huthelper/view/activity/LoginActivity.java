package cn.nicolite.huthelper.view.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.OnClick;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.BaseActivity;
import cn.nicolite.huthelper.utils.KeyBoardUtils;
import cn.nicolite.huthelper.utils.SnackbarUtils;
import cn.nicolite.huthelper.utils.ToastUtil;
import cn.nicolite.huthelper.view.customView.LoadingDialog;
import cn.nicolite.huthelper.view.iview.ILoginView;
import cn.nicolite.huthelper.view.presenter.LoginPresenter;

/**
 * 登录页面
 * Created by nicolite on 17-10-17.
 */

public class LoginActivity extends BaseActivity implements ILoginView {
    @BindView(R.id.rootView)
    LinearLayout rootView;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    private LoginPresenter loginPresenter;
    private LoadingDialog loadingDialog;

    @Override
    protected void initConfig(Bundle savedInstanceState) {
        setImmersiveStatusBar(true);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void doBusiness() {
        KeyBoardUtils.scrollLayoutAboveKeyBoard(context, rootView, btnLogin);

        SharedPreferences.Editor editor = getSharedPreferences("login_user", MODE_PRIVATE).edit();
        editor.putString("userId", "*");
        editor.apply();

        loginPresenter = new LoginPresenter(this, this);

    }

    @OnClick(R.id.btn_login)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                KeyBoardUtils.hideSoftInput(context, getWindow());

                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    ToastUtil.showToastShort("请填写完整再提交");
                    return;
                }

                loginPresenter.login(username, password);
                break;
        }
    }

    @Override
    public void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(context)
                    .setLoadingText("登录中...");
        }
        loadingDialog.show();
    }

    @Override
    public void closeLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void showMessage(String msg) {
        SnackbarUtils.INSTANCE.showShortSnackbar(rootView, msg);
    }

    @Override
    public void onSuccess() {
        startActivity(MainActivity.class);
        finish();
    }
}
