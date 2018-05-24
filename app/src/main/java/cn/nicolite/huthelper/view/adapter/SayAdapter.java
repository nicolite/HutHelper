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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.db.DaoUtils;
import cn.nicolite.huthelper.model.Constants;
import cn.nicolite.huthelper.model.bean.Say;
import cn.nicolite.huthelper.model.bean.SayLikedCache;
import cn.nicolite.huthelper.utils.AnimationTools;
import cn.nicolite.huthelper.utils.ListUtils;
import cn.nicolite.huthelper.view.customView.NinePictureLayout;
import cn.nicolite.huthelper.view.customView.PictureLayout;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by nicolite on 17-11-14.
 */

public class SayAdapter extends RecyclerView.Adapter<SayAdapter.SayViewHolder> {

    private Context context;
    private List<Say> sayList;
    private OnItemClickListener onItemClickListener;
    private String userId;
    private int sayPosition = -1;

    public SayAdapter(Context context, List<Say> sayList) {
        this.context = context;
        this.sayList = sayList;
        userId = DaoUtils.getLoginUser();
    }

    @NonNull
    @Override
    public SayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_say_list, parent, false);
        return new SayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SayViewHolder holder, final int position) {
        final Say say = sayList.get(position);
        sayPosition = position;
        List<Say.CommentsBean> comments = say.getComments();
        String imageUrl = TextUtils.isEmpty(say.getHead_pic()) ? Constants.PICTURE_URL + say.getHead_pic_thumb() :
                Constants.PICTURE_URL + say.getHead_pic();
        Glide
                .with(context)
                .load(imageUrl)
                .bitmapTransform(new CropCircleTransformation(context))
                .placeholder(R.drawable.say_default_head)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .error(R.drawable.say_default_head)
                .dontAnimate()
                .into(holder.ivItemSayAvatar);

        holder.tvItemSayauthor.setText(say.getUsername());
        holder.tvItemSayTime.setText(say.getCreated_on());
        holder.tvItemSayXy.setText(say.getDep_name());
        holder.tvSayItemLikenum.setText(say.getLikes());
        holder.tvItemSaycontent.setText(say.getContent());
        if (say.getUser_id().equals(userId)) {
            holder.ivItemDeletesay.setVisibility(View.VISIBLE);
        } else {
            holder.ivItemDeletesay.setVisibility(View.GONE);
        }

        say.setLike(SayLikedCache.isHave(say.getId()));
        // 取出bean中当记录状态是否为true，是的话则给img设置focus点赞图片
        if (say.isLike()) {
            holder.ivSayItemLike.setImageResource(R.drawable.ic_like);
        } else {
            holder.ivSayItemLike.setImageResource(R.drawable.ic_unlike);
        }
        holder.ivSayItemLike.setFocusable(false);
        holder.ivSayItemLike.setFocusableInTouchMode(false);

        holder.ivSayItemLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    if (say.isLike()) {
                        ((ImageView) view).setImageResource(R.drawable.ic_unlike);
                        say.setLike(false);
                        say.setLikes(String.valueOf(Integer.parseInt(say.getLikes()) - 1));
                        SayLikedCache.removeLike(say.getId());
                    } else {
                        ((ImageView) view).setImageResource(R.drawable.ic_like);
                        say.setLike(true);
                        say.setLikes(String.valueOf(Integer.parseInt(say.getLikes()) + 1));
                        SayLikedCache.addLike(say.getId());
                    }
                    holder.tvSayItemLikenum.setText(say.getLikes());
                    AnimationTools.scale(view);
                    onItemClickListener.onLikeClick(say.getId());
                }
            }
        });

        holder.ivItemDeletesay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onDeleteClick(say, holder.getAdapterPosition());
                }
            }
        });

        final List<String> pics = say.getPics();
        final List<String> picsRaw = new ArrayList<>();
        for (String item : pics) {
            picsRaw.add(item.replace("_thumb", ""));
        }
        holder.rvItemSayimg.setUrlList(picsRaw);

        if (comments.size() <= 0) {
            holder.ivItemSay.setVisibility(View.GONE);
            holder.tvSayItemCommitnum.setText("0");
        } else {
            holder.ivItemSay.setVisibility(View.VISIBLE);
            holder.tvSayItemCommitnum.setText(String.valueOf(comments.size()));
        }

        holder.ivItemSayAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onUserClick(say.getUser_id(), say.getUsername());
                }
            }
        });

        holder.tvItemSayauthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onUserClick(say.getUser_id(), say.getUsername());
                }
            }
        });


        holder.ivSayItemAddcommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onAddCommentClick(position, say.getId());
                }
            }
        });

        holder.rvItemSayimg.setOnClickImageListener(new PictureLayout.OnClickImageListener() {
            @Override
            public void onClickImage(int position, List<String> urlList) {
                onItemClickListener.onImageClick(position, picsRaw);
            }
        });

        holder.commentContainer.removeAllViews();
        for (int i = 0; i < comments.size(); i++) {
            Say.CommentsBean commentsBean = comments.get(i);
            addComment(holder.commentContainer, commentsBean);
        }
    }

    @Override
    public int getItemCount() {
        return ListUtils.isEmpty(sayList) ? 0 : sayList.size();
    }


    static class SayViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.say_root_view)
        RelativeLayout sayRootView;
        @BindView(R.id.iv_item_say_avatar)
        ImageView ivItemSayAvatar;
        @BindView(R.id.tv_item_sayauthor)
        TextView tvItemSayauthor;
        @BindView(R.id.tv_item_say_time)
        TextView tvItemSayTime;
        @BindView(R.id.tv_item_saycontent)
        TextView tvItemSaycontent;
        @BindView(R.id.rv_item_sayimg)
        NinePictureLayout rvItemSayimg;
        @BindView(R.id.tv_item_say_xy)
        TextView tvItemSayXy;
        @BindView(R.id.iv_say_item_addcommit)
        ImageView ivSayItemAddcommit;
        @BindView(R.id.tv_say_item_commitnum)
        TextView tvSayItemCommitnum;
        @BindView(R.id.iv_say_item_like)
        ImageView ivSayItemLike;
        @BindView(R.id.tv_say_item_likenum)
        TextView tvSayItemLikenum;
        @BindView(R.id.iv_item_deletesay)
        ImageView ivItemDeletesay;
        @BindView(R.id.ll_sayitem)
        LinearLayout llSayitem;
        @BindView(R.id.iv_item_say)
        View ivItemSay;
        @BindView(R.id.comment_container)
        LinearLayout commentContainer;

        public SayViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
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
        void onItemClick(RecyclerView.Adapter adapter, int position, long itemId);

        void onAddCommentClick(int position, String sayId);

        void onUserClick(String userId, String username);

        void onLikeClick(String sayId);

        void onDeleteClick(Say say, int position);

        void onImageClick(int position, List<String> urlList);

        void onCommentDeleteClick(int sayPosition, Say.CommentsBean commentsBean);

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void addComment(ViewGroup commentContainer, final Say.CommentsBean commentsBean) {
        TextView comment = (TextView) LayoutInflater.from(context).inflate(R.layout.item_comments, commentContainer, false);
        String userName = commentsBean.getUsername() + ": ";
        String content = userName + commentsBean.getComment();
        SpannableStringBuilder richContent = new SpannableStringBuilder(content);

        richContent.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                if (onItemClickListener != null) {
                    onItemClickListener.onUserClick(commentsBean.getUser_id(), commentsBean.getUsername());
                }
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        }, 0, userName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        richContent.setSpan(new ForegroundColorSpan(Color.parseColor("#1dcbdb")), 0, userName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

//        if (commentsBean.getUser_id().equals(userId)) {
//            richContent.append("  删除");
//            richContent.setSpan(new ClickableSpan() {
//                @Override
//                public void onClick(View widget) {
//                    if (onItemClickListener != null && sayPosition > -1) {
//                        onItemClickListener.onCommentDeleteClick(sayPosition, commentsBean);
//                    }
//                }
//
//                @Override
//                public void updateDrawState(TextPaint ds) {
//                    super.updateDrawState(ds);
//                    ds.setUnderlineText(false);
//                }
//            }, content.length(), richContent.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//            richContent.setSpan(new ForegroundColorSpan(Color.RED), content.length(), richContent.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }

        comment.setText(richContent);
        comment.setMovementMethod(LinkMovementMethod.getInstance());

        commentContainer.addView(comment);
    }
}
