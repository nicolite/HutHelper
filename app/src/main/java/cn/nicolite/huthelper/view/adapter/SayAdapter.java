package cn.nicolite.huthelper.view.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.app.MApplication;
import cn.nicolite.huthelper.model.Constants;
import cn.nicolite.huthelper.model.bean.Say;
import cn.nicolite.huthelper.model.bean.SayLikedCache;
import cn.nicolite.huthelper.utils.AnimationTools;
import cn.nicolite.huthelper.utils.ListUtils;
import cn.nicolite.huthelper.view.customView.NinePictureLayout;
import cn.nicolite.huthelper.view.customView.NoScrollLinearLayoutManager;
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
        SharedPreferences preferences = MApplication.Companion.getAppContext().getSharedPreferences("login_user", Context.MODE_PRIVATE);
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
        String head_pic = !TextUtils.isEmpty(say.getHead_pic()) ? say.getHead_pic() : say.getHead_pic_thumb();
        //下载任务查询ID
        final String task = "https://img.tuzhaozhao.com/2019/01/18/333f9619d6a0d5fa_600x600.jpg";

        Glide
                .with(context)
                .load(Constants.PICTURE_URL + head_pic)
//                .load(task)
                .bitmapTransform(new CropCircleTransformation(context))
                .placeholder(R.drawable.say_default_head)
                .skipMemoryCache(true)
                .error(R.drawable.say_default_head)
                .dontAnimate()
                .into(holder.ivItemSayAvatar);

        /*
         * TODO 完善下载进度逻辑
         * */
//        DownloadSchedule.query(task)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<DownloadSchedule.Config>() {
//                    Disposable mDisposable = null;
//
//                    void fin(Disposable d, DownloadSchedule.Config c) {
//                        if (d != null && !d.isDisposed())
//                            d.dispose();
//                        DownloadSchedule.remove(c.getTask());
//                        holder.tvItemSayTime.setText("下载完成");
//
//                    }
//
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        mDisposable = d;
//                    }
//
//                    @Override
//                    public void onNext(DownloadSchedule.Config config) {
////                        if (config.isDone()) fin(mDisposable, config);
////                        else
//                        StringBuilder builder = new StringBuilder();
//                        if (config.isDone())
//                            builder.append("完成 ");
//                        else
//                            builder.append("未完成 ");
//                        if (config.isStart())
//                            builder.append("开始 ");
//                        else
//                            builder.append("未开始 ");
//                        builder.append("进度：").append(String.valueOf(config.getProgress())).append("%");
//                        holder.tvItemSayTime.setText(builder.toString());
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });

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
            NoScrollLinearLayoutManager layout = new NoScrollLinearLayoutManager(context, OrientationHelper.VERTICAL, false);
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
        ImageView ivItemSayAvatar;

        TextView tvItemSayauthor;

        TextView tvItemSayTime;

        TextView tvItemSaycontent;

        NinePictureLayout rvItemSayimg;

        TextView tvItemSayXy;

        ImageView ivSayItemAddcommit;

        TextView tvSayItemCommitnum;

        ImageView ivSayItemLike;

        TextView tvSayItemLikenum;

        ImageView ivItemDeletesay;

        LinearLayout llSayitem;

        ImageView ivItemSay;

        RecyclerView rvSayComments;

        public SayViewHolder(View itemView) {
            super(itemView);
            ivItemSayAvatar = (ImageView) itemView.findViewById(R.id.iv_item_say_avatar);
            tvItemSayauthor = (TextView) itemView.findViewById(R.id.tv_item_sayauthor);
            tvItemSayTime = (TextView) itemView.findViewById(R.id.tv_item_say_time);
            tvItemSaycontent = (TextView) itemView.findViewById(R.id.tv_item_saycontent);
            rvItemSayimg = (NinePictureLayout) itemView.findViewById(R.id.rv_item_sayimg);
            tvItemSayXy = (TextView) itemView.findViewById(R.id.tv_item_say_xy);
            ivSayItemAddcommit = (ImageView) itemView.findViewById(R.id.iv_say_item_addcommit);
            tvSayItemCommitnum = (TextView) itemView.findViewById(R.id.tv_say_item_commitnum);
            ivSayItemLike = (ImageView) itemView.findViewById(R.id.iv_say_item_like);
            tvSayItemLikenum = (TextView) itemView.findViewById(R.id.tv_say_item_likenum);
            ivItemDeletesay = (ImageView) itemView.findViewById(R.id.iv_item_deletesay);
            llSayitem = (LinearLayout) itemView.findViewById(R.id.ll_sayitem);
            ivItemSay = (ImageView) itemView.findViewById(R.id.iv_item_say);
            rvSayComments = (RecyclerView) itemView.findViewById(R.id.rv_say_comments);
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
