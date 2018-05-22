package cn.nicolite.huthelper.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.model.Constants;
import cn.nicolite.huthelper.utils.ListUtils;

/**
 * Created by nicolite on 17-11-10.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context context;
    private List<String> list;
    private List<Uri> uriList;

    public static final int STRING = 300;
    public static final int URI = 57;
    private int flag = STRING;
    private OnItemClickListener onItemClickListener;

    public ImageAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    public ImageAdapter(Context context, List<Uri> uriList, int flag) {
        this.context = context;
        this.uriList = uriList;
        this.flag = flag;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, int position) {
        if (flag == STRING) {
            Glide
                    .with(context)
                    .load(Constants.PICTURE_URL + list.get(position))
                    .placeholder(R.drawable.img_loading)
                    .error(R.drawable.img_error)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .centerCrop()
                    .crossFade()
                    .into(holder.image);
        } else if (flag == URI) {
            Glide
                    .with(context)
                    .load(uriList.get(position))
                    .placeholder(R.drawable.img_loading)
                    .error(R.drawable.img_error)
                    .skipMemoryCache(true)
                    .centerCrop()
                    .crossFade()
                    .into(holder.image);
        }

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(view, holder.getAdapterPosition(), holder.getItemId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (flag == STRING) {
            return ListUtils.isEmpty(list) ? 0 : list.size();
        } else if (flag == URI) {
            return ListUtils.isEmpty(uriList) ? 0 : uriList.size();
        }
        return 0;
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        ImageView image;

        public ImageViewHolder(View itemView) {
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
