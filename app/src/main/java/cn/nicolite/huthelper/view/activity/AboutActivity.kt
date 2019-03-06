package cn.nicolite.huthelper.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import cn.nicolite.huthelper.BuildConfig
import cn.nicolite.huthelper.R
import cn.nicolite.huthelper.kbase.DefaultConfigActivity
import cn.nicolite.huthelper.model.Constants
import cn.nicolite.huthelper.utils.ToastUtil
import com.bumptech.glide.Glide
import jp.wasabeef.glide.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.toolbar_nomenu.*

/**
 * 关于页面
 * Created by nicolite on 17-10-22.
 */

class AboutActivity : DefaultConfigActivity() {
    override fun setLayoutId(): Int {
        return R.layout.activity_about
    }

    override fun doBusiness() {
        toolbar_title.text = "关于"
        toolbar_back.setOnClickListener {
            finish()
        }

        Glide
                .with(this)
                .load(R.drawable.logo256)
                .bitmapTransform(CropCircleTransformation(this))
                .skipMemoryCache(true)
                .dontAnimate()
                .into(logo)

        version.text = "${BuildConfig.VERSION_NAME}(${BuildConfig.VERSION_CODE})"

        blog.setOnClickListener {
            val bundle2 = Bundle()
            bundle2.putInt("type", WebViewActivity.TYPE_BLOG)
            bundle2.putString("url", Constants.BLOG)
            bundle2.putString("title", "nicolite")
            startActivity(WebViewActivity::class.java, bundle2)
        }
        openSource.setOnClickListener {
            val bundle3 = Bundle()
            bundle3.putInt("type", WebViewActivity.TYPE_OPEN_SOURCE)
            bundle3.putString("url", Constants.OPEN_SOURCE)
            bundle3.putString("title", "HutHelper")
            startActivity(WebViewActivity::class.java, bundle3)
        }
        rating.setOnClickListener {
            try {
                val uri = Uri.parse("market://details?id=$packageName")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            } catch (e: Exception) {
                ToastUtil.showToastShort("您的手机没有安装Android应用市场")
                e.printStackTrace()
            }
        }
    }
}
