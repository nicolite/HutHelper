package cn.nicolite.huthelper.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.model.bean.Say;
import cn.nicolite.huthelper.utils.ListUtils;

/**
 * Created by nicolite on 17-11-14.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private Context context;
    private List<Say.CommentsBean> commentsBeanList;
    private OnItemClickListener onItemClickListener;
    public CommentAdapter commentAdapter;

    public CommentAdapter(Context context, List<Say.CommentsBean> commentsBeanList) {
        this.context = context;
        this.commentsBeanList = commentsBeanList;
        commentAdapter = this;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comments, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CommentViewHolder holder, int position) {
        final Say.CommentsBean commentsBean = commentsBeanList.get(position);
        holder.text.setText(Html.fromHtml(String.valueOf("<font color = '#1dcbdb'>" + commentsBean.getUsername() + "：</font>" + commentsBean.getComment())));
        holder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(commentAdapter, holder.getAdapterPosition(), holder.getItemId(),
                            commentsBean.getUser_id(), commentsBean.getUsername());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return ListUtils.isEmpty(commentsBeanList) ? 0 : commentsBeanList.size();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView text;

        public CommentViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(RecyclerView.Adapter adapter, int position, long itemId, String userId, String username);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
