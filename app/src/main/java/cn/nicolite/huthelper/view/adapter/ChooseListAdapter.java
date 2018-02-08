package cn.nicolite.huthelper.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.utils.ListUtils;

/**
 * Created by nicolite on 17-11-13.
 */

public class ChooseListAdapter extends BaseAdapter {
    private Context context;
    private List<String> stringList;

    public ChooseListAdapter(Context context, List<String> stringList) {
        this.context = context;
        this.stringList = stringList;
    }

    public int getSelectedPosition(){
        return context.getSharedPreferences("choose", Context.MODE_PRIVATE).getInt("position", 0);
    }
    @Override
    public int getCount() {
        return ListUtils.isEmpty(stringList) ? 0 : stringList.size();
    }

    @Override
    public Object getItem(int i) {
        return stringList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_text, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (i == getSelectedPosition()) {
            viewHolder.textView.setBackgroundResource(R.color.colorPrimary);
            viewHolder.textView.setTextColor(Color.WHITE);
        } else {
            viewHolder.textView.setBackgroundResource(R.color.transparent);
            viewHolder.textView.setTextColor(context.getResources().getColor(R.color.dull_grey));
        }
        viewHolder.textView.setText(stringList.get(i));
        return view;
    }

    static class ViewHolder{
        TextView textView;
        public ViewHolder(View itemView) {
            textView = (TextView) itemView.findViewById(R.id.text);
        }
    }
}
