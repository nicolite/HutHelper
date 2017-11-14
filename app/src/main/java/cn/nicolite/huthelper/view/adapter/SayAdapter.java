package cn.nicolite.huthelper.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.model.Constants;
import cn.nicolite.huthelper.model.bean.Say;
import cn.nicolite.huthelper.utils.ListUtils;
import cn.nicolite.huthelper.view.activity.ShowImageActivity;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by nicolite on 17-11-14.
 */

public class SayAdapter extends RecyclerView.Adapter<SayAdapter.SayViewHolder> {

    private Context context;
    private List<Say> sayList;
    private OnItemClickListener onItemClickListener;
    private SayAdapter sayAdapter;
    private List<Say.CommentsBean> commentsBeanList = new ArrayList<>();

    public SayAdapter(Context context, List<Say> sayList) {
        this.context = context;
        this.sayList = sayList;
        sayAdapter = this;
    }

    @Override
    public SayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_say_list, parent, false);
        return new SayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SayViewHolder holder, int position) {
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

        final List<String> pics = say.getPics();
        holder.rvItemSayimg.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));
        ImageAdapter imageAdapter = new ImageAdapter(context, pics);
        holder.rvItemSayimg.setAdapter(imageAdapter);

        imageAdapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, long itemId) {
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("images", (ArrayList<String>) pics);
                bundle.putInt("curr", position);
                Intent intent = new Intent(context, ShowImageActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        commentsBeanList.clear();
        commentsBeanList.addAll(say.getComments());
        if (ListUtils.isEmpty(commentsBeanList)) {
            holder.rvSayComments.setVisibility(View.GONE);
            holder.ivItemSay.setVisibility(View.GONE);
            holder.tvSayItemCommitnum.setText("0");
        } else {
            holder.rvSayComments.setVisibility(View.VISIBLE);
            holder.tvSayItemCommitnum.setText(String.valueOf(commentsBeanList.size()));
            holder.ivItemSay.setVisibility(View.VISIBLE);
        }

        holder.rvSayComments.setLayoutManager(new LinearLayoutManager(context, OrientationHelper.VERTICAL, false));
        final CommentAdapter commentAdapter = new CommentAdapter(context, commentsBeanList);
        holder.rvSayComments.setAdapter(commentAdapter);
        commentAdapter.setOnItemClickListener(new CommentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.Adapter adapter, int position, long itemId, String userId, String username) {
                if (onItemClickListener != null) {
                    onItemClickListener.onUserClick(adapter, position, itemId, userId, username);
                }
            }
        });


        holder.ivItemSayAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onUserClick(sayAdapter, holder.getAdapterPosition(), holder.getItemId(),
                            say.getUser_id(), say.getUsername());
                }
            }
        });

        holder.tvItemSayauthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onUserClick(sayAdapter, holder.getAdapterPosition(), holder.getItemId(),
                            say.getUser_id(), say.getUsername());
                }
            }
        });

        holder.ivItemDeletesay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onDeleteClick(sayAdapter, holder.getAdapterPosition(), holder.getItemId(),
                            say.getId());
                }
            }
        });

        holder.ivSayItemLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onLikeClick(sayAdapter, holder.ivSayItemLike, holder.tvSayItemLikenum,
                            Integer.parseInt(say.getLikes()), holder.getAdapterPosition(), holder.getItemId(),
                            say.getId());
                }
            }
        });

        holder.ivSayItemAddcommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onAddCommentClick(commentAdapter, commentsBeanList, holder.tvSayItemCommitnum,
                            commentsBeanList.size(), holder.getAdapterPosition(), holder.getItemId(), say.getId());
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
        RecyclerView rvItemSayimg;
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

        void onAddCommentClick(RecyclerView.Adapter adapter, List<Say.CommentsBean> commentsBeans,
                               TextView commentNumView, int commentNum, int position, long itemId,
                               String sayId);

        void onLikeClick(RecyclerView.Adapter adapter, ImageView likeView, TextView likeNumView,
                         int likeNum, int position, long itemId, String sayId);

        void onDeleteClick(RecyclerView.Adapter adapter, int position, long itemId, String sayId);

        void onUserClick(RecyclerView.Adapter adapter, int position, long itemId, String userId, String username);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
