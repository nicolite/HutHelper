package cn.nicolite.huthelper.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;

import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.utils.DensityUtils;
import cn.nicolite.huthelper.utils.ScreenUtils;

/**
 * Created by nicolite on 17-12-5.
 * TODO 暂时不能用
 */

public class SyllabusItemAdapter extends BaseAdapter {
    private Context context;
    private boolean[] weeklist;

    public SyllabusItemAdapter(Context context, boolean[] list) {
        this.context = context;
        this.weeklist = list;
    }

    @Override
    public int getCount() {
        return weeklist.length;
    }

    @Override
    public Object getItem(int i) {
        return weeklist[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final Button img = new Button(context);
        int width = ScreenUtils.getScreenWidth(context) - DensityUtils.dp2px(context, 45);// 获取屏幕宽度
        int height = 0;
        width = width / 5;// 对当前的列数进行设置imgView的宽度
        height = width * 5 / 6;
        if (!weeklist[i]) {
            img.setBackgroundResource(R.color.new_grty);
        } else {
            img.setBackgroundResource(R.color.colorPrimary);
        }
        img.setText(String.valueOf(i + 1));
        img.setTextColor(Color.WHITE);
        AbsListView.LayoutParams layout = new AbsListView.LayoutParams(width, height);
        img.setLayoutParams(layout);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //if (type == SHOW_COURSE)
               //    return;
                if (weeklist[i]) {
                    img.setBackgroundResource(R.color.new_grty);
                    weeklist[i] = false;
                } else if (!weeklist[i]) {
                    img.setBackgroundResource(R.color.colorPrimary);
                    weeklist[i] = true;
                }
            }
        });
        return img;
    }
}
