package cn.nicolite.huthelper.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.model.bean.SearchHistory;
import cn.nicolite.huthelper.utils.ListUtils;

/**
 * Created by nicolite on 17-11-10.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private Context context;
    private List<SearchHistory> searchHistoryList;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public SearchAdapter(Context context, List<SearchHistory> searchHistoryList) {
        this.context = context;
        this.searchHistoryList = searchHistoryList;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SearchViewHolder holder, int position) {
        SearchHistory searchHistory = searchHistoryList.get(position);
        holder.text.setText(searchHistory.getHistory());
        holder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(view, holder.getAdapterPosition(), holder.getItemId());
                }
            }
        });

        holder.text.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return onItemLongClickListener != null && onItemLongClickListener.onItemLongClick(view, holder.getAdapterPosition(), holder.getItemId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return ListUtils.isEmpty(searchHistoryList) ? 0 : searchHistoryList.size();
    }

    static class SearchViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text)
        TextView text;

        public SearchViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, long itemId);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(View view, int position, long itemId);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener){
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
