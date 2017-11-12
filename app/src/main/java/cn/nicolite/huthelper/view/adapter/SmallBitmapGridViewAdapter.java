package cn.nicolite.huthelper.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.model.Constants;
import cn.nicolite.huthelper.utils.DensityUtils;
import cn.nicolite.huthelper.utils.ScreenUtils;

/**
 * Created by nicolite on 17-11-12.
 */

public class SmallBitmapGridViewAdapter extends BaseAdapter {
    Context context;
    List<String> list;

    public SmallBitmapGridViewAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size() >= 2 ? 2 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        ImageView img = new ImageView(context);
        img.setBackgroundResource(R.color.white);
        img.setPadding(2, 2, 2, 2);
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);

        int width = ScreenUtils.getScreenWidth(context) - DensityUtils.dp2px(context, 117);// 获取屏幕宽度
        int height = width = width / 4;// 对当前的列数进行设置imgView的宽度
        img.setLayoutParams(new AbsListView.LayoutParams(width, height));

        Glide
                .with(context)
                .load(Constants.PICTURE_URL + list.get(position))
                .override(width, height)
                .skipMemoryCache(true)
                .centerCrop()
                .crossFade()
                .placeholder(R.drawable.img_loading)
                .error(R.drawable.img_error)
                .into(img);

        return img;
    }
}

