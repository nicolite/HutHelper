package cn.nicolite.huthelper.injection

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.JavascriptInterface
import cn.nicolite.huthelper.utils.ListUtils
import cn.nicolite.huthelper.view.activity.ShowImageActivity
import java.util.*

/**
 * JS注入
 * Created by nicolite on 17-10-13.
 */

class JsInject(val context: Context) {

    /**
     * 展示单张图片
     *
     * @param image
     */
    @JavascriptInterface
    fun showImage(image: String) {
        val list = ArrayList<String>()
        list.add(image)
        showImages(list, 0)
    }

    /**
     * 展示多张图片
     *
     * @param images
     * @param position
     */
    @JavascriptInterface
    fun showImages(images: List<String>, position: Int) {
        if (!ListUtils.isEmpty(images)) {
            val bundle = Bundle()
            bundle.putStringArrayList("images", images as ArrayList<String>)
            bundle.putInt("curr", position)
            val intent = Intent(context, ShowImageActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }
}
