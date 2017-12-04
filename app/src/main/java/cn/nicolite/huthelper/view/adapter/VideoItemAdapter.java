package cn.nicolite.huthelper.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.model.bean.Video;
import cn.nicolite.huthelper.utils.ListUtils;

/**
 * Created by nicolite on 17-12-4.
 */

public class VideoItemAdapter extends RecyclerView.Adapter<VideoItemAdapter.VideoItemViewHolder> {
    private Context context;
    List<Video.LinksBean.VedioListBean> videoList;
    private OnItemClickListener onItemClickListener;

    private final int[] bg = {R.drawable.bg_list_empty, R.drawable.bg2_list_empty, R.drawable.bg3_list_empty, R.drawable.bg4_list_empty};

    public VideoItemAdapter(Context context, List<Video.LinksBean.VedioListBean> videoList) {
        this.context = context;
        this.videoList = videoList;
    }

    @Override
    public VideoItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video_item, parent, false);
        return new VideoItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final VideoItemViewHolder holder, final int position) {
        int num = (int) (Math.random() * 4);
        holder.rootView.setBackgroundResource(bg[num]);
        holder.tvVideoitemContent.setText(videoList.get(position).getTitle());
        holder.tvVideoitemNum.setText(String.valueOf("第" + (position + 1) + "集"));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v, position, holder.getItemId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return ListUtils.isEmpty(videoList) ? 0 : videoList.size();
    }

    static class VideoItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_videoitem_num)
        TextView tvVideoitemNum;
        @BindView(R.id.tv_videoitem_content)
        TextView tvVideoitemContent;
        @BindView(R.id.rootView)
        LinearLayout rootView;

        public VideoItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, long itemId);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
