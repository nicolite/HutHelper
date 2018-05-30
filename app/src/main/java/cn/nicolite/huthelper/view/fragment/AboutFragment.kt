package cn.nicolite.huthelper.view.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import cn.nicolite.huthelper.BuildConfig
import cn.nicolite.huthelper.R
import cn.nicolite.huthelper.kBase.BaseFragment
import cn.nicolite.huthelper.model.Constants
import cn.nicolite.huthelper.utils.ToastUtils
import cn.nicolite.huthelper.view.activity.WebViewActivity
import com.bumptech.glide.Glide
import jp.wasabeef.glide.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.fragment_about.*

/**
 * Created by nicolite on 2018/5/30.
 * email nicolite@nicolite.cn
 */
class AboutFragment : BaseFragment() {
    override fun setLayoutId(): Int {
        return R.layout.fragment_about
    }

    override fun doBusiness() {
        super.doBusiness()
        Glide
                .with(activity)
                .load(R.drawable.logo256)
                .bitmapTransform(CropCircleTransformation(context))
                .skipMemoryCache(true)
                .dontAnimate()
                .into(logo)

        version.text = "V${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"

        help.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("type", WebViewActivity.TYPE_HELP)
            bundle.putString("url", Constants.HELP)
            bundle.putString("title", "帮助")
            startActivity(WebViewActivity::class.java, bundle)
        }

        permission_message.setOnClickListener {
            val bundle1 = Bundle()
            bundle1.putInt("type", WebViewActivity.TYPE_PERMISSION)
            bundle1.putString("url", Constants.PERMISSON)
            bundle1.putString("title", "软件许可协议")
            startActivity(WebViewActivity::class.java, bundle1)
        }

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
                val uri = Uri.parse("market://details?id=${BuildConfig.APPLICATION_ID}")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            } catch (e: Exception) {
                ToastUtils.showToastShort("您的手机没有安装Android应用市场")
                e.printStackTrace()
            }
        }
    }

}