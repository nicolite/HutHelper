package cn.nicolite.huthelper.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import cn.nicolite.huthelper.R
import cn.nicolite.huthelper.base.BaseActivity
import cn.nicolite.huthelper.manager.ActivityStackManager
import cn.nicolite.huthelper.view.customView.CommonDialog

/**
 * 弹出对话框
 * Created by nicolite on 17-11-2.
 */

class OffsiteLoginDialogActivity : BaseActivity() {
    private lateinit var commonDialog: CommonDialog

    override fun initConfig(savedInstanceState: Bundle?) {
        setImmersiveStatusBar()
        setSlideExit()
    }

    override fun setLayoutId(): Int {
        return R.layout.activity_offsite_login_dialog
    }

    override fun doBusiness() {
        val editor = getSharedPreferences("login_user", Context.MODE_PRIVATE).edit()
        editor.putString("userId", "*")
        editor.apply()

        commonDialog = CommonDialog(context)
        commonDialog
                .setTitle("下线通知")
                .setMessage("你的帐号已在另一台设备登录。如非本人操作，则密码可能已泄露，建议修改密码。")
                .setMessageGravity(Gravity.START or Gravity.CENTER_VERTICAL)
                .setCancelable(false)
                .setPositiveButton("重新登录") {
                    commonDialog.dismiss()
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("退出") {
                    commonDialog.dismiss()
                    ActivityStackManager.getManager().exitApp(context)
                }
                .show()
    }

    override fun onDestroy() {
        commonDialog.dismiss()
        super.onDestroy()
    }
}
