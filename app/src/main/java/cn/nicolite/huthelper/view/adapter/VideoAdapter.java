package cn.nicolite.huthelper.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.model.bean.Video;
import cn.nicolite.huthelper.utils.DensityUtils;
import cn.nicolite.huthelper.utils.ListUtils;
import cn.nicolite.huthelper.utils.ScreenUtils;

/**
 * Created by nicolite on 17-12-4.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private Context context;
    private List<Video.LinksBean> videoList;


    public VideoAdapter(Context context, List<Video.LinksBean> videoList) {
        this.context = context;
        this.videoList = videoList;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video_list, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        holder.tvTitleVideo.setText(videoList.get(position).getName());
        holder.tvTotalVideo.setText(String.valueOf("共" + videoList.get(position).getVedioList().size() + "集"));
        int width = (ScreenUtils.getScreenWidth(context) - DensityUtils.dp2px(context, 45)) / 2;// 获取屏幕宽度
        int height = width * 3 / 4;
        Glide
                .with(context)
                .load(videoList.get(position).getImg())
                .override(width, height)
                .skipMemoryCache(true)
                .crossFade()
                .centerCrop()
                .into(holder.ivImgVideo);
    }

    @Override
    public int getItemCount() {
        return ListUtils.isEmpty(videoList) ? 0 : videoList.size();
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_img_video)
        ImageView ivImgVideo;
        @BindView(R.id.tv_title_video)
        TextView tvTitleVideo;
        @BindView(R.id.tv_total_video)
        TextView tvTotalVideo;

        public VideoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
