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
import cn.nicolite.huthelper.model.bean.LostAndFound;
import cn.nicolite.huthelper.utils.ListUtils;
import cn.nicolite.huthelper.view.customView.PictureGridView;

/**
 * Created by nicolite on 17-11-12.
 */

public class LostAndFoundAdapter extends RecyclerView.Adapter<LostAndFoundAdapter.LostAndFoundViewHolder> {

    private Context context;
    private List<LostAndFound> lostAndFoundList;
    private final int[] bg = {R.drawable.bg_list, R.drawable.bg2_list, R.drawable.bg3_list, R.drawable.bg4_list};

    public LostAndFoundAdapter(Context context, List<LostAndFound> lostAndFoundList) {
        this.context = context;
        this.lostAndFoundList = lostAndFoundList;
    }

    @Override
    public LostAndFoundViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lost_and_found, parent, false);
        return new LostAndFoundViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LostAndFoundViewHolder holder, int position) {
        LostAndFound lostAndFound = lostAndFoundList.get(position);
        holder.tvItemLoseContent.setText(String.valueOf(lostAndFound.getTit() + "\n" + lostAndFound.getLocate() + "\n" + lostAndFound.getContent()));
        holder.tvItemLoseAuthor.setText(lostAndFound.getUsername());
        holder.tvItemLoseTime.setText(lostAndFound.getCreated_on());
        List<String> images = lostAndFound.getPics();
        holder.gvItemLoseImg.setVisibility(View.GONE);

        int num = lostAndFound.getContent().length() % 4;
        holder.rootView.setBackgroundResource(bg[num]);

        if (images != null && images.size() != 0) {
            holder.gvItemLoseImg.setVisibility(View.VISIBLE);
            holder.gvItemLoseImg.setAdapter(new SmallBitmapGridViewAdapter(context, images));
        }
    }

    @Override
    public int getItemCount() {
        return ListUtils.isEmpty(lostAndFoundList) ? 0 : lostAndFoundList.size();
    }

    static class LostAndFoundViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_item_lose_content)
        TextView tvItemLoseContent;
        @BindView(R.id.gv_item_lose_img)
        PictureGridView gvItemLoseImg;
        @BindView(R.id.tv_item_lose_author)
        TextView tvItemLoseAuthor;
        @BindView(R.id.tv_item_lose_time)
        TextView tvItemLoseTime;
        @BindView(R.id.rootView)
        LinearLayout rootView;

        public LostAndFoundViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
