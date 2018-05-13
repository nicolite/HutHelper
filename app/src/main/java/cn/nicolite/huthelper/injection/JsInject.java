package cn.nicolite.huthelper.injection;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;

import java.util.ArrayList;
import java.util.List;

import cn.nicolite.huthelper.utils.ListUtils;
import cn.nicolite.huthelper.view.activity.ShowImageActivity;

/**
 * JS注入
 * Created by nicolite on 17-10-13.
 */

public class JsInject {

    private Context context;

    public JsInject(Context context) {
        this.context = context;
    }


    /**
     * 展示单张图片
     * @param image
     */
    @JavascriptInterface
    public void showImage(String image){
        List<String> list = new ArrayList<>();
        list.add(image);
        showImages(list, 0);
    }

    /**
     * 展示多张图片
     * @param images
     * @param position
     */
    @JavascriptInterface
    public void showImages(List<String> images, int position){
        if (!ListUtils.isEmpty(images)){
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("images", (ArrayList<String>) images);
            bundle.putInt("curr", position);
            Intent intent = new Intent(context, ShowImageActivity.class);
            intent.putExtras(bundle);
            context.startActivity(intent);
        }
    }
}
