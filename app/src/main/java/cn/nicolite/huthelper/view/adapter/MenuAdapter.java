package cn.nicolite.huthelper.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.model.bean.Menu;

/**
 * Created by nicolite on 17-10-24.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {
    private List<Menu> menuList;
    private Context context;
    private boolean isEidt;

    public MenuAdapter(List<Menu> menuList, Context context) {
        this.menuList = menuList;
        this.context = context;
    }

    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_edit_menulist, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MenuViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return menuList == null? 0 : menuList.size();
    }


    static class MenuViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_menu_title)
        TextView tvTItle;
        @BindView(R.id.iv_menu_edit)
        ImageView ivEdit;
        @BindView(R.id.iv_menu_pic)
        ImageView ivPic;
        @BindView(R.id.rootView)
        RelativeLayout rootView;
        public MenuViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
