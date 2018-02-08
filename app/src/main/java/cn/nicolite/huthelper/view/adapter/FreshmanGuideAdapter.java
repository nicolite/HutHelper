package cn.nicolite.huthelper.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.model.bean.FreshmanGuide;
import cn.nicolite.huthelper.utils.ListUtils;
import cn.nicolite.huthelper.view.activity.WebViewActivity;

/**
 * 新生攻略页面
 * Created by nicolite on 17-8-22.
 */

public class FreshmanGuideAdapter extends RecyclerView.Adapter<FreshmanGuideAdapter.FreshmanGuideViewHolder> {
    private Context context;
    private List<FreshmanGuide> dataList;

    public FreshmanGuideAdapter(Context context, List<FreshmanGuide> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public FreshmanGuideViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_freshman_guide, parent, false);
        return new FreshmanGuideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FreshmanGuideViewHolder holder, final int position) {
        final FreshmanGuide freshmanGuide = dataList.get(position);
        holder.itemText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("type", WebViewActivity.TYPE_FRESHMAN_GUIDE);
                bundle.putString("url", freshmanGuide.getUrl());
                bundle.putString("title", freshmanGuide.getTitle().replace("新生攻略手册--", ""));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        holder.itemText.setText(dataList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return ListUtils.isEmpty(dataList) ? 0 : dataList.size();
    }

    static class FreshmanGuideViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text)
        TextView itemText;

        public FreshmanGuideViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
