package cn.nicolite.huthelper.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    private String userId;
    private int sayPosition;
    public CommentAdapter commentAdapter;

    public CommentAdapter(Context context, List<Say.CommentsBean> commentsBeanList, String userId, int sayPosition) {
        this.context = context;
        this.commentsBeanList = commentsBeanList;
        this.userId = userId;
        this.sayPosition = sayPosition;
        commentAdapter = this;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comments, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CommentViewHolder holder, final int position) {
        final Say.CommentsBean commentsBean = commentsBeanList.get(position);
        String userName = commentsBean.getUsername() + ": ";
        String content = userName + commentsBean.getComment();
        SpannableStringBuilder richContent = new SpannableStringBuilder(content);

        richContent.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                if (onItemClickListener != null) {
                    onItemClickListener.onUserClick(position, commentsBean.getUser_id(), commentsBean.getUsername());
                }
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        }, 0, userName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        richContent.setSpan(new ForegroundColorSpan(Color.parseColor("#1dcbdb")), 0, userName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        if (commentsBean.getUser_id().equals(userId) && !TextUtils.isEmpty(commentsBean.getId())) {
            richContent.append("  删除");
            richContent.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    if (onItemClickListener != null && sayPosition > -1) {
                        onItemClickListener.onDeleteClick(sayPosition, position, commentsBean);
                    }
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                }
            }, content.length(), richContent.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            richContent.setSpan(new ForegroundColorSpan(Color.RED), content.length(), richContent.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        holder.text.setText(richContent);
        holder.text.setMovementMethod(LinkMovementMethod.getInstance());

    }

    @Override
    public int getItemCount() {
        return ListUtils.isEmpty(commentsBeanList) ? 0 : commentsBeanList.size();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text)
        TextView text;

        public CommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onUserClick(int position, String userId, String username);

        void onDeleteClick(int sayPosition, int commentPosition, Say.CommentsBean commentsBean);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
