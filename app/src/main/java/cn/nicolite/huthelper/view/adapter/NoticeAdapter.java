package cn.nicolite.huthelper.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.model.bean.Notice;
import cn.nicolite.huthelper.utils.ListUtils;
import cn.nicolite.huthelper.view.activity.NoticeItemActivity;
import cn.nicolite.huthelper.view.activity.WebViewActivity;
import cn.nicolite.huthelper.view.customView.RichTextView;

/**
 * Created by nicolite on 17-12-4.
 */

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder> {
    private Context context;
    private List<Notice> noticeList;

    public NoticeAdapter(Context context, List<Notice> noticeList) {
        this.context = context;
        this.noticeList = noticeList;
    }

    @Override
    public NoticeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notice, parent, false);
        return new NoticeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoticeViewHolder holder, final int position) {
        final Notice notice = noticeList.get(position);
        holder.tvNoticeitemTime.setText(notice.getTime());
        holder.tvNoticeitemTitle.setText(notice.getTitle());
        holder.tvNoticeitemContent.setText(notice.getContent());
        holder.btnNoticeitemShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = notice.getType();
                if ("url".equals(type)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("url", notice.getContent());
                    bundle.putInt("type", WebViewActivity.TYPE_NOTICE);
                    bundle.putString("title", notice.getTitle());
                    Intent intent = new Intent(context, WebViewActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putLong("noticeId", notice.getId());
                    Intent intent = new Intent(context, NoticeItemActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return ListUtils.isEmpty(noticeList) ? 0 : noticeList.size();
    }

    static class NoticeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_noticeitem_title)
        TextView tvNoticeitemTitle;
        @BindView(R.id.tv_noticeitem_time)
        RichTextView tvNoticeitemTime;
        @BindView(R.id.tv_noticeitem_content)
        TextView tvNoticeitemContent;
        @BindView(R.id.btn_noticeitem_show)
        Button btnNoticeitemShow;

        public NoticeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
