package cn.nicolite.huthelper.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.activity.BaseActivity;
import cn.nicolite.huthelper.manager.ActivityStackManager;
import cn.nicolite.huthelper.view.customView.CommonDialog;

/**
 * 弹出对话框
 * Created by nicolite on 17-11-2.
 */

public class OffsiteLoginDialogActivity extends BaseActivity {

    private CommonDialog commonDialog;

    @Override
    protected void initConfig(Bundle savedInstanceState) {
        setImmersiveStatusBar(true);
    }

    @Override
    protected void initBundleData(Bundle bundle) {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_offsite_login_dialog;
    }

    @Override
    protected void doBusiness() {

        SharedPreferences.Editor editor = getSharedPreferences("login_user", MODE_PRIVATE).edit();
        editor.putString("userId", "*");
        editor.apply();

       //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.CHINA);
       //String date = simpleDateFormat.format(new Date());

        commonDialog = new CommonDialog(context);
        commonDialog
                .setTitle("下线通知")
                .setMessage("你的帐号已在另一台设备登录。如非本人操作，则密码可能已泄露，建议修改密码。")
                .setMessageGravity(Gravity.START | Gravity.CENTER_VERTICAL)
                .setCancelable(false)
                .setPositiveButton("重新登录", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        commonDialog.dismiss();
                        Intent intent = new Intent(context, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("退出", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        commonDialog.dismiss();
                        ActivityStackManager.getManager().exitApp(context);
                    }
                })
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (commonDialog != null){
            commonDialog.dismiss();
        }
    }
}
