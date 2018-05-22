package cn.nicolite.huthelper.view.adapter;

import android.content.Context;
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
import cn.nicolite.huthelper.model.Constants;
import cn.nicolite.huthelper.model.bean.Goods;
import cn.nicolite.huthelper.utils.ListUtils;

/**
 * Created by nicolite on 17-11-6.
 */

public class MarketAdapter extends RecyclerView.Adapter<MarketAdapter.MarketViewHolder> {

    private Context context;
    private List<Goods> goodsList;

    public MarketAdapter(Context context, List<Goods> goodsList) {
        this.context = context;
        this.goodsList = goodsList;
    }

    @Override
    public MarketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_market_list, parent, false);
        return new MarketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MarketViewHolder holder, int position) {
        Goods goodsBean = goodsList.get(position);

        Glide
                .with(context)
                .load(Constants.PICTURE_URL + goodsBean.getImage())
                .placeholder(R.drawable.img_loading)
                .error(R.drawable.img_error)
                .skipMemoryCache(true)
                .crossFade()
                .into(holder.image);

        holder.price.setText(String.valueOf("￥" + goodsBean.getPrize()));
        holder.title.setText(goodsBean.getTit());
        holder.date.setText(goodsBean.getCreated_on());

    }

    @Override
    public int getItemCount() {
        return ListUtils.isEmpty(goodsList)? 0 : goodsList.size();
    }

    static class MarketViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.rootView)
        LinearLayout rootView;
        public MarketViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
