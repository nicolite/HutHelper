package cn.nicolite.huthelper.view.customView;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by gaop1 on 2017/4/11.
 */

public class CustomItemTouchHelper extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter mAdapter;

    public CustomItemTouchHelper(ItemTouchHelperAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN| ItemTouchHelper.START | ItemTouchHelper.END;
        //int swipeFlags =;
        return makeMovementFlags(dragFlags,0);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

    public interface ItemTouchHelperAdapter {
        void onItemMove(int fromPosition, int toPosition);

        void onItemDismiss(int position);
    }
}
