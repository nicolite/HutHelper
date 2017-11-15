package cn.nicolite.huthelper.view.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.app.MApplication;
import cn.nicolite.huthelper.model.Constants;
import cn.nicolite.huthelper.model.bean.Say;
import cn.nicolite.huthelper.model.bean.SayLikedCache;
import cn.nicolite.huthelper.utils.AnimationTools;
import cn.nicolite.huthelper.utils.ListUtils;
import cn.nicolite.huthelper.utils.LogUtils;
import cn.nicolite.huthelper.view.widget.NinePictureLayout;
import cn.nicolite.huthelper.view.widget.ScrollLinearLayoutManager;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by nicolite on 17-11-14.
 */

public class SayAdapter extends RecyclerView.Adapter<SayAdapter.SayViewHolder> {

    private Context context;
    private List<Say> sayList;
    private OnItemClickListener onItemClickListener;
    private String userId;

    public SayAdapter(Context context, List<Say> sayList) {
        this.context = context;
        this.sayList = sayList;
        SharedPreferences preferences = MApplication.AppContext.getSharedPreferences("login_user", Context.MODE_PRIVATE);
        userId = preferences.getString("userId", "");
    }

    @Override
    public SayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_say_list, parent, false);
        return new SayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SayViewHolder holder, final int position) {
        final Say say = sayList.get(position);

        Glide
                .with(context)
                .load(Constants.PICTURE_URL + say.getHead_pic_thumb())
                .bitmapTransform(new CropCircleTransformation(context))
                .placeholder(R.drawable.say_default_head)
                .skipMemoryCache(true)
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
        holder.rvItemSayimg.setUrlList(pics);

        int num = say.getComments().size();
        if (num == 0) {
            holder.rvSayComments.setVisibility(View.GONE);
            holder.ivItemSay.setVisibility(View.GONE);
            holder.tvSayItemCommitnum.setText("0");
        } else {
            holder.rvSayComments.setVisibility(View.VISIBLE);
            holder.tvSayItemCommitnum.setText(String.valueOf(say.getComments().size()));
            holder.ivItemSay.setVisibility(View.VISIBLE);
            holder.rvSayComments.setFocusable(false);
            holder.rvSayComments.setFocusableInTouchMode(false);
            ScrollLinearLayoutManager layout = new ScrollLinearLayoutManager(context, OrientationHelper.VERTICAL, false);
            layout.setScrollEnabled(false);
            holder.rvSayComments.setLayoutManager(layout);
            CommentAdapter commentAdapter = new CommentAdapter(context, say.getComments());
            holder.rvSayComments.setAdapter(commentAdapter);
            commentAdapter.setOnItemClickListener(new CommentAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(RecyclerView.Adapter adapter, int position, long itemId, String userId, String username) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onUserClick(userId, username);
                    }
                }
            });
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
    }

    @Override
    public int getItemCount() {
        return ListUtils.isEmpty(sayList) ? 0 : sayList.size();
    }

    static class SayViewHolder extends RecyclerView.ViewHolder {
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
        ImageView ivItemSay;
        @BindView(R.id.rv_say_comments)
        RecyclerView rvSayComments;

        public SayViewHolder(View itemView) {
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
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
