package cn.nicolite.huthelper.view.activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.activity.BaseActivity;
import cn.nicolite.huthelper.model.Constants;
import cn.nicolite.huthelper.presenter.LoginPresenter;
import cn.nicolite.huthelper.utils.KeyBoardUtils;
import cn.nicolite.huthelper.utils.SnackbarUtils;
import cn.nicolite.huthelper.utils.ToastUtil;
import cn.nicolite.huthelper.view.iview.ILoginView;
import cn.nicolite.huthelper.view.customView.LoadingDialog;

/**
 * 登录页面
 * Created by nicolite on 17-10-17.
 */

public class LoginActivity extends BaseActivity implements ILoginView{
    @BindView(R.id.rootView)
    LinearLayout rootView;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.tv_message)
    TextView tvMessage;
    private LoginPresenter loginPresenter;
    private LoadingDialog loadingDialog;

    @Override
    protected void initConfig(Bundle savedInstanceState) {
        setImmersiveStatusBar(true);
    }

    @Override
    protected void initBundleData(Bundle bundle) {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void doBusiness() {
        SpannableString spannableString = new SpannableString("密码默认为身份证后六位（x小写） 忘记密码？");

        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Bundle bundle = new Bundle();
                bundle.putInt("type", WebViewActivity.TYPE_CHANGE_PWD);
                bundle.putString("title", "修改密码");
                bundle.putString("url", Constants.CHANGE_PWD);
                LoginActivity.this.startActivity(WebViewActivity.class, bundle);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#ff8f8f8f"));
                ds.setUnderlineText(true);
            }
        }, 17, 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvMessage.setText(spannableString);
        tvMessage.setMovementMethod(LinkMovementMethod.getInstance());

        KeyBoardUtils.scrollLayoutAboveKeyBoard(context, rootView, tvMessage);

        SharedPreferences.Editor editor = getSharedPreferences("login_user", MODE_PRIVATE).edit();
        editor.putString("userId", "*");
        editor.apply();

        loginPresenter = new LoginPresenter(this, this);

    }

    @OnClick(R.id.btn_login)
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                KeyBoardUtils.hideSoftInput(context, getWindow());

                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
                    ToastUtil.showToastShort("请填写完整再提交");
                    return;
                }

                loginPresenter.login(username, password);
                break;
        }
    }

    @Override
    public void showLoading() {
        if (loadingDialog == null){
            loadingDialog = new LoadingDialog(context)
                    .setLoadingText("登录中...");
        }
        loadingDialog.show();
    }

    @Override
    public void closeLoading() {
        if (loadingDialog != null){
            loadingDialog.dismiss();
        }
    }

    @Override
    public void showMessage(String msg) {
        SnackbarUtils.showShortSnackbar(rootView, msg);
    }

    @Override
    public void onSuccess() {
        startActivity(MainActivity.class);
        finish();
    }
}
